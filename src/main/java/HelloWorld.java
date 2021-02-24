import static spark.Spark.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class HelloWorld {
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
    }
}