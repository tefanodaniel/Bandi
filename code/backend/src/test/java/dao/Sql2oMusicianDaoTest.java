package dao;

import dao.MusicianDao;
import dao.Sql2oMusicianDao;
import exceptions.ApiError;
import exceptions.DaoException;
import model.Musician;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oMusicianDaoTest {
    private static Sql2o sql2o;
    private static List<Musician> samples;
    private static MusicianDao musicianDao;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        String databaseUrl = System.getenv("TEST_DATABASE_URL");
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        sql2o = new Sql2o(dbUrl, username, password);

        musicianDao = new Sql2oMusicianDao(sql2o);
    }

    @Test
    @DisplayName("create musician for valid input")
    void createAndDeleteNewMusician() {
        // Testing create
        Set<String> genres = new HashSet<String>(Arrays.asList("Pop"));
        Set<String> instruments = new HashSet<String>(Arrays.asList("Vocals"));
        Set<String> profileLinks = new HashSet<String>();
        Set<String> gagaFriends = new HashSet<String>(Arrays.asList("00001fakeid"));
        Musician m1 = new Musician("010101fakeid","Lady Gaga", genres, instruments,
                "Expert", profileLinks, "Honolulu, HI", "96816", gagaFriends, false);
        Musician m2 = musicianDao.create(m1.getId(), m1.getName(), m1.getGenres(), m1.getInstruments(),
                m1.getExperience(), m1.getLocation(), m1.getZipCode(), m1.getProfileLinks(), m1.getFriends(), m1.getAdmin());
        assertEquals(m1, m2);

        // Testing delete
        Musician m3 = musicianDao.delete(m1.getId());
        assertEquals(m1, m3);
    }

    @Test
    @DisplayName("read musician given ID")
    void readMusicianByID() {
        // Test assumes musician with id = 00004fakeid exists in the database
        Set<String> genres = new HashSet<String>(Arrays.asList("Progressive Rock", "Psychedelic Rock"));
        Set<String> instruments = new HashSet<String>(Arrays.asList("Guitar", "Vocals"));

        Musician m = musicianDao.read("00004fakeid");
        assertEquals("Roger Waters", m.getName());
        assertEquals(genres, m.getGenres());
        assertEquals(instruments, m.getInstruments());
        assertEquals("Expert", m.getExperience());
        assertEquals("Grand Prairie, TX", m.getLocation());
    }

    @Test
    void readAllMusicians() {
        List<Musician> musicians = musicianDao.readAll();
        assertNotEquals(0, musicians.size());
    }

    @Test
    @DisplayName("updating a musician works")
    void updateWorks() {
        String name = "New Name";
        Set<String> genre = new HashSet<String>(Arrays.asList("New Genre"));
        Set<String> instruments = new HashSet<String>(Arrays.asList("New Instrument"));
        Set<String> proflinks = new HashSet<String>(Arrays.asList("New Link"));
        Set<String> toptracks = new HashSet<String>(Arrays.asList("New Track"));
        Musician m = musicianDao.updateName("00001fakeid", name);
        m = musicianDao.updateGenres("00001fakeid", genre);
        m = musicianDao.updateInstruments("00001fakeid", instruments);
        m = musicianDao.updateExperience("00001fakeid", "New Exp");
        m = musicianDao.updateLocation("00001fakeid", "New Location");
        m = musicianDao.updateZipCode("00001fakeid", "21218");
        m = musicianDao.updateAdmin("00001fakeid", true);
        m = musicianDao.updateProfileLinks("00001fakeid", proflinks);
        m = musicianDao.updateTopTracks("00001fakeid", toptracks);
        m = musicianDao.updateShowtoptracks("00001fakeid", true);

        // add more updates
        assertEquals("00001fakeid", m.getId());
        assertEquals(name, m.getName());
        assertEquals(genre, m.getGenres());
        assertEquals(instruments, m.getInstruments());
        assertEquals(proflinks, m.getProfileLinks());
        assertEquals(toptracks, m.getTopTracks());
        assertEquals("New Exp", m.getExperience());
        assertEquals("New Location", m.getLocation());
        assertEquals("21218", m.getZipCode());
        assertEquals(true, m.getAdmin());
        assertEquals(true, m.getShowtoptracks());

        // Reset back to original properties
        Set<String> genres1 = new HashSet<String>(Arrays.asList("Progressive Rock", "Psychedelic Rock"));
        Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
        Set<String> profileLinks1 = new HashSet<String>();
        Set<String> toptracks1 = new HashSet<String>();
        m = musicianDao.updateName("00001fakeid", "David Gilmour");
        m = musicianDao.updateGenres("00001fakeid", genres1);
        m = musicianDao.updateInstruments("00001fakeid", instruments1);
        m = musicianDao.updateExperience("00001fakeid", "Expert");
        m = musicianDao.updateLocation("00001fakeid", "WNY, NJ");
        m = musicianDao.updateZipCode("00001fakeid", "07093");
        m = musicianDao.updateAdmin("00001fakeid", false);
        m = musicianDao.updateProfileLinks("00001fakeid", profileLinks1);
        m = musicianDao.updateTopTracks("00001fakeid", toptracks1);
        m = musicianDao.updateShowtoptracks("00001fakeid", false);
    }

    @Test
    @DisplayName("Update returns null for an invalid musician id")
    void updateReturnsNullInvalidCode() {
        String name = "New Name";
        Musician m = musicianDao.updateName("112111fakeid", name);
        assertNull(m);
        // add more updates
    }

    @Test
    @DisplayName("read all the musicians that satisfy the search query by distance and multiple genres")
    void readAllGivenSearchQuery() {
        String[] genre = new String[]{"blues", "rock"};
        String[] distance = new String[]{"5000"};
        String[] sourceID = new String[]{"00001fakeid"};
        Map<String, String[]> query = Map.of("genre", genre,"distance", distance, "id", sourceID);
        List<Musician> musicians = musicianDao.readAll(query);
        assertNotEquals(0, musicians.size());
        for (Musician musician : musicians) {
            assertTrue(musician.getDistance() <= Double.parseDouble("5000"));
            assertTrue(musician.getGenres().contains("Blues"));
            assertTrue(musician.getGenres().contains("Rock"));
        }
    }

    @Test
    @DisplayName("read all the musicians that satisfy the search query")
    void readAllGivenSearchQueryDistanceOnly() {
        String[] distance = new String[]{"5000"};
        String[] sourceID = new String[]{"00001fakeid"};
        Map<String, String[]> query = Map.of("distance", distance, "id", sourceID);
        List<Musician> musicians = musicianDao.readAll(query);
        assertNotEquals(0, musicians.size());
        for (Musician musician : musicians) {
            assertTrue(musician.getDistance() <= Double.parseDouble("5000"));
        }
    }

    @Test
    public void testAPIErrorExceptionNoSourceID() {
        String[] distance = new String[]{"5000"};
        Map<String, String[]> query = Map.of("distance", distance);
        assertThrows(ApiError.class, ()->musicianDao.readAll(query));
    }


}