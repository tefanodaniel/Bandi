package api;

import static spark.Spark.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

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

    private static Connection getConnection() throws URISyntaxException, SQLException {
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

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void main(String[] args) throws SQLException {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        try (Connection conn = getConnection()) {
            // simply testing if I can connect to the database.

            String sql = "CREATE TABLE IF NOT EXISTS Musicians("
                    + "id INT PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "genre VARCHAR(30) NOT NULL"
                    + ");";
            Statement st = conn.createStatement();
            st.execute(sql);

        } catch (URISyntaxException | SQLException e) {
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
}