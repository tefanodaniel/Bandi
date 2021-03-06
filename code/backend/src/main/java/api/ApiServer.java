package api;

import dao.MusicianDao;
import exceptions.ApiError;
import exceptions.DaoException;
import util.Database;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static spark.Spark.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.MusicianDao;
import dao.Sql2oMusicianDao;
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
    private static final boolean isLocalTest = false;

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
            String email = user.getEmail();
            String id = user.getId();

            res.redirect(frontend_url + "/?name="
                    + name + "&email=" + email + "&id=" + id);

            // Create user in database if not already existent
            Musician musician = musicianDao.read(id);
            if (musician == null) { // user has not been added to database yet
                musicianDao.create(id, name, "unknown genre");
            }

            return null;
            //return new JSONObject("{\"name\": \""+name+"\",\"email\":\""+email+"\"}");
        });

        // post musicians
        post("/musicians", (req, res) -> {
            try {
                Musician musician = gson.fromJson(req.body(), Musician.class);
                //musicianDao.create(musician.getId(), musician.getName(), musician.getGenre());
                String instrument = musician.getInstrument();
                String experience = musician.getExperience();
                String location = musician.getLocation();
                if (instrument == null) { instrument = "NULL"; }
                if (experience == null) { experience = "NULL"; }
                if (location == null) { location = "NULL"; }

                musicianDao.create(musician.getId(), musician.getName(), musician.getGenre(),
                        instrument, experience, location);

                res.status(201);
                return gson.toJson(musician);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

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

                // Update specific fields:
                boolean flag = false;
                if (musician.getName() != null) {
                    flag = true;
                    musician = musicianDao.updateName(musician.getId(), musician.getName());
                } if (musician.getInstrument() != null) {
                    flag = true;
                    musician = musicianDao.updateInstrument(musician.getId(), musician.getInstrument());
                } if (musician.getGenre() != null) {
                    flag = true;
                    musician = musicianDao.updateGenre(musician.getId(), musician.getGenre());
                } if (musician.getExperience() != null) {
                    flag = true;
                    musician = musicianDao.updateExperience(musician.getId(), musician.getExperience());
                } if (musician.getLocation() != null) {
                    flag = true;
                    musician = musicianDao.updateLocation(musician.getId(), musician.getLocation());
                } if (!flag) {
                    throw new ApiError("Nothing to update", 400);
                }

                if (musician == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(musician);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        after((req, res) -> res.type("application/json"));
    }

    private static MusicianDao getMusicianDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
//        Musician musician = new Musician("1", "sample name", "sample genre");
//        List<Musician> musicians = new ArrayList<>();
//        musicians.add(musician);
//        Database.createMusiciansTableWithSampleData(sql2o, musicians);
        return new Sql2oMusicianDao(sql2o);
    }
}
