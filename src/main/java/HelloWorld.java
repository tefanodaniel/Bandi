import static spark.Spark.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class HelloWorld {

    // uri for redirecting purposes, either heroku or localhost
    static String uri;

    private static int getHerokuAssignedPort() {
        // Heroku stores port number as an environment variable
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            uri = "https://group10-oose.herokuapp.com";
            return Integer.parseInt(herokuPort);
        }
        //return default port if heroku-port isn't set (i.e. on localhost)
        uri = "http://localhost:4567";
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

        // Client Secret for using Spotify API (should never be stored to GitHub)
        final String SECRET = System.getenv("client_secret");

        try (Connection conn = getConnection()) {
            // simply testing if I can connect to the database.

            String sql = "CREATE TABLE IF NOT EXISTS artists("
                    + "name VARCHAR(30) NOT NULL PRIMARY KEY,"
                    + "instrument VARCHAR(30) NOT NULL"
                    + ");";
            Statement st = conn.createStatement();
            st.execute(sql);

            //sql = "INSERT INTO artists (name, instrument)"
            //        + "VALUES ('Kenny G', 'Sexy Saxophone');";
            //st.execute(sql);

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }

        get("/", (req, res) -> {
            return new ModelAndView(null, "index.hbs");
        }, new HandlebarsTemplateEngine());

        // Spotify GET
        get("/login", (req, res) -> {
            String scopes = "user-read-private%20user-read-email";
            res.redirect("https://accounts.spotify.com/authorize"
                + "?response_type=code" + "&client_id=f0bfba57fdbc4e6fadba79b09f419f5b"
                    + "&scope=" + scopes + "&redirect_uri=" + uri + "/profile"
                + "&show_dialog=true");
            return null;
        });

        // Go to Profile page
        get("/profile", (req, res) -> {
            String code = req.queryParams("code");
            return new ModelAndView(null, "profile.hbs");
        }, new HandlebarsTemplateEngine());

        // Spotify POST
        post("/profile", (req, res) -> {
            String endpoint = "https://accounts.spotify.com/api/token";
            String params = "?grant_type=authorization_code" +
                    "&code=" + "?" + "&redirect_uri=" + uri + "/profile";
            // Header parameter Authorization must be in the form:
            // "Authorization: Basic *<base64 encoded client_id:client_secret>*"
            return null;
        });
    }
}