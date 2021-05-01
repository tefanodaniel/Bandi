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
    @DisplayName("return musician friends")
    void getMusicianFriends() {

    }

    @Test
    @DisplayName("updating a musician works")
    void updateWorks() {
        String name = "New Name";
        Set<String> genre = new HashSet<String>(Arrays.asList("New Genre"));
        Musician m = musicianDao.updateName("00001fakeid", name);
        Musician m = musicianDao.updateGenres("00001fakeid", genre);

        // add more updates
        assertEquals("00001fakeid", m.getId());
        assertEquals(name, m.getName());

        // Reset back to original properties
        m = musicianDao.updateName("00001fakeid", "David Gilmour");
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