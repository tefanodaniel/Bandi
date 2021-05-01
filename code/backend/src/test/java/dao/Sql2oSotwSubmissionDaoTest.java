package dao;

import exceptions.DaoException;
import model.Song;
import model.SongOfTheWeekSubmission;
import org.junit.jupiter.api.*;
import org.sql2o.Sql2o;
import util.DataStore;
import util.Database;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Sql2oSotwSubmissionDaoTest {

    // url for the test database to use
    private static Sql2o sql2o;
    private SotwSubmissionDao sotwSubmissionDao;
    private static List<SongOfTheWeekSubmission> sample_sotw_submissions;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        sql2o = Database.getSql2o();
    }

    @BeforeAll
    static void createSampleData() {
        sample_sotw_submissions = DataStore.sampleSotwSubmissions();
    }

    @BeforeEach
    void injectDependency(){
        Database.createSotwSubmissionTablesWithSampleData(sql2o, sample_sotw_submissions);
        sotwSubmissionDao = new Sql2oSotwSubmissionDao(sql2o);
    }
    /**
     * Tests for dao.Sql2oSotwSubmissionDao.create() method
     */
    @Test
    @Order(1)
    @DisplayName("create Sotw Submission works for valid input")
    void createNewSong() {
        System.out.println("Test 1");
        Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
        String avLink = "https://youtu.be/dQw4w9WgXcQ";
        SongOfTheWeekSubmission s1 = new SongOfTheWeekSubmission("99999fakesubmissionid","00001fakeid", "David Gilmour", avLink, instruments1);
        SongOfTheWeekSubmission s2 = sotwSubmissionDao.create(s1.getSubmission_id(), s1.getMusician_id(), s1.getMusician_name(), s1.getAVSubmission(), s1.getInstruments());
        assertEquals(s1, s2);
    }

    @Test
    @Order(2)
    @DisplayName("create Sotw Submission throws exception for incomplete data")
    void createSongIncompleteData() {
        System.out.println("Test 2");
        assertThrows(DaoException.class, () -> {
            System.out.println("Test 1");
            Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
            String avLink = "https://youtu.be/dQw4w9WgXcQ";
            SongOfTheWeekSubmission s1 = new SongOfTheWeekSubmission("99999fakesubmissionid","00001fakeid", "David Gilmour", avLink, instruments1);
            sotwSubmissionDao.create(null, s1.getMusician_id(), s1.getMusician_name(), s1.getAVSubmission(), s1.getInstruments());
            //courseDao.create(null, null);
        });
    }

    @Test
    @Order(3)
    @DisplayName("create Song throws exception for duplicate song")
    void createSongDuplicateData() {
        System.out.println("Test 3");
        assertThrows(DaoException.class, () -> {
            System.out.println("Test 1");
            Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
            String avLink = "https://youtu.be/dQw4w9WgXcQ";
            SongOfTheWeekSubmission s1 = new SongOfTheWeekSubmission("00001fakesubmissionid","00001fakeid", "David Gilmour", avLink, instruments1);
            sotwSubmissionDao.create(s1.getSubmission_id(), s1.getMusician_id(), s1.getMusician_name(), s1.getAVSubmission(), s1.getInstruments());
        });
    }
}