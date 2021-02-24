package backend;

import static spark.Spark.*;

public class HelloWorld {
    public static void main(String[] args) {
        get("/", (req, res) -> "Welcome to Group 10's project");
    }
}
