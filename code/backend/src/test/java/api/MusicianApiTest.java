package api;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import model.Musician;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MusicianApiTest {
/*
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

    @Test
    public void getMusiciansWorks() throws UnirestException {
        final String URL = BASE_URL + "/musicians";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    public void getMusicianByID() throws UnirestException {
        // ids are randomly generated, so set manually
        String id = "00001fakeid";
        final String URL = BASE_URL + "/musicians/" + id;
        HttpResponse<JsonNode> jsonResponse = Unirest.get(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    public void putMusicianWorks() throws UnirestException {
        Set<String> genres1 = new HashSet<String>(Arrays.asList("Progressive Rock", "Psychedelic Rock"));
        Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
        Set<String> profileLinks = new HashSet<String>();
        Set<String> davidFriends = new HashSet<String>(Arrays.asList("00002fakeid", "22zcnk76clvox7mifcwgz3tha", "thegreatbelow1"));
        Musician m = new Musician("00001fakeid","David G", genres1, instruments1, "Expert",
                profileLinks, "WNY, NJ", "07093", davidFriends, false);

        String id = "00001fakeid";
        final String URL = BASE_URL + "/musicians/" + id;
        HttpResponse<JsonNode> jsonResponse = Unirest.put(URL)
                .body(gson.toJson(m)).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());

        // reset to default
        Musician m2 = new Musician("00001fakeid","David Gilmour", genres1, instruments1, "Expert",
                profileLinks, "WNY, NJ", "07093", davidFriends, false);
        HttpResponse<JsonNode> jsonResponse2 = Unirest.put(URL)
                .body(gson.toJson(m2)).asJson();
        assertEquals(200, jsonResponse2.getStatus());
        assertNotEquals(0, jsonResponse2.getBody().getArray().length());
    }
*/
}