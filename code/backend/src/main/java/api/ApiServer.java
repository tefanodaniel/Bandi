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

        // Song of the Week Event Api Routes
        // get (read) all sotw events
        get("/sotwevents", (req, res) -> {
            try {
                List<SongOfTheWeekEvent> events = sotw_eventDao.readAll();
                return gson.toJson(events);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // get (read) a sotw event given event id
        get("/sotwevents/:eventid", (req, res) -> {
            try {
                String eventid = req.params("eventid");
                SongOfTheWeekEvent s = sotw_eventDao.read(eventid);
                if (s == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(s);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post (create) a sotw event
        post("/sotwevents", (req, res) -> {
            try {
                SongOfTheWeekEvent event = gson.fromJson(req.body(), SongOfTheWeekEvent.class);
                Set<String> submissions = event.getSubmissions();

                if (submissions == null) { submissions = new HashSet<String>(); }
                sotw_eventDao.create(event.getEventId(), event.getAdminId(), event.getStart_week(), event.getEnd_week(), event.getSongId(), submissions);
                res.status(201);
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put (update) an sotw event
        put("/sotwevents/:eventid", (req, res) -> {
            // doesn't include adding or removing submissions
            try {

                String eventid = req.params("eventid");
                SongOfTheWeekEvent event = gson.fromJson(req.body(), SongOfTheWeekEvent.class);
                if (event == null) {
                    throw new ApiError("Resource not found", 404);
                }

                if (! (event.getEventId().equals(eventid))) {
                    throw new ApiError("event ID does not match the resource identifier", 400);
                }

                String start_week = event.getStart_week();
                String end_week = event.getEnd_week();
                String songId = event.getSongId();

                // Update specific fields:
                boolean flag = false;
                if (start_week != null) {
                    flag = true;
                    event = sotw_eventDao.updateStartWeek(eventid, start_week);
                } if (end_week != null) {
                    flag = true;
                    event = sotw_eventDao.updateEndWeek(eventid, end_week);
                } if (songId != null) {
                    flag = true;
                    event = sotw_eventDao.updateSong(eventid, songId);
                } if (!flag) {
                    throw new ApiError("Nothing to update", 400);
                } if (event == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(event);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });


        // get (read All) submissions given sotw event id
        get("/sotwevents/submissions/:eventid", (req, res) -> {
            try {
                String eventid = req.params("eventid");
                Set<String> submission_ids = sotw_eventDao.readAllSubmissionsGivenEvent(eventid);
                List<SongOfTheWeekSubmission> submissions = new ArrayList<SongOfTheWeekSubmission>();
                for (String sid : submission_ids) {
                    submissions.add(sotw_submissionDao.read(sid));
                }
                return gson.toJson(submissions);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });


        // put (add) a submission to an sotw events
        put("/sotwevents/submissions/:eventid/:submissionid", (req, res) -> {
            try {
                String eventid = req.params("eventid");
                String submissionid = req.params("submissionid");
                SongOfTheWeekEvent event = sotw_eventDao.read(eventid);
                SongOfTheWeekSubmission submission = sotw_submissionDao.read(submissionid);
                if (event == null){
                    throw new ApiError("Event Resource not found", 404);
                }
                if (submission == null) {
                    throw new ApiError("Submission Resource not found", 404);
                }

                /** is this needed?
                if (!event.getEventId().equals(eventId)) {
                    throw new ApiError("Event ID does not match the resource identifier", 400);
                }

                if (!submission.getSubmission_id().equals(submissionId)) {
                    throw new ApiError("Submission ID does not match the resource identifier", 400);
                }*/

                event = sotw_eventDao.addSubmissionToEvent(eventid, submissionid);
                if (event == null) {
                    throw new ApiError("Updated Event resource not found", 404);
                }
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // delete (remove) a submission from an sotw event
        delete("/sotwevents/submissions/:eventid/:submissionid", (req, res) -> {
            try {
                String eventid = req.params("eventid");
                String submissionid = req.params("submissionid");
                SongOfTheWeekEvent event = sotw_eventDao.read(eventid);
                SongOfTheWeekSubmission submission = sotw_submissionDao.read(submissionid);
                if (event == null){
                    throw new ApiError("Event Resource not found", 404);
                }
                if (submission == null) {
                    throw new ApiError("Submission Resource not found", 404);
                }

                event = sotw_eventDao.removeSubmissionFromEvent(eventid, submissionid);
                if (event == null) {
                    throw new ApiError("Updated Event Resource not found", 404);
                }

                res.type("application/json");
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // delete a sotw event
        delete("/sotwevents/:eventid", (req, res) -> {
            try {
                String eventid = req.params("eventid");
                SongOfTheWeekEvent event = sotw_eventDao.deleteEvent(eventid);
                if (event == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

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
