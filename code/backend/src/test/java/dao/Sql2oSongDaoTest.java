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
        sql2o = Database.getSql2o();
    }

    @BeforeAll
    static void createSampleData() {
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

    @Test
    @Order(3)
    @DisplayName("create Song throws exception for duplicate song")
    void createSongDuplicateData() {
        System.out.println("Test 3");
        assertThrows(DaoException.class, () -> {
            Set<String> g1 = new HashSet<>();
            g1.add("Pop");
            g1.add("R&B");
            Song s1 = new Song("00001fakesongid", "Peaches", "Justin Bieber", "null", 2021, g1);
            Song s2 = songDao.create(s1.getSongId(), s1.getSongName(), s1.getArtistName(), s1.getAlbumName(), s1.getReleaseYear(), s1.getGenres());
        });
    }

    /**
     * Tests for dao.Sql2oSongDao.read() method
     */
    @Test
    @Order(4)
    @DisplayName("read a Song given its id")
    void readSongGivenId() {
        System.out.println("Test 4");
        for (Song s1 : sample_songs) {
            Song s2 = songDao.read(s1.getSongId());
            assertEquals(s1, s2);
        }
    }


    @Test
    @Order(5)
    @DisplayName("read Song returns null given invalid Id")
    void readSongGivenInvalidId() {
        System.out.println("Test 5");
        Song s = songDao.read("88888fakesongid");
        assertNull(s);
    }

    /**
     * Tests for dao.Sql2oSongDao.readGivenName() method
     */
    @Test
    @Order(6)
    @DisplayName("read a Song given its Name")
    void readSongGivenName() {
        System.out.println("Test 6");
        for (Song s1 : sample_songs) {
            Song s2 = songDao.readGivenName(s1.getSongName());
            assertEquals(s1, s2);
        }
    }


    @Test
    @Order(7)
    @DisplayName("read Song returns null given invalid Name")
    void readSongGivenInvalidName() {
        System.out.println("Test 7");
        Song s = songDao.readGivenName("Never Gonna Give You Up");
        assertNull(s);
    }

    /**
     * Tests for dao.Sql2oSongDao.readAll() method
     */
    @Test
    @Order(8)
    @DisplayName("read all the songs")
    void readAllSongs() {
        HashSet<Song> sample_Set = new HashSet<Song>();
        sample_Set.addAll(sample_songs);

        List<Song> read_songs = songDao.readAll();
        HashSet<Song> read_Set = new HashSet<Song>();
        read_Set.addAll(read_songs);
        assertEquals(sample_Set.size(), read_Set.size());
        assertTrue(sample_Set.containsAll(read_Set));
    }

    /**
     * Tests for dao.Sql2oSongDao.updateSongName() method
     */
    @Test
    @Order(9)
    @DisplayName("updating a Song name works")
    void updateSongNameWorks() {
        String name = "Updated Title!";
        Song s = songDao.updateSongName(sample_songs.get(0).getSongId(), name);
        assertEquals(name, s.getSongName());
        assertEquals(sample_songs.get(0).getSongId(), s.getSongId());
    }

    @Test
    @Order(10)
    @DisplayName("Update Song Name throws exception for an invalid/missing name")
    void updateSongNameInvalidName() {
        assertThrows(DaoException.class, () -> {
            songDao.updateSongName(sample_songs.get(0).getSongId(), null);
        });
    }

    /**
     * Tests for dao.Sql2oSongDao.updateArtistName() method
     */
    @Test
    @Order(11)
    @DisplayName("updating a Song Artist name works")
    void updateSongArtistNameWorks() {
        String name = "Updated Title2!";
        Song s = songDao.updateArtistName(sample_songs.get(0).getSongId(), name);
        assertEquals(name, s.getArtistName());
        assertEquals(sample_songs.get(0).getSongId(), s.getSongId());
    }

    @Test
    @Order(12)
    @DisplayName("Update Song Artist Name throws exception for an invalid/missing name")
    void updateSongArtistNameInvalidName() {
        assertThrows(DaoException.class, () -> {
            songDao.updateArtistName(sample_songs.get(0).getSongId(), null);
        });
    }

    /**
     * Tests for dao.Sql2oSongDao.updateAlbumName() method
     */
    @Test
    @Order(13)
    @DisplayName("updating a Song Album name works")
    void updateSongAlbumNameWorks() {
        String name = "Updated Title2!";
        Song s = songDao.updateAlbumName(sample_songs.get(0).getSongId(), name);
        assertEquals(name, s.getAlbumName());
        assertEquals(sample_songs.get(0).getSongId(), s.getSongId());
    }

    @Test
    @Order(14)
    @DisplayName("Update Song Album Name throws exception for an invalid/missing name")
    void updateSongAlbumNameInvalidName() {
        assertThrows(DaoException.class, () -> {
            songDao.updateAlbumName(sample_songs.get(0).getSongId(), null);
        });
    }

    /**
     * Tests for dao.Sql2oSongDao.updateReleaseYear() method
     */
    @Test
    @Order(15)
    @DisplayName("updating a Song ReleaseYear works")
    void updateSongReleaseYearWorks() {
        Integer year = 9999;
        Song s = songDao.updateReleaseYear(sample_songs.get(0).getSongId(), year);
        assertEquals(year, s.getReleaseYear());
        assertEquals(sample_songs.get(0).getSongId(), s.getSongId());
    }

    @Test
    @Order(16)
    @DisplayName("Update Song ReleaseYear throws exception for an invalid/missing year")
    void updateSongReleaseYearInvalidYear() {
        assertThrows(DaoException.class, () -> {
            songDao.updateReleaseYear(sample_songs.get(0).getSongId(), null);
        });
    }

    /**
     * Tests for dao.Sql2oSongDao.updateGenres() method
     */
    @Test
    @Order(17)
    @DisplayName("updating a Song Genres works")
    void updateSongGenresWorks() {
        Set<String> g2 = new HashSet<>();
        g2.add("Hip Hop");
        Song s = songDao.updateGenres(sample_songs.get(0).getSongId(), g2);
        assertEquals(g2, s.getGenres());
        assertEquals(sample_songs.get(0).getSongId(), s.getSongId());
    }

    @Test
    @Order(18)
    @DisplayName("Update Song Genres throws exception for an invalid/missing genres set")
    void updateSongGenresInvalidSet() {
        assertThrows(DaoException.class, () -> {
            songDao.updateGenres(sample_songs.get(0).getSongId(), null);
        });
    }

    /**
     * Tests for dao.Sql2oSongDao.delete() method
     */
    @Test
    @Order(19)
    @DisplayName("create and delete a dummy song")
    void deleteWorksReadSongGivenDeletedId() {
        // create a dummy song
        Set<String> genres = new HashSet<>();
        genres.add("Pop");
        genres.add("Rock");
        Song s1 = new Song("99999fakesongid", "Never Gonna Give You Up", "Rick Astley", "null", 1987, genres);
        Song s = songDao.create(s1.getSongId(), s1.getSongName(), s1.getArtistName(), s1.getAlbumName(), s1.getReleaseYear(), s1.getGenres());
        // ensure that it is in the database
        Song s2 = songDao.read(s.getSongId());
        assertEquals(s, s2);
        // delete and retrieve dummy song
        Song s3 = songDao.deleteSong(s.getSongId());
        // check that the returned song was the dummy song
        assertEquals(s, s3);
        // check that the dummy song was in fact deleted
        Song s4 = songDao.read(s.getSongId());
        assertNull(s4);
    }

    @Test
    @Order(20)
    @DisplayName("delete returns null for an invalid song Id")
    void deleteReturnsNullInvalidSongId() {
        Song s  = songDao.deleteSong("99999fakesongid");
        assertNull(s);
    }
    /**
     * The doNothing Test to invoke before all tests
     */

    @Test
    void doNothing() {}


}