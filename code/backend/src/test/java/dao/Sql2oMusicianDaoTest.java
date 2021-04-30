package dao;

import dao.MusicianDao;
import dao.Sql2oMusicianDao;
import exceptions.ApiError;
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

    /*
    @BeforeAll
    static void setSampleMusicians() {
//        samples = new ArrayList<>();
//        samples.add(new Musician("fakeid1","Kenny G", "Jazz",
//                "saxophone", "expert", "WNY, NJ", "07093"));
//        samples.add(new Musician("fakeid2","Bruno Mars", "Pop",
//                "instru", "expert", "Edgewater, NJ", "07020"));
//        samples.add(new Musician("fakeid3","070 Shake", "Rap",
//                "vocals", "advanced", "North Bergen, NJ", "07047"));
//        samples.add(new Musician("fakeid4","Rihanna", "Pop",
//                "instru", "expert", "San Diego, CA", "92168"));

        // Create lists of genres for the test musicians
        Set<String> genres1 = new HashSet<String>(Arrays.asList("Progressive Rock", "Psychedelic Rock"));
        Set<String> genres2 = new HashSet<String>(Arrays.asList("Blues", "Rock", "Classic Rock"));
        Set<String> genres3 = new HashSet<String>(Arrays.asList("Jazz", "Rock"));

        // Create lists of instruments for the test musicians
        Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
        Set<String> instruments2 = new HashSet<String>(Arrays.asList("Guitar", "Vocals"));
        Set<String> instruments3 = new HashSet<String>(Arrays.asList("Saxophone"));
        Set<String> profileLinks = new HashSet<String>();

        // Create lists of friends for the test musicians
        Set<String> davidFriends = new HashSet<String>(Arrays.asList());
        Set<String> ericFriends = new HashSet<String>(Arrays.asList("00001fakeid"));
        Set<String> kennyFriends = new HashSet<String>(Arrays.asList("00001fakeid", "00002fakeid"));
        Set<String> rogerFriends = new HashSet<String>(Arrays.asList("00001fakeid", "00002fakeid", "00003fakeid"));
        Set<String> nickFriends = new HashSet<String>(Arrays.asList("00001fakeid", "00002fakeid", "00003fakeid", "00004fakeid"));

        samples = new ArrayList<>();
        samples.add(new Musician("00001fakeid","David Gilmour", genres1, instruments1, "Expert",
                profileLinks, "WNY, NJ", "07093", davidFriends, false));
        samples.add(new Musician("00002fakeid","Eric Clapton", genres2, instruments2, "Expert",
                profileLinks, "Edgewater, NJ", "07020", ericFriends,false));
        samples.add(new Musician("00003fakeid","Kenny G", genres3, instruments3, "Expert",
                profileLinks, "San Diego, CA", "92168",kennyFriends, false));
    }


    @BeforeEach
    void injectDependency() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("create extension if not exists cube;").executeUpdate();
            conn.createQuery("create extension if not exists earthdistance;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS MTest CASCADE;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS InstrTest;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS MGenresTest;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS ProfileAVLinksTest;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS MTest("
                    + "id VARCHAR(30) PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "experience VARCHAR(30),"
                    + "location VARCHAR(30),"
                    + "zipCode VARCHAR(10),"
                    + "latitude DOUBLE PRECISION,"
                    + "longitude DOUBLE PRECISION,"
                    + "distance DOUBLE PRECISION DEFAULT 9999.0"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS InstrTest("
                    + "id VARCHAR(30) REFERENCES MTest, " // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "instrument VARCHAR(50)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS MGenresTest("
                    + "id VARCHAR(30) REFERENCES MTest, " // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "genre VARCHAR(30)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS ProfileAVLinksTest("
                    + "id VARCHAR(30) REFERENCES MTest, " // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "link VARCHAR(100)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            String musician_sql = "INSERT INTO MTest(id, name, experience, location, " +
                    "zipCode, latitude, longitude, distance)" +
                    " VALUES(:id, :name, :experience, :location, :zipCode, " +
                    ":latitude, :longitude, :distance);";
            String instrument_sql = "INSERT INTO InstrTest(id, instrument) VALUES(:id, :instrument);";
            String genre_sql = "INSERT INTO MGenresTest(id, genre) VALUES(:id, :genre);";
            String link_sql = "INSERT INTO ProfileAVLinksTest(id, link) VALUES(:id, :link);";

            for (Musician m : samples) {
                // Allows for more Musician class attributes than there are columns:
                conn.createQuery(musician_sql).bind(m).executeUpdate();

                // Insert all instruments for this musician
                for (String instrument : m.getInstruments()) {
                    conn.createQuery(instrument_sql)
                            .addParameter("id", m.getId())
                            .addParameter("instrument", instrument)
                            .executeUpdate();
                }

                // Insert all genres for this musician
                for (String genre : m.getGenres()) {
                    conn.createQuery(genre_sql)
                            .addParameter("id", m.getId())
                            .addParameter("genre", genre)
                            .executeUpdate();
                }

                for (String link : m.getProfileLinks()) {
                    conn.createQuery(link_sql)
                            .addParameter("id", m.getId())
                            .addParameter("link", link)
                            .executeUpdate();
                }
            }
        }

        musicianDao = new Sql2oMusicianDao(sql2o);
    }
    */

    @Test
    void doNothing() {    }

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
    @DisplayName("read all the musicians that satisfy the search query by distance and genre")
    void readAllGivenSearchQuery() {
        String[] genre = new String[]{"blues"};
        String[] distance = new String[]{"5000"};
        String[] sourceID = new String[]{"00001fakeid"};
        Map<String, String[]> query = Map.of("genre", genre,"distance", distance, "id", sourceID);
        List<Musician> musicians = musicianDao.readAll(query);
        assertNotEquals(0, musicians.size());
        for (Musician musician : musicians) {
            assertTrue(musician.getDistance() <= Double.parseDouble("5000"));
            assertTrue(musician.getGenres().contains("Blues"));
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