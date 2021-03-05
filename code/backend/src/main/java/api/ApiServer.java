package api;

import com.google.gson.JsonSyntaxException;
import dao.MusicianDao;
import exceptions.ApiError;
import exceptions.DaoException;
import util.Database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static spark.Spark.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import dao.MusicianDao;
import dao.Sql2oMusicianDao;
import model.Musician;
import org.sql2o.Sql2o;
import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import org.apache.hc.core5.http.ParseException;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class ApiServer {
    // client id for Spotify
    private static final String client_id= "ae87181e126a4fd9ac434b67cf6f6f14";
    // Client Secret for using Spotify API (should never be stored to GitHub)
    private static final String client_secret = System.getenv("client_secret");
    // redirect_uri
    private static final URI redirect_uri =
            SpotifyHttpManager.makeUri("http://localhost:4567/profile");
    private static String code = "";
    private static AuthorizationCodeRequest auth_code_req;
    private static AuthorizationCodeCredentials auth_code_creds;

    // Spotify API variable
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(client_id)
            .setClientSecret(client_secret)
            .setRedirectUri(redirect_uri)
            .build();

    // uri request
    private static final AuthorizationCodeUriRequest auth_code_uri_req =
            spotifyApi.authorizationCodeUri()
                    .scope("user-read-email,user-read-private,user-top-read")
                    .show_dialog(true)
                    .build();

    private static int getHerokuAssignedPort() {
        // Heroku stores port number as an environment variable
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        //return default port if heroku-port isn't set (i.e. on localhost)
        return 4567;
    }

    public static void main(String[] args) throws URISyntaxException{
        port(getHerokuAssignedPort());
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

        // index.hbs
        get("/", (req, res) -> {
            URI uri_for_code = auth_code_uri_req.execute();
            res.redirect(uri_for_code.toString());

            return new ModelAndView(null, "index.hbs");
        }, new HandlebarsTemplateEngine());

        // profile.hbs
        get("/profile", (req, res) -> {
            code = req.queryParams("code");
            auth_code_req = spotifyApi.authorizationCode(code).build();
            auth_code_creds = auth_code_req.execute();

            spotifyApi.setAccessToken(auth_code_creds.getAccessToken());
            spotifyApi.setRefreshToken(auth_code_creds.getRefreshToken());

            GetUsersTopArtistsRequest getUsersTopArtistsRequest =
                    spotifyApi.getUsersTopArtists()
                    .limit(10)
                    .offset(0)
                    .time_range("medium_term")
                    .build();
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            Artist [] artists = artistPaging.getItems();
            for (Artist artist : artists) {
                System.out.println(artist.getName());
            }

            return new ModelAndView(null, "profile.hbs");
        }, new HandlebarsTemplateEngine());

        post("/musicians", (req, res) -> {
            try {
                Musician musician = gson.fromJson(req.body(), Musician.class);
                musicianDao.create(musician.getId(), musician.getName(), musician.getGenre());
                res.status(201);
                return gson.toJson(musician);
            } catch (DaoException ex) {
                // eventually, we want to process the error messages and make custom messages
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

                if (musician.getId() != Integer.parseInt(id)) {
                    throw new ApiError("musician ID does not match the resource identifier", 400);
                }

                /** Update specific fields */
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
                } if (flag==false) {
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
        return new Sql2oMusicianDao(sql2o);
    }
}