package dao;

import exceptions.DaoException;
import model.Song;
import org.junit.jupiter.api.*;
import org.sql2o.Sql2o;
import util.DataStore;
import util.Database;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static api.ApiServer.songDao;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Sql2oSongDaoTest {

    // url for the test database to use
    private static Sql2o sql2o;
    private SongDao songDao;
    private static List<Song> sample_songs;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        //String databaseUrl = System.getenv("TEST_DATABASE_URL");
        //URI dbUri = new URI(databaseUrl);
        //String username = dbUri.getUserInfo().split(":")[0];
        //String password = dbUri.getUserInfo().split(":")[1];
        //String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
        //        + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
        //sql2o = new Sql2o(dbUrl, username, password);
        //songDao = new Sql2oSongDao(sql2o);
        sql2o = Database.getSql2o();
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
        sample_songs = DataStore.sampleSongs();
    }

    @BeforeEach
    void injectDependency(){
        Database.createSongTablesWithSampleData(sql2o, sample_songs);
        songDao = new Sql2oSongDao(sql2o);
    }

    /**
     * Tests for dao.Sql2oSongDao.create() method
     */
    @Test
    @Order(1)
    @DisplayName("create Song works for valid input")
    void createNewSong() {
        System.out.println("Test 1");
        Set<String> genres = new HashSet<>();
        genres.add("Pop");
        genres.add("Rock");
        Song s1 = new Song("99999fakesongid", "Never Gonna Give You Up", "Rick Astley", "null", 1987, genres);
        Song s2 = songDao.create(s1.getSongId(), s1.getSongName(), s1.getArtistName(), s1.getAlbumName(), s1.getReleaseYear(), s1.getGenres());
        assertEquals(s1, s2);
    }

    @Test
    @Order(2)
    @DisplayName("create Song throws exception for incomplete data")
    void createSongIncompleteData() {
        System.out.println("Test 2");
        assertThrows(DaoException.class, () -> {
            Set<String> genres = new HashSet<>();
            genres.add("Pop");
            genres.add("Rock");
            Song s1 = new Song("99999fakesongid", "Never Gonna Give You Up", "Rick Astley", "null", 1987, genres);
            songDao.create(null, null, s1.getArtistName(), s1.getAlbumName(), s1.getReleaseYear(), s1.getGenres());
            //courseDao.create(null, null);
        });
    }

}