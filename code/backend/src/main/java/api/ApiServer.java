package api;

import dao.*;
import exceptions.ApiError;
import exceptions.DaoException;
import kong.unirest.json.JSONObject;
import model.Band;
import model.FriendRequest;
import spark.QueryParamsMap;
import util.Database;
import util.DataStore;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static spark.Spark.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import dao.MusicianDao;
import model.Musician;
import org.sql2o.Sql2o;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;


import javax.xml.crypto.Data;

public class ApiServer {

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
        MusicianDao musicianDao = getMusicianDao();
        BandDao bandDao = getBandDao();
        RequestDao requestDao = getRequestDao();

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
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

        // Redirects to Spotify login
        get("/login", (req, res) -> {
            URI uri_for_code = auth_code_uri_req.execute();
            String uriString = uri_for_code.toString();
            res.redirect(uriString);
            return null;
            //return new JSONObject("{\"link\": \""+uriString+"\"}");
        });

        // Gets user info following login
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

        // Get musicians given the id
        get("/musicians/:id", (req, res) -> {
            try {
                String id = req.params("id");
                Musician musician = musicianDao.read(id);
                if (musician == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(musician);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // Get all musicians (with optional query parameters)
        get("/musicians", (req, res) -> {
            try {
                List<Musician> musicians;
                Map<String, String[]> query = req.queryMap().toMap();
                if(query.size() > 0) {
                    musicians = musicianDao.readAll(query);
                }
                else {
                    musicians = musicianDao.readAll();
                }
                return gson.toJson(musicians);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post musicians
        post("/musicians", (req, res) -> {
            try {
                Musician musician = gson.fromJson(req.body(), Musician.class);
                //musicianDao.create(musician.getId(), musician.getName(), musician.getGenre());
                Set<String> instruments = musician.getInstruments();
                Set<String> genres = musician.getGenres();
                String experience = musician.getExperience();
                String location = musician.getLocation();
                Set<String> profileLinks = musician.getProfileLinks();
                Set<String> friends = musician.getFriends();
                boolean admin = musician.getAdmin();

                if (instruments == null) { instruments = new HashSet<String>(); }
                if (genres == null) { genres = new HashSet<String>(); }
                if (experience == null) { experience = "NULL"; }
                if (location == null) { location = "NULL"; }
                if (profileLinks == null) { profileLinks = new HashSet<String>(); }
                if (friends == null) { friends = new HashSet<String>(); }
                musicianDao.create(musician.getId(), musician.getName(), genres,
                        instruments, experience, location, profileLinks, friends, admin);
                res.status(201);
                return gson.toJson(musician);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put musicians
        put("/musicians/:id", (req, res) -> {

            try {

                String id = req.params("id");
                Musician musician = gson.fromJson(req.body(), Musician.class);
                if (musician == null) {
                    throw new ApiError("Resource not found", 404);
                }

                if (! (musician.getId()).equals(id)) {
                    throw new ApiError("musician ID does not match the resource identifier", 400);
                }

                String name = musician.getName();
                Set<String> genres = musician.getGenres();
                Set<String> instruments = musician.getInstruments();
                String experience = musician.getExperience();
                String location = musician.getLocation();
                Set<String> profileLinks = musician.getProfileLinks();
                // no check for admin flag. We don't want to change admin on and off,
                // and since ints default to 0, we might accidentally take admin
                // permissions away.

                // Update specific fields:
                boolean flag = false;
                if (name != null) {
                    flag = true;
                    musician = musicianDao.updateName(id, name);
                } if (instruments != null) {
                    flag = true;
                    musician = musicianDao.updateInstruments(id, instruments);
                } if (genres != null) {
                    flag = true;
                    musician = musicianDao.updateGenres(id, genres);
                } if (experience != null) {
                    flag = true;
                    musician = musicianDao.updateExperience(id, experience);
                } if (location != null) {
                    flag = true;
                    musician = musicianDao.updateLocation(id, location);
                } if (profileLinks != null) {
                    flag = true;
                    musician = musicianDao.updateProfileLinks(id, profileLinks);
                } if (!flag) {
                    throw new ApiError("Nothing to update", 400);
                } if (musician == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(musician);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // delete musicians
        delete("/musicians/:id", (req, res) -> {
            try {
                String id = req.params("id");
                Musician musician = musicianDao.delete(id);
                if (musician == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(musician);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // get all of user's friends
        get("/friends/:id", (req, res) -> {
            try {
                String id = req.params("id");
                List<Musician> musicians = musicianDao.getAllFriendsOf(id);
                res.type("application/json");
                return gson.toJson(musicians);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // get all of user's pending friend requests
        get("/request/:senderid", (req, res) -> {
            try {
                String senderID = req.params("senderid");
                List<FriendRequest> requests = requestDao.readAllFrom(senderID);
                res.type("application/json");
                return gson.toJson(requests);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // send friend request
        post("/request/:senderid/:recipientid", (req, res) -> {
            try {
                String senderID = req.params("senderid");
                String recipientID = req.params("recipientid");
                FriendRequest fr = requestDao.createRequest(senderID, recipientID);
                if (fr == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(fr);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // respond (accept or decline) friend request
        delete("request/:senderid/:recipientid/:action", (req, res) -> {
            try {
                String senderID = req.params("senderid");
                String recipientID = req.params("recipientid");
                String action = req.params("action");

                FriendRequest fr = null;

                if (action.equals("accept"))  {
                    fr = requestDao.acceptRequest(senderID, recipientID);
                } else if (action.equals("decline")) {
                    fr = requestDao.declineRequest(senderID, recipientID);
                } else {
                    throw new ApiError("Invalid action to perform on request", 505);
                }

                if (fr == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(fr);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // get the admin status of a musician
        get("/adminstatus/:id", (req, res) -> {
                    try {
                        String id = req.params("id");
                        Musician musician = musicianDao.read(id);
                        if (musician == null) {
                            throw new ApiError("Resource not found", 404); // Bad request
                        }
                        boolean isAdmin = musician.getAdmin();
                        res.type("application/json");
                        return "{\"isAdmin\":"+ isAdmin +"}";
                    } catch (DaoException ex) {
                        throw new ApiError(ex.getMessage(), 500);
                    }
                }
        );

        // update the admin status of a musician
        put("/adminstatus/:id", (req, res) -> {
                    try {
                        String id = req.params("id");
                        Map map = gson.fromJson(req.body(),HashMap.class);
                        boolean isAdmin = (boolean) map.get("isAdmin");
                        Musician m = musicianDao.updateAdmin(id, isAdmin);
                        res.type("application/json");
                        return gson.toJson(m);
                    } catch (DaoException ex) {
                        throw new ApiError(ex.getMessage(), 500);
                    }
                }
        );

        // Get all bands (optional query parameters)
        // if searching for id, only pass 1 parameter
        get("/bands", (req, res) -> {
            try {
                List<Band> bands;
                Map<String, String[]> query = req.queryMap().toMap();

                if (query.get("musicianId") != null) {
                    bands = bandDao.readAll(query.get("musicianId")[0]);
                } else if (query.size() > 0) {
                    bands = bandDao.readAll(query);
                } else {
                    bands = bandDao.readAll();
                }
                return gson.toJson(bands);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // Get band given the id
        get("/bands/:id", (req, res) -> {
            try {
                String id = req.params("id");
                Band band = bandDao.read(id);
                if (band == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(band);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post band
        post("/bands", (req, res) -> {
            try {
                Band band = gson.fromJson(req.body(), Band.class);

                String id = UUID.randomUUID().toString();
                Set<String> members = band.getMembers();
                Set<String> genres = band.getGenres();
                String name = band.getName();
                int capacity = band.getCapacity();

                if (members == null) { members = new HashSet<String>(); }
                if (genres == null) { genres = new HashSet<String>(); }
                if (name == null) { name = "NULL"; }
                if (capacity == 0) { capacity = 1; }

                bandDao.create(id, name, capacity, genres, members);

                res.status(201);
                return gson.toJson(band);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put new band members
        put("/bands/:bid/:mid", (req, res) -> {
            try {
                String bandId = req.params("bid");
                String musicianId = req.params("mid");
                Band band = bandDao.read(bandId);
                Musician musician = musicianDao.read(musicianId);
                if (band == null) {
                    throw new ApiError("Resource not found", 404);
                }
                if (musician == null) {
                    throw new ApiError("Resource not found", 404);
                }
                if (!(musician.getId()).equals(musicianId)) {
                    throw new ApiError("musician ID does not match the resource identifier", 400);
                }
                if (!(band.getId()).equals(bandId)) {
                    throw new ApiError("band ID does not match the resource identifier", 400);
                }

                Band b = bandDao.add(bandId, musicianId);
                if (b == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(b);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // remove
        delete("/bands/:bid/:mid", (req, res) -> {
            try {
                String bandId = req.params("bid");
                String musicianId = req.params("mid");
                Band band = bandDao.remove(bandId, musicianId);
                if (band == null) {
                    throw new ApiError("Resource not found", 404);
                }

                res.type("application/json");
                return gson.toJson(band);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // sent friend request from user to user


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
        return new Sql2oMusicianDao(sql2o);
    }

    private static BandDao getBandDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oBandDao(sql2o);
    }

    private static RequestDao getRequestDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oRequestDao(sql2o);
    }
}
