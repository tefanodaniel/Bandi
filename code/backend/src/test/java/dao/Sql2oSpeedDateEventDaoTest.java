package dao;

import model.FriendRequest;
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

    // url for the database to use
    private static String databaseUrl = "";
    private static Sql2o sql2o;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
        sql2o = new Sql2o(dbUrl, username, password);
    }

    @BeforeAll
    static void createSampleData() {
        Database.createMusicianTablesWithSampleData(sql2o, DataStore.sampleMusicians());
        Database.createBandTablesWithSampleData(sql2o, DataStore.sampleBands());
        Database.createSpeedDateEventsWithSampleData(sql2o, DataStore.sampleSpeedDateEvents());
        Database.createRequestTableWithSamples(sql2o, new ArrayList<FriendRequest>());
        Database.createSongTablesWithSampleData(sql2o, DataStore.sampleSongs());
        Database.createSotwSubmissionTablesWithSampleData(sql2o, DataStore.sampleSotwSubmissions());
        Database.createSotwEventTablesWithSampleData(sql2o, DataStore.sampleSotwEvents());
    }

    @BeforeEach
    void injectDependency() {

    }

    @Test
    @DisplayName("Test that...")
    void test() {
        assertTrue(true);
    }

}
