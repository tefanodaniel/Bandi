package api;

import dao.*;
import exceptions.ApiError;
import spark.Spark;
import util.Database;
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
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

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
    private static final boolean isLocalTest = Boolean.parseBoolean(System.getenv("isLocal"));
    // frontend and backend urls
    public static String frontend_url;
    public static String backend_url;

    // client id for Spotify
    private static final String client_id= "75bf47e1a9ab493b9910401e6dad93af";
    // Client Secret for using Spotify API (should never push to VCS)
    private static final String client_secret = System.getenv("client_secret");
    // Redirect uri after Spotify dialog
    public static URI redirect_uri;
    // Spotify API variable
    public static SpotifyApi spotifyApi;
    // Spotify authorization code uri request
    public static AuthorizationCodeUriRequest auth_code_uri_req;

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

        // Initialize Gson object
        gson = new GsonBuilder().disableHtmlEscaping().create();

        // Initialize Dao objects
        musicianDao = getMusicianDao();
        bandDao = getBandDao();
        speedDateEventDao = getSpeedDateEventDao();
        requestDao = getRequestDao();
        songDao = getSongDao();
        sotw_submissionDao = getSubmissionDao();
        sotw_eventDao = getEventDao();

        // Store frontend and backend urls
        if (isLocalTest) {
            frontend_url = "http://localhost:3000";
            backend_url = "http://localhost:4567";
        }
        else {
            frontend_url = "http://bandiscover.herokuapp.com";
            backend_url = "http://bandiscover-api.herokuapp.com";
        }

        // Initialize Spotify variables
        redirect_uri =
                SpotifyHttpManager.makeUri(backend_url + "/callback");

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(client_id)
                .setClientSecret(client_secret)
                .setRedirectUri(redirect_uri)
                .build();

        auth_code_uri_req =
                spotifyApi.authorizationCodeUri()
                        .scope("user-read-email,user-read-private,user-top-read")
                        .show_dialog(true)
                        .build();

        // Exception for API Error
        exception(ApiError.class, (ex, req, res) -> {
            // Handle the exception here
            Map<String, String> map = Map.of("status", ex.getStatus() + "",
                    "error", ex.getMessage());
            res.body(gson.toJson(map));
            res.status(ex.getStatus());
            res.type("application/json");
        });

        // Spotify / Login API routes
        get("/login", SpotifyController.loginWithSpotify);
        get("/callback", SpotifyController.callbackFromSpotify);

        // Musician routes
        get("/musicians/:id", MusicianController.getMusicianById);
        get("/musicians", MusicianController.getAllMusicians);
        post("/musicians", MusicianController.postMusician);
        put("/musicians/:id", MusicianController.putMusician);
        delete("/musicians/:id", MusicianController.deleteMusician);
        get("/adminstatus/:id", MusicianController.getAdminStatus);
        put("/adminstatus/:id", MusicianController.putAdminStatus);
        get("/showtoptracks/:id", MusicianController.getShowTopTracks);
        put("/showtoptracks/:id", MusicianController.putShowTopTracks);

        get("/friends/:id", RequestController.getMusicianFriends);
        // (Friend, Band) request routes
        get("/requests/:type/in/:recipientid", RequestController.getIncomingPendingRequests);
        get("/requests/:type/out/:senderid", RequestController.getOutgoingPendingRequests);
        post("/request/:type/:senderid/:recipientid", RequestController.postRequest);
        delete("request/:type/:senderid/:recipientid/:action", RequestController.respondToRequest);

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
        delete("/speeddateevents/:id", SpeedDateController.deleteSpeedDateEvent);

        // Song routes
        get("/songs", SongController.getAllSongs);
        get("/songs/:songid", SongController.getSongById);
        post("/songs/generate/:genre", SongController.generateSongByGenre);
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
        get("/sotwevents/desc/:genre/:startday/:endday", SOTWEventController.findSOTWEventByDesc);
        post("/sotwevents", SOTWEventController.postSOTWEvent);
        put("/sotwevents/:eventid", SOTWEventController.putSOTWEvent);
        get("/sotwevents/submissions/:eventid", SOTWEventController.getSOTWEventSubmissions);
        put("/sotwevents/submissions/:eventid/:submissionid", SOTWEventController.putSubmissionToSOTWEvent);
        delete("/sotwevents/submissions/:eventid/:submissionid", SOTWEventController.deleteSubmissionFromSOTWEvent);
        delete("/sotwevents/:eventid", SOTWEventController.deleteSOTWEvent);

        // options route for CORS
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

        // Set header for CORS
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
