package dao;

import exceptions.DaoException;
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

public class Sql2oSotwSubmissionDaoTest {

    // url for the test database to use
    private static Sql2o sql2o;
    private static Sql2oSotwSubmissionDao sotwSubmissionDao;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        String databaseUrl = System.getenv("TEST_DATABASE_URL");
        URI dbUri = new URI(databaseUrl);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
        sql2o = new Sql2o(dbUrl, username, password);
        sotwSubmissionDao = new Sql2oSotwSubmissionDao(sql2o);
    }

    @BeforeAll
    static void createSampleData() {
        // Careful resetting database if other people are using it
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

}