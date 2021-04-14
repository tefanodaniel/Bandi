package api;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import model.SpeedDateEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SpeedDateEventApiTest {

    private final static String BASE_URL = "http://localhost:4567";
    private static final Gson gson = new Gson();

    @BeforeAll
    static void runApiServer() throws URISyntaxException {
        ApiServer.main(null); // run the server
    }

    @AfterAll
    static void stopApiServer() {
        ApiServer.stop();
    }

    @Test
    @DisplayName("Test: get all SDEvents")
    public void getAllSDEventsWorks() throws UnirestException {
        final String URL = BASE_URL + "/speeddateevents";
        HttpResponse<JsonNode> jsonResponse = Unirest.get(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    @DisplayName("Test: get SDEvent by id")
    public void getSDEventWorks() throws UnirestException {
        // ids are randomly generated, so set manually
        String eventId = "7edbe0e7-8daf-4e73-8918-95766bd87183";
        final String URL = BASE_URL + "/speeddateevents/" + eventId;
        HttpResponse<JsonNode> jsonResponse = Unirest.get(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    @DisplayName("Test: Put participant in SDEvent")
    public void putParticipantWorks() throws UnirestException {
        // event ids are randomly generated, so set manually
        String eventId = "7edbe0e7-8daf-4e73-8918-95766bd87183";
        String musId = "22zcnk76clvox7mifcwgz3tha";
        final String URL = BASE_URL + "/speeddateevents/" + eventId + "/" + musId;
        HttpResponse<JsonNode> jsonResponse = Unirest.put(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    @DisplayName("Test: Delete participant from SDEvent")
    public void deleteParticipantWorks() throws UnirestException {
        // event ids are randomly generated, so set manually
        String eventId = "7edbe0e7-8daf-4e73-8918-95766bd87183";
        String musId = "22zcnk76clvox7mifcwgz3tha";
        final String URL = BASE_URL + "/speeddateevents/" + eventId + "/" + musId;
        Unirest.put(URL).asJson();

        HttpResponse<JsonNode> jsonResponse = Unirest.delete(URL).asJson();
        assertEquals(200, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());
    }

    @Test
    @DisplayName("Test: Post and Delete SDEvent")
    public void postDeleteSDEventWorks() throws UnirestException {
        // event ids are randomly generated, so set manually
        String eventId = UUID.randomUUID().toString();
        SpeedDateEvent SDEvent = new SpeedDateEvent(eventId, "Posted Event!");

        final String URL = BASE_URL + "/speeddateevents";
        HttpResponse<JsonNode> jsonResponse = Unirest.post(URL)
                .body(gson.toJson(SDEvent))
                .asJson();
        assertEquals(201, jsonResponse.getStatus());
        assertNotEquals(0, jsonResponse.getBody().getArray().length());

        final String deleteURL = BASE_URL + "/speeddateevents/" + eventId;
        HttpResponse<JsonNode> deleteResponse = Unirest.delete(deleteURL).asJson();
        assertNotEquals(0, deleteResponse.getBody().getArray().length());
    }

}
