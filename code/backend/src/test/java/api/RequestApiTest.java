package api;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.*;

class RequestApiTest {

    private final static String BASE_URL = "http://localhost:4567";
    private static final Gson gson = new Gson();

    @BeforeAll
    static void runApiServer() throws URISyntaxException {
        ApiServer.main(null);
    }

    @AfterAll
    static void stopApiServer() {
        ApiServer.stop();
    }

}