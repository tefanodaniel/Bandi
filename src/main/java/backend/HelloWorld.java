package backend;

import static spark.Spark.*;

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

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        get("/", (req, res) -> "Welcome to Group 10's project");
    }
}
