package dao;

import exceptions.DaoException;
import model.SongOfTheWeekEvent;
import model.SongOfTheWeekSubmission;
import model.Song;
import org.junit.jupiter.api.*;
import org.sql2o.Sql2o;
import util.DataStore;
import util.Database;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static api.ApiServer.sotw_eventDao;
import static api.ApiServer.sotw_submissionDao;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Sql2oSotwEventDaoTest {

    // url for the test database to use
    private static Sql2o sql2o;
    private Sql2oSotwEventDao sotwEventDao;
    private Sql2oSotwSubmissionDao sotwSubmissionDao;
    private static List<SongOfTheWeekEvent> sample_sotw_events;
    private static List<SongOfTheWeekSubmission> sample_sotw_event_submissions;


    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        sql2o = Database.getSql2o();
    }

    @BeforeAll
    static void createSampleData() {
        sample_sotw_events = DataStore.sampleSotwEvents();
    }

    @BeforeEach
    void injectDependency(){
        Database.createSotwEventTablesWithSampleData(sql2o, sample_sotw_events);
        sotwEventDao = new Sql2oSotwEventDao(sql2o);
        sotwSubmissionDao = new Sql2oSotwSubmissionDao(sql2o);
    }

    /**
     * Tests for dao.Sql2oSotwEventDao.create() method without submissions
     */
    @Test
    @Order(1)
    @DisplayName("create Sotw Event works for valid input")
    void createNewSotwEvent() {
        System.out.println("Test 1");
        Set<String> submissions = new HashSet<String>(Arrays.asList("00008fakesubmissionid", "00002fakesubmissionid", "00001fakesubmissionid", "00004fakesubmissionid"));

        String startday = "April 4, 2021";
        String endday = "April 10, 2021";
        String adminid = "22zcnk76clvox7mifcwgz3tha";
        String songid = "00001fakesongid";
        String genre = "pop";
        SongOfTheWeekEvent e1 = new SongOfTheWeekEvent("99999fakeeventid", adminid, startday, endday, songid, genre);
        SongOfTheWeekEvent e2 = sotwEventDao.create(e1.getEventId(), e1.getAdminId(), e1.getStartDay(), e1.getEndDay(), e1.getSongId(), e1.getGenre());
        assertEquals(e1, e2);
        SongOfTheWeekEvent e3 = new SongOfTheWeekEvent("88888fakeeventid", adminid, startday, endday, songid, genre, submissions);
        SongOfTheWeekEvent e4 = sotwEventDao.create(e3.getEventId(), e3.getAdminId(), e3.getStartDay(), e3.getEndDay(), e3.getSongId(), e3.getGenre(), e3.getSubmissions());
        assertEquals(e3, e4);
    }

    @Test
    @Order(2)
    @DisplayName("create Sotw Event throws exception for incomplete data")
    void createSotwEventIncompleteData() {
        System.out.println("Test 2");
        assertThrows(DaoException.class, () -> {
            String startday = "April 4, 2021";
            String endday = "April 10, 2021";
            String adminid = "22zcnk76clvox7mifcwgz3tha";
            String songid = "00001fakesongid";
            String genre = "pop";
            SongOfTheWeekEvent e1 = new SongOfTheWeekEvent("99999fakeeventid", adminid, startday, endday, songid, genre);
            sotwEventDao.create(null, null, e1.getStartDay(), e1.getEndDay(), e1.getSongId(), e1.getGenre());
        });
    }

    @Test
    @Order(3)
    @DisplayName("create Sotw Event throws exception for duplicate event")
    void createSotwEventDuplicateData() {
        System.out.println("Test 3");
        assertThrows(DaoException.class, () -> {
            Set<String> submissions1 = new HashSet<String>(Arrays.asList("00001fakesubmissionid", "00002fakesubmissionid", "00003fakesubmissionid", "00004fakesubmissionid"));
            String startday1 = "April 4, 2021";
            String endday1 = "April 10, 2021";
            String admin1 = "22zcnk76clvox7mifcwgz3tha";
            String songid1 = "00001fakesongid";
            String genre1 = "pop";
            SongOfTheWeekEvent e = sotwEventDao.create("00001fakeeventid", admin1, startday1, endday1, songid1, genre1, submissions1);
        });
    }

    /**
     * Tests for dao.Sql2oSotwEventDao.read() method
     */
    @Test
    @Order(4)
    @DisplayName("read a Sotw Event given its id")
    void readSotwEventGivenId() {
        System.out.println("Test 4");
        for (SongOfTheWeekEvent e1 : sample_sotw_events) {
            SongOfTheWeekEvent e2 = sotwEventDao.read(e1.getEventId());
            assertEquals(e1, e2);
        }
    }


    @Test
    @Order(5)
    @DisplayName("read Sotw Event returns null given invalid Id")
    void readSotwEventGivenInvalidId() {
        System.out.println("Test 5");
        SongOfTheWeekEvent e = sotwEventDao.read("88888fakeeventid");
        assertNull(e);
    }

    /**
     * Tests for dao.Sql2oSotwEventDao.readAll() method
     */
    @Test
    @Order(6)
    @DisplayName("read all Sotw Events")
    void readAllSotwEvents() {
        HashSet<SongOfTheWeekEvent> sample_Set = new HashSet<SongOfTheWeekEvent>();
        sample_Set.addAll(sample_sotw_events);

        List<SongOfTheWeekEvent> read_events = sotwEventDao.readAll();
        HashSet<SongOfTheWeekEvent> read_Set = new HashSet<SongOfTheWeekEvent>();
        read_Set.addAll(read_events);
        assertEquals(sample_Set.size(), read_Set.size());
        assertTrue(sample_Set.containsAll(read_Set));
    }

    /**
     * Tests for dao.Sql2oSongDao.findEvent() method
     */
    @Test
    @Order(7)
    @DisplayName("find a Sotw Event given genre and week")
    void findEventGivenGenreAndWeek() {
        System.out.println("Test 7");
        for (SongOfTheWeekEvent e1 : sample_sotw_events) {
            SongOfTheWeekEvent e2 = sotwEventDao.findEvent(e1.getStartDay(), e1.getEndDay(), e1.getGenre());
            assertEquals(e1, e2);
        }
    }


    @Test
    @Order(8)
    @DisplayName("find a Sotw Event returns null given invalid/missing parameters")
    void findSotwEventGivenInvalidParams() {
        System.out.println("Test 7");
        String startDay = "April 4, 2021";
        String endDay = "April 10, 2021";
        String genre = "pop";

        //check for missing parameters
        SongOfTheWeekEvent e1 = sotwEventDao.findEvent(null, endDay, genre);
        assertNull(e1);
        SongOfTheWeekEvent e2 = sotwEventDao.findEvent(startDay, null, genre);
        assertNull(e2);
        SongOfTheWeekEvent e3 = sotwEventDao.findEvent(startDay, endDay, null);
        assertNull(e3);

        // check for invalid parameters
        SongOfTheWeekEvent e4 = sotwEventDao.findEvent("April 0, 2021", endDay, genre);
        assertNull(e4);
        SongOfTheWeekEvent e5 = sotwEventDao.findEvent(startDay, "September 6, 2021", genre);
        assertNull(e5);
        SongOfTheWeekEvent e6 = sotwEventDao.findEvent(startDay, endDay, "electric-boogalo");
        assertNull(e6);

    }

    /**
     * Tests for dao.Sql2oSotwEventDao.updateStartDay() method
     */
    @Test
    @Order(9)
    @DisplayName("updating a Sotw Event start day works")
    void updateSotwEventStartDayWorks() {
        String new_day = "Test Day!";
        SongOfTheWeekEvent e = sotwEventDao.updateStartDay(sample_sotw_events.get(0).getEventId(), new_day);
        assertEquals(new_day, e.getStartDay());
        assertEquals(sample_sotw_events.get(0).getEventId(), e.getEventId());
    }

    @Test
    @Order(10)
    @DisplayName("Update Sotw Event Start Day Name throws exception for an invalid/missing day")
    void updateSotwEventStartDayInvalid() {
        assertThrows(DaoException.class, () -> {
            sotwEventDao.updateStartDay(sample_sotw_events.get(0).getEventId(), null);
        });
    }

    /**
     * Tests for dao.Sql2oSotwEventDao.updateEndDay() method
     */
    @Test
    @Order(11)
    @DisplayName("updating a Sotw Event end day works")
    void updateSotwEventEndDayWorks() {
        String new_day = "Test Day!";
        SongOfTheWeekEvent e = sotwEventDao.updateEndDay(sample_sotw_events.get(0).getEventId(), new_day);
        assertEquals(new_day, e.getEndDay());
        assertEquals(sample_sotw_events.get(0).getEventId(), e.getEventId());
    }

    @Test
    @Order(12)
    @DisplayName("Update Sotw Event End Day Name throws exception for an invalid/missing day")
    void updateSotwEventEndDayInvalid() {
        assertThrows(DaoException.class, () -> {
            sotwEventDao.updateEndDay(sample_sotw_events.get(0).getEventId(), null);
        });
    }

    /**
     * Tests for dao.Sql2oSotwEventDao.updateSong() method
     */
    @Test
    @Order(13)
    @DisplayName("updating a Sotw Event song works")
    void updateSotwEventSongWorks() {
        String songid = "Test id!";
        SongOfTheWeekEvent e = sotwEventDao.updateSong(sample_sotw_events.get(0).getEventId(), songid);
        assertEquals(songid, e.getSongId());
        assertEquals(sample_sotw_events.get(0).getEventId(), e.getEventId());
    }

    @Test
    @Order(14)
    @DisplayName("Update Sotw Event song throws exception for an invalid/missing id")
    void updateSotwEventSongInvalid() {
        assertThrows(DaoException.class, () -> {
            sotwEventDao.updateSong(sample_sotw_events.get(0).getEventId(), null);
        });
    }


    /**
     * The doNothing Test to invoke before all tests
     */

    @Test
    void doNothing() {}
}