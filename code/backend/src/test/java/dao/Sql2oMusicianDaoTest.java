package dao;

import dao.MusicianDao;
import dao.Sql2oMusicianDao;
import model.Musician;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oMusicianDaoTest {
    private static Sql2o sql2o;
    private static List<Musician> samples;
    private MusicianDao musicianDao;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        String databaseUrl = System.getenv("TEST_DATABASE_URL");
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        sql2o = new Sql2o(dbUrl, username, password);
    }

    @BeforeAll
    static void setSampleCourses() {
        samples = new ArrayList<>();
        samples.add(new Musician("fakeid1","Kenny G", "Jazz",
                "saxophone", "expert", "WNY, NJ", "07093"));
        samples.add(new Musician("fakeid2","Bruno Mars", "Pop",
                "instru", "expert", "Edgewater, NJ", "07020"));
        samples.add(new Musician("fakeid3","070 Shake", "Rap",
                "vocals", "advanced", "North Bergen, NJ", "07047"));
        samples.add(new Musician("fakeid4","Rihanna", "Pop",
                "instru", "expert", "San Diego, CA", "92168"));
    }


    @BeforeEach
    void injectDependency() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("create extension if not exists cube;").executeUpdate();
            conn.createQuery("create extension if not exists earthdistance;").executeUpdate();

            conn.createQuery("DROP TABLE IF EXISTS MusiciansTest;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS MusiciansTest("
                    + "id VARCHAR(30) PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "genre VARCHAR(30) NOT NULL,"
                    + "instrument VARCHAR(50),"
                    + "experience VARCHAR(30),"
                    + "location VARCHAR(30),"
                    + "zipCode VARCHAR(10),"
                    + "latitude DOUBLE PRECISION,"
                    + "longitude DOUBLE PRECISION"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "INSERT INTO MusiciansTest(id, name, genre, instrument, experience, " +
                    "location, zipCode, latitude, longitude) " +
                    "VALUES(:id, :name, :genre, :instrument, :experience, " +
                    ":location, :zipCode, :latitude, :longitude);";
            for (Musician Musician : samples) {
                conn.createQuery(sql).bind(Musician).executeUpdate();
            }
        }

        musicianDao = new Sql2oMusicianDao(sql2o);
    }


    @Test
    void doNothing() {    }

    @Test
    @DisplayName("create musician for valid input")
    void createNewCourse() {
        Musician m1 = new Musician("fakeid5","Lady Gaga", "Pop", "vocals",
                "Expert", "Honolulu, HI", "96816");
        Musician m2 = musicianDao.create(m1.getId(), m1.getName(), m1.getGenre(), m1.getInstrument(),
                m1.getExperience(), m1.getLocation(), m1.getZipCode(), m1.getLatitude(), m1.getLongitude());
        assertEquals(m1, m2);
    }

    @Test
    @DisplayName("read all the musicians that satisfy the search query")
    void readAllGivenSearchQuery() {
        String[] genre = new String[]{"pop"};
        String[] distance = new String[]{"20"};
        String[] sourceID = new String[]{"fakeid1"};
        Map<String, String[]> query = Map.of("genre", genre, "distance", distance, "id", sourceID);
        List<Musician> musicians = musicianDao.readAll(query);
        assertNotEquals(0, musicians.size());
        for (Musician musician : musicians) {
            assertTrue(musician.getDistance() <= Double.parseDouble("20"));
            assertTrue(musician.getGenre().equals("Pop"));
        }
    }

    @Test
    @DisplayName("filter by distance of 20 miles")
    void distanceFilterTest() {
        String miles = "20";
        List<Musician> musicians = musicianDao.filterByDist("fakeid1", miles);
        assertNotEquals(0, musicians.size());
        for (Musician musician : musicians) {
            assertTrue(musician.getDistance() <= Double.parseDouble(miles));
        }
    }
}