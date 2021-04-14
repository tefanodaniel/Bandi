package dao;

import model.FriendRequest;
import model.Musician;
import model.SpeedDateEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import util.DataStore;
import util.Database;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class Sql2oSpeedDateEventDaoTest {

    // url for the test database to use
    private static Sql2o sql2o;
    private static Sql2oSpeedDateEventDao speedDateEventDao;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {

        String databaseUrl = System.getenv("TEST_DATABASE_URL");

        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
        sql2o = new Sql2o(dbUrl, username, password);

        speedDateEventDao = new Sql2oSpeedDateEventDao(sql2o);
    }

    @BeforeAll
    static void createSampleData() {
        /*
        Database.createMusicianTablesWithSampleData(sql2o, DataStore.sampleMusicians());
        Database.createBandTablesWithSampleData(sql2o, DataStore.sampleBands());
        Database.createSpeedDateEventsWithSampleData(sql2o, DataStore.sampleSpeedDateEvents());
        Database.createRequestTableWithSamples(sql2o, new ArrayList<FriendRequest>());
        Database.createSongTablesWithSampleData(sql2o, DataStore.sampleSongs());
        Database.createSotwSubmissionTablesWithSampleData(sql2o, DataStore.sampleSotwSubmissions());
        Database.createSotwEventTablesWithSampleData(sql2o, DataStore.sampleSotwEvents());
         */
    }

    @Test
    @DisplayName("Test: read all SpeedDateEvents")
    void testReadAllSDEventsWorks() {
        List<SpeedDateEvent> SDEvents = speedDateEventDao.readAll();
        assertNotEquals(0, SDEvents.size());
    }

    @Test
    @DisplayName("Test: read SpeedDateEvent")
    void testReadSDEventWorks() {
        // ids are randomly generated, so be sure to set manually
        String id = "7edbe0e7-8daf-4e73-8918-95766bd87183";
        SpeedDateEvent SDEvent = speedDateEventDao.read(id);
        assertEquals("Speed-Dating Part 1", SDEvent.getName());
    }

    @Test
    @DisplayName("Test: Create SpeedDateEvent")
    void testCreateSDEventWorks() {
        String id = UUID.randomUUID().toString();
        SpeedDateEvent SDEvent = speedDateEventDao.create(id, "Test Event", "",
                "", 4, new HashSet<String>());
        assertEquals("Test Event", SDEvent.getName());
        speedDateEventDao.delete(id);
    }

}