package dao;

import exceptions.DaoException;
import model.Song;
import model.SongOfTheWeekEvent;
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
//
//    // url for the test database to use
//    private static Sql2o sql2o;
//    private SotwSubmissionDao sotwSubmissionDao;
//    private static List<SongOfTheWeekSubmission> sample_sotw_submissions;
//
//    @BeforeAll
//    static void connectToDatabase() throws URISyntaxException {
//        sql2o = Database.getSql2o();
//    }
//
//    @BeforeAll
//    static void createSampleData() {
//        sample_sotw_submissions = DataStore.sampleSotwSubmissions();
//    }
//
//    @BeforeEach
//    void injectDependency(){
//        Database.createSotwSubmissionTablesWithSampleData(sql2o, sample_sotw_submissions);
//        sotwSubmissionDao = new Sql2oSotwSubmissionDao(sql2o);
//    }
//    /**
//     * Tests for dao.Sql2oSotwSubmissionDao.create() method
//     */
//    @Test
//    @Order(1)
//    @DisplayName("create Sotw Submission works for valid input")
//    void createNewSotwSubmission() {
//        Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
//        String avLink = "https://youtu.be/dQw4w9WgXcQ";
//        SongOfTheWeekSubmission s1 = new SongOfTheWeekSubmission("99999fakesubmissionid","00001fakeid", "David Gilmour", avLink, instruments1);
//        SongOfTheWeekSubmission s2 = sotwSubmissionDao.create(s1.getSubmission_id(), s1.getMusician_id(), s1.getMusician_name(), s1.getAVSubmission(), s1.getInstruments());
//        assertEquals(s1, s2);
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("create Sotw Submission throws exception for incomplete data")
//    void createSotwSubmissionIncompleteData() {
//        assertThrows(DaoException.class, () -> {
//            Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
//            String avLink = "https://youtu.be/dQw4w9WgXcQ";
//            SongOfTheWeekSubmission s1 = new SongOfTheWeekSubmission("99999fakesubmissionid","00001fakeid", "David Gilmour", avLink, instruments1);
//            sotwSubmissionDao.create(null, s1.getMusician_id(), s1.getMusician_name(), s1.getAVSubmission(), s1.getInstruments());
//            //courseDao.create(null, null);
//        });
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("create Song throws exception for duplicate song")
//    void createSotwSubmissionDuplicateData() {
//        assertThrows(DaoException.class, () -> {
//            Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
//            String avLink = "https://youtu.be/dQw4w9WgXcQ";
//            SongOfTheWeekSubmission s1 = new SongOfTheWeekSubmission("00001fakesubmissionid","00001fakeid", "David Gilmour", avLink, instruments1);
//            sotwSubmissionDao.create(s1.getSubmission_id(), s1.getMusician_id(), s1.getMusician_name(), s1.getAVSubmission(), s1.getInstruments());
//        });
//    }
//
//    /**
//     * Tests for dao.Sql2oSotwSubmissionDao.read() method
//     */
//    @Test
//    @Order(4)
//    @DisplayName("read a Sotw Submission given its id")
//    void readSotwSubmissionGivenId() {
//        for (SongOfTheWeekSubmission s1 : sample_sotw_submissions) {
//            SongOfTheWeekSubmission s2 = sotwSubmissionDao.read(s1.getSubmission_id());
//            assertEquals(s1, s2);
//        }
//    }
//
//
//    @Test
//    @Order(5)
//    @DisplayName("read Sotw Submission returns null given invalid Id")
//    void readSotwSubmissionGivenInvalidId() {
//        System.out.println("Test 5");
//        SongOfTheWeekSubmission s = sotwSubmissionDao.read("88888fakesongid");
//        assertNull(s);
//    }
//
//    /**
//     * Tests for dao.Sql2oSotwSubmissionDao.readAll() method
//     */
//    @Test
//    @Order(6)
//    @DisplayName("read all the sotw submissions")
//    void readAllSotwSubmissions() {
//        HashSet<SongOfTheWeekSubmission> sample_Set = new HashSet<SongOfTheWeekSubmission>();
//        sample_Set.addAll(sample_sotw_submissions);
//
//        List<SongOfTheWeekSubmission> read_submissions = sotwSubmissionDao.readAll();
//        HashSet<SongOfTheWeekSubmission> read_Set = new HashSet<SongOfTheWeekSubmission>();
//        read_Set.addAll(read_submissions);
//        assertEquals(sample_Set.size(), read_Set.size());
//        assertTrue(sample_Set.containsAll(read_Set));
//    }
//
//    /**
//     * Tests for dao.Sql2oSotwSubmissionDao.updateAVSubmission() method
//     */
//    @Test
//    @Order(7)
//    @DisplayName("updating a Sotw Submission AV Link works")
//    void updateSotwSubmissionAVLinkWorks() {
//        String avlink = "new link";
//        SongOfTheWeekSubmission s = sotwSubmissionDao.updateAVSubmission(sample_sotw_submissions.get(0).getSubmission_id(), avlink);
//        assertEquals(avlink, s.getAVSubmission());
//        assertEquals(sample_sotw_submissions.get(0).getSubmission_id(), s.getSubmission_id());
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("Update Sotw Submission AV Link throws exception for an invalid/missing link")
//    void updateSotwSubmissionAVLinkInvalid() {
//        assertThrows(DaoException.class, () -> {
//            sotwSubmissionDao.updateAVSubmission(sample_sotw_submissions.get(0).getSubmission_id(), null);
//        });
//    }
//
//    /**
//     * Tests for dao.Sql2oSotwSubmissionDao.updateInstruments() method
//     */
//    @Test
//    @Order(9)
//    @DisplayName("updating a Sotw Submission Instruments works")
//    void updateSotwSubmissionInstrumentsWorks() {
//        Set<String> instruments4 = new HashSet<String>(Arrays.asList("Drums"));
//        SongOfTheWeekSubmission s = sotwSubmissionDao.updateInstruments(sample_sotw_submissions.get(0).getSubmission_id(), instruments4);
//        assertEquals(instruments4, s.getInstruments());
//        assertEquals(sample_sotw_submissions.get(0).getSubmission_id(), s.getSubmission_id());
//    }
//
//    @Test
//    @Order(10)
//    @DisplayName("Update Sotw Submission Instruments throws exception for an invalid/missing instruments")
//    void updateSotwSubmissionInstrumentsInvalid() {
//        assertThrows(DaoException.class, () -> {
//            sotwSubmissionDao.updateInstruments(sample_sotw_submissions.get(0).getSubmission_id(), null);
//        });
//    }
//
//    /**
//     * Tests for dao.Sql2oSotwSubmissionDao.delete() method
//     */
//    @Test
//    @Order(11)
//    @DisplayName("create and delete a dummy sotw submission")
//    void deleteSotwSubmissionsWorks() {
//        // create a dummy sotw submission
//        Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
//        String avLink = "https://youtu.be/dQw4w9WgXcQ";
//        SongOfTheWeekSubmission s1 = new SongOfTheWeekSubmission("82345fakesubmissionid","00001fakeid", "David Gilmour", avLink, instruments1);
//        SongOfTheWeekSubmission s = sotwSubmissionDao.create(s1.getSubmission_id(), s1.getMusician_id(), s1.getMusician_name(), s1.getAVSubmission(), s1.getInstruments());
//        // ensure that it is in the database
//        SongOfTheWeekSubmission s2 = sotwSubmissionDao.read(s.getSubmission_id());
//        assertEquals(s, s2) ;
//        // delete and retrieve dummy sotw submission
//        SongOfTheWeekSubmission s3 = sotwSubmissionDao.delete(s.getSubmission_id());
//        // check that the returned event was the dummy sotw submission
//        assertEquals(s, s3);
//        // check that the dummy sotw submission was in fact deleted
//        SongOfTheWeekSubmission s4 = sotwSubmissionDao.read(s.getSubmission_id());
//        assertNull(s4);
//    }
//
//    @Test
//    @Order(12)
//    @DisplayName("delete returns null for an invalid submission Id")
//    void deleteReturnsNullInvalidSubmissionId() {
//        SongOfTheWeekSubmission s  = sotwSubmissionDao.delete("92134fakesubmissionid");
//        assertNull(s);
//    }
//

}