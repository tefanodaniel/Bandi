package api;

import dao.*;
import exceptions.ApiError;
import exceptions.DaoException;
import model.*;
import spark.Spark;
import util.Database;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static spark.Spark.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import dao.MusicianDao;
import org.sql2o.Sql2o;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

public class ApiServer {

    public static Gson gson;
    public static MusicianDao musicianDao;
    public static BandDao bandDao;
    public static Sql2oSpeedDateEventDao speedDateEventDao;
    public static RequestDao requestDao;
    public static SongDao songDao;
    public static SotwSubmissionDao sotw_submissionDao;
    public static SotwEventDao sotw_eventDao;

    // flag for testing locally vs. deploying
    private static final boolean isLocalTest =
            Boolean.parseBoolean(System.getenv("isLocal"));
    // client id for Spotify
    private static final String client_id= "ae87181e126a4fd9ac434b67cf6f6f14";
    // Client Secret for using Spotify API (should never push to VCS)
    private static final String client_secret = System.getenv("client_secret");

    private static int getHerokuAssignedPort() {
        // Heroku stores port number as an environment variable
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        //return default port if heroku-port isn't set (i.e. on localhost)
        return 4567;
    }

    public static void main(String[] args) throws URISyntaxException {
        int myPort = getHerokuAssignedPort();
        port(myPort);
        staticFiles.location("/public");

        // static Gson object
        gson = new GsonBuilder().disableHtmlEscaping().create();
        // Dao objects
        musicianDao = getMusicianDao();
        bandDao = getBandDao();
        speedDateEventDao = getSpeedDateEventDao();
        requestDao = getRequestDao();
        songDao = getSongDao();
        sotw_submissionDao = getSubmissionDao();
        sotw_eventDao = getEventDao();

        exception(ApiError.class, (ex, req, res) -> {
            // Handle the exception here
            Map<String, String> map = Map.of("status", ex.getStatus() + "",
                    "error", ex.getMessage());
            res.body(gson.toJson(map));
            res.status(ex.getStatus());
            res.type("application/json");
        });

        // Set frontend and backend urls
        String frontend_url;
        String backend_url;
        if (isLocalTest) {
            frontend_url = "http://localhost:3000";
            backend_url = "http://localhost:4567";
        }
        else {
            frontend_url = "http://bandiscover.herokuapp.com";
            backend_url = "http://bandiscover-api.herokuapp.com";
        }
        // Set the redirect_uri from Spotify dialog
        final URI redirect_uri =
                SpotifyHttpManager.makeUri(backend_url + "/callback");
        // Spotify API variable
        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(client_id)
                .setClientSecret(client_secret)
                .setRedirectUri(redirect_uri)
                .build();
        // authorization code uri request
        final AuthorizationCodeUriRequest auth_code_uri_req =
                spotifyApi.authorizationCodeUri()
                        .scope("user-read-email,user-read-private,user-top-read")
                        .show_dialog(true)
                        .build();

        // Redirects the window to Spotify login
        get("/login", (req, res) -> {
            URI uri_for_code = auth_code_uri_req.execute();
            String uriString = uri_for_code.toString();
            res.redirect(uriString);
            return null;
            //return new JSONObject("{\"link\": \""+uriString+"\"}");
        });
        // Where Spotify redirects after login dialog
        get("/callback", (req, res) -> {

            String error = req.queryParams("error");
            if (error != null) { // SSO was canceled by user
                res.redirect(frontend_url);
                return null;
            }

            // Use authorization code to get access token and refresh token
            String code = req.queryParams("code");
            AuthorizationCodeRequest auth_code_req =
                    spotifyApi.authorizationCode(code).build();
            AuthorizationCodeCredentials auth_code_credentials =
                    auth_code_req.execute();

            // Set tokens in Spotify API Object
            spotifyApi.setAccessToken(auth_code_credentials.getAccessToken());
            spotifyApi.setRefreshToken(auth_code_credentials.getRefreshToken());

            // get current user's info
            final GetCurrentUsersProfileRequest getCurrentUser =
                    spotifyApi.getCurrentUsersProfile()
                    .build();
            final User user = getCurrentUser.execute();
            String name = user.getDisplayName();
            String id = user.getId();

            res.redirect(frontend_url + "/?id=" + id);

            // Create user in database if not already existent
            Musician musician = musicianDao.read(id);
            if (musician == null) { // user has not been added to database yet
                musicianDao.create(id, name);
            }

            return null;
            //return new JSONObject("{\"id\": \"" + id + "\"}");
        });

        // Musician routes
        get("/musicians/:id", MusicianController.getMusicianById);
        get("/musicians", MusicianController.getAllMusicians);
        post("/musicians", MusicianController.postMusician);
        put("/musicians/:id", MusicianController.putMusician);
        delete("/musicians/:id", MusicianController.deleteMusician);
        get("/adminstatus/:id", MusicianController.getAdminStatus);
        put("/adminstatus/:id", MusicianController.putAdminStatus);

        // Friend Request routes
        get("/friends/:id", RequestController.getMusicianFriends);
        get("/requests/in/:recipientid", RequestController.getIncomingPendingRequests);
        get("/requests/out/:senderid", RequestController.getOutgoingPendingRequests);
        post("/request/:senderid/:recipientid", RequestController.postFriendRequest);
        delete("request/:senderid/:recipientid/:action", RequestController.respondToRequest);

        // Band routes
        get("/bands", BandController.getAllBands);
        get("/bands/:id", BandController.getBandById);
        post("/bands", BandController.postBand);
        put("/bands/:bid/:mid", BandController.putBandMember);
        delete("/bands/:bid/:mid", BandController.deleteMember);

        // SpeedDateEvent routes
        get("/speeddateevents", SpeedDateController.getAllSpeedDateEvents);
        get("/speeddateevents/:id", SpeedDateController.getSpeedDateEventById);
        put("/speeddateevents/:eid/:mid", SpeedDateController.putSpeedDateEventParticipant);
        delete("/speeddateevents/:eid/:mid", SpeedDateController.deleteSpeedDateEventParticipant);
        post("/speeddateevents", SpeedDateController.postSpeedDateEvent);

        // Song routes
        get("/songs", SongController.getAllSongs);
        get("/songs/:songid", SongController.getSongById);
        post("/songs", SongController.postSong);
        put("/songs/:songid", SongController.putSong);
        delete("/songs/:songId", SongController.deleteSong);

        // SOTW Submission Api Routes
        get("/submissions", SOTWSubmissionController.getAllSubmissions);
        get("/submissions/:submissionid", SOTWSubmissionController.getSubmissionById);
        post("/submissions", SOTWSubmissionController.postSubmission);
        put("/submissions/:submissionid", SOTWSubmissionController.putSubmission);
        delete("/submissions/:submissionid", SOTWSubmissionController.deleteSubmission);

        // SOTW Event Routes
        get("/sotwevents", SOTWEventController.getAllSOTWEvents);
        get("/sotwevents/:eventid", SOTWEventController.getSOTWEventById);
        post("/sotwevents", SOTWEventController.postSOTWEvent);
        put("/sotwevents/:eventid", SOTWEventController.putSOTWEvent);
        get("/sotwevents/submissions/:eventid", SOTWEventController.getSOTWEventSubmissions);
        put("/sotwevents/submissions/:eventid/:submissionid", SOTWEventController.putSubmissionToSOTWEvent);
        delete("/sotwevents/submissions/:eventid/:submissionid", SOTWEventController.deleteSubmissionFromSOTWEvent);
        delete("/sotwevents/:eventid", SOTWEventController.deleteSOTWEvent);

        // options request to allow for CORS
        options("/*", (req, res) -> {
            String headers = req.headers("Access-Control-Request-Headers");
            if (headers != null) {
                res.header("Access-Control-Allow-Headers", headers);
            }
            String method = req.headers("Access-Control-Request-Method");
            if (method != null) {
                res.header("Access-Control-Allow-Methods", method);
            }
            return "OK";
        });

        // CORS
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            res.header("Access-Control-Request-Method", "*");
            res.header("Access-Control-Allow-Headers", "*");
            res.type("application/json");
        });
    }

    private static MusicianDao getMusicianDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        //List<Musician> musicians = DataStore.sampleMusicians();
        //Database.createMusicianTablesWithSampleData(sql2o, musicians);
        return new Sql2oMusicianDao(sql2o);
    }

    private static BandDao getBandDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oBandDao(sql2o);
    }

    private static Sql2oSpeedDateEventDao getSpeedDateEventDao() throws URISyntaxException {
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oSpeedDateEventDao(sql2o);
    }

    private static RequestDao getRequestDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oRequestDao(sql2o);
    }

    private static SongDao getSongDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oSongDao(sql2o);
    }

    private static SotwEventDao getEventDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oSotwEventDao(sql2o);
    }

    private static SotwSubmissionDao getSubmissionDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oSotwSubmissionDao(sql2o);
    }

    /**
     * Stop the server.
     */
    public static void stop() {
        Spark.stop();
    }
}
