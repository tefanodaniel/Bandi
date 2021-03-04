package api;

import dao.MusicianDao;

import static spark.Spark.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    private static Connection getConnection() throws URISyntaxException, Sql2oException {
        // converting heroku database_url -> jdbc uri
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            throw new URISyntaxException(databaseUrl, "DATABASE_URL is not set");
        }

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        Sql2o sql2o = new Sql2o(dbUrl, username, password);
        return sql2o.open();
    }

    public static void main(String[] args) throws URISyntaxException{
        port(getHerokuAssignedPort());
        staticFiles.location("/public");
        MusicianDao musicianDao = getMusicianDao();

        try (Connection conn = getConnection()) {
            // simply testing if I can connect to the database.

            String sql = "CREATE TABLE IF NOT EXISTS Musicians("
                    + "id INT PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "genre VARCHAR(30) NOT NULL"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            // sql = "INSERT INTO Musicians(id, name, genre) VALUES(1, 'Frank Ocean', 'r&b');";
            // conn.createQuery(sql).executeUpdate();

        } catch (URISyntaxException | Sql2oException e) {
            e.printStackTrace();
        }

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
    }

    private static MusicianDao getMusicianDao() throws URISyntaxException{
        String databaseUrl = System.getenv("DATABASE_URL");

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        Sql2o sql2o = new Sql2o(dbUrl, username, password);
        return new Sql2oMusicianDao(sql2o);
    }
}