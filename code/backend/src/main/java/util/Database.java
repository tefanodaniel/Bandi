package util;

import model.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;

/**
 * A utility class with methods to establish JDBC connection, set schemas, etc.
 */
public final class Database {
    public static boolean USE_TEST_DATABASE = Boolean.parseBoolean(System.getenv("isLocal"));

    private Database() {
        // This class should not be instantiated.
    }

    /**
     * Connect to the database and build the tables with sample data for this application.
     * <p>
     * Caution: Use this to cleanup the database.
     * </p>
     *
     * @param args command-line arguments; not used here.
     * @throws URISyntaxException Checked exception thrown to indicate the provided database URL cannot be parsed as a
     *     URI reference.
     */
    public static void main(String[] args) throws URISyntaxException {
        Sql2o sql2o = getSql2o();
        createMusicianTablesWithSampleData(sql2o, DataStore.sampleMusicians());
        createBandTablesWithSampleData(sql2o, DataStore.sampleBands());
        createSpeedDateEventsWithSampleData(sql2o, DataStore.sampleSpeedDateEvents());
        createRequestTableWithSamples(sql2o, new ArrayList<FriendRequest>());
        createSongTablesWithSampleData(sql2o, DataStore.sampleSongs());
        createSotwSubmissionTablesWithSampleData(sql2o, DataStore.sampleSotwSubmissions());
        createSotwEventTablesWithSampleData(sql2o, DataStore.sampleSotwEvents());
    }

    /**
     * Create and return a Sql2o object connected to the database pointed to by the DATABASE_URL.
     *
     * @return a Sql2o object connected to the database to be used in this application.
     * @throws URISyntaxException Checked exception thrown to indicate the provided database URL cannot be parsed as a
     *     URI reference.
     * @throws Sql2oException an generic exception thrown by Sql2o encapsulating anny issues with the Sql2o ORM.
     */
    public static Sql2o getSql2o() throws URISyntaxException, Sql2oException {
        String databaseUrl = getDatabaseUrl();
        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        return new Sql2o(dbUrl, username, password);
    }

    /**
     * Create Musicians table schema and add sample CS Musicians to it.
     *
     * @param sql2o a Sql2o object connected to the database to be used in this application.
     * @param samples a list of sample CS Musicians.
     * @throws Sql2oException an generic exception thrown by Sql2o encapsulating anny issues with the Sql2o ORM.
     */
    public static void createMusicianTablesWithSampleData(Sql2o sql2o, List<Musician> samples) throws Sql2oException {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS Musicians CASCADE;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS Instruments;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS MusicianGenres;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS ProfileAVLinks;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS MusicianFriends;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS Musicians("
                    + "id VARCHAR(30) PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "experience VARCHAR(30),"
                    + "location VARCHAR(30),"
                    + "zipCode VARCHAR(10),"
                    + "latitude DOUBLE PRECISION,"
                    + "longitude DOUBLE PRECISION,"
                    + "distance DOUBLE PRECISION DEFAULT 9999.0,"
                    + "admin boolean"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS Instruments("
                    + "id VARCHAR(30) REFERENCES Musicians, " // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "instrument VARCHAR(50)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS MusicianGenres("
                    + "id VARCHAR(30) REFERENCES Musicians, " // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "genre VARCHAR(30)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS ProfileAVLinks("
                    + "id VARCHAR(30) REFERENCES Musicians, " // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "link VARCHAR(100)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS MusicianFriends("
                    + "id VARCHAR(30) REFERENCES Musicians, " // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "friendID VARCHAR(30) REFERENCES Musicians"
                    + ");";

            String musician_sql = "INSERT INTO Musicians(id, name, experience, location, " +
                    "zipCode, latitude, longitude, distance, admin)" +
                    " VALUES(:id, :name, :experience, :location, " +
                    ":zipCode, :latitude, :longitude, :distance, :admin);";

            conn.createQuery(sql).executeUpdate();
            String instrument_sql = "INSERT INTO Instruments(id, instrument) VALUES(:id, :instrument);";
            String genre_sql = "INSERT INTO MusicianGenres(id, genre) VALUES(:id, :genre);";
            String friend_sql = "INSERT INTO MusicianFriends(id, friendID) VALUES(:id, :friendID);";
            String link_sql = "INSERT INTO profileavlinks(id, link) VALUES(:id, :link);";

            for (Musician m : samples) {
                conn.createQuery(musician_sql).bind(m).executeUpdate();
                // Does this break if the class has more attributes than there are columns? Nope!

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

                // Insert all friends for this musician
                for (String friendID : m.getFriends()) {
                    conn.createQuery(friend_sql)
                            .addParameter("id", m.getId())
                            .addParameter("friendID", friendID)
                            .executeUpdate();
                }

                for (String link : m.getProfileLinks()) {
                    conn.createQuery(link_sql)
                            .addParameter("id", m.getId())
                            .addParameter("link", link)
                            .executeUpdate();
                }
            }

        } catch (Sql2oException ex) {
            throw new Sql2oException(ex.getMessage());
        }
    }

    public static void createRequestTableWithSamples(Sql2o sql2o, List<FriendRequest> samples) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS Requests;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS Requests("
                    + "senderid VARCHAR(30) REFERENCES Musicians,"
                    + "recipientid VARCHAR(30) REFERENCES Musicians,"
                    + "CONSTRAINT unique_message UNIQUE(senderid, recipientid)"
                    + ");";

            conn.createQuery(sql).executeUpdate();

            String requestSql = "INSERT INTO Requests(senderid, recipientid) VALUES (:senderid, :recipientid);";

            for (FriendRequest fr : samples) {
                conn.createQuery(requestSql)
                        .addParameter("senderid", fr.getSenderID())
                        .addParameter("recipientid", fr.getRecipientID())
                        .executeUpdate();
            }

        } catch (Sql2oException ex) {
            throw new Sql2oException(ex.getMessage());
        }
    }

    /**
     * Create Bands table schema and add sample CS Musicians to it.
     *
     * @param sql2o a Sql2o object connected to the database to be used in this application.
     * @param samples a list of sample bands.
     * @throws Sql2oException an generic exception thrown by Sql2o encapsulating anny issues with the Sql2o ORM.
     */
    public static void createBandTablesWithSampleData(Sql2o sql2o, List<Band> samples) throws Sql2oException {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS Bands CASCADE;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS BandMembers;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS BandGenres;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS Bands("
                    + "id VARCHAR(50) PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "capacity integer CHECK(capacity > 0)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS BandMembers("
                    + "member VARCHAR(30) REFERENCES Musicians," // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "band VARCHAR(50) REFERENCES Bands"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS BandGenres("
                    + "id VARCHAR(50) REFERENCES Bands," // TODO: Add ON DELETE CASCADE somehow. Was getting weird error
                    + "genre VARCHAR(30)"
                    + ");";
            conn.createQuery(sql).executeUpdate();



            String band_sql = "INSERT INTO Bands(id, name, capacity) VALUES(:id, :name, :capacity);";
            String bandmembers_sql = "INSERT INTO BandMembers(member, band) VALUES(:member, :band);";
            String bandgenres_sql = "INSERT INTO BandGenres(id, genre) VALUES(:id, :genre);";
            for (Band b : samples) {
                conn.createQuery(band_sql)
                        .addParameter("id", b.getId())
                        .addParameter("name", b.getName())
                        .addParameter("capacity", b.getCapacity())
                        .executeUpdate();
                // Does this break if the class has more attributes than there are columns? Nope!

                // Insert all band member info
                for (String member : b.getMembers()) {
                    conn.createQuery(bandmembers_sql)
                            .addParameter("member", member)
                            .addParameter("band",b.getId())
                            .executeUpdate();
                }

                // Insert all genres for this musician
                for (String genre : b.getGenres()) {
                    conn.createQuery(bandgenres_sql)
                            .addParameter("id", b.getId())
                            .addParameter("genre", genre)
                            .executeUpdate();
                }


            }



        } catch (Sql2oException ex) {
            throw new Sql2oException(ex.getMessage());
        }
    }


    /**
     * Create Speed Dating Events table schema and add sample speed dating events
     *
     * @param sql2o a Sql2o object connected to the database to be used in this application.
     * @param samples a list of sample SpeedDateEvents
     * @throws Sql2oException an generic exception thrown by Sql2o encapsulating anny issues with the Sql2o ORM.
     */
    public static void createSpeedDateEventsWithSampleData(Sql2o sql2o, List<SpeedDateEvent> samples) throws Sql2oException {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS SpeedDateEvents CASCADE;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS SpeedDateParticipants;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS SpeedDateEvents("
                    + "id VARCHAR(50) PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "link VARCHAR(50),"
                    + "date VARCHAR(50),"
                    + "minusers integer"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS SpeedDateParticipants("
                    + "participant VARCHAR(30),"
                    + "event VARCHAR(50)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            String event_sql = "INSERT INTO SpeedDateEvents(id, name, link, date, minusers) VALUES(:id, :name, :link, :date, :minusers);";
            String participants_sql = "INSERT INTO SpeedDateParticipants(participant, event) VALUES(:participant, :event);";
            for (SpeedDateEvent e : samples) {
                conn.createQuery(event_sql)
                        .addParameter("id", e.getId())
                        .addParameter("name", e.getName())
                        .addParameter("link", e.getLink())
                        .addParameter("date", e.getDate())
                        .addParameter("minusers", e.getMinusers())
                        .executeUpdate();

                // Insert participant info
                for (String participant : e.getParticipants()) {
                    conn.createQuery(participants_sql)
                            .addParameter("participant", participant)
                            .addParameter("event", e.getId())
                            .executeUpdate();
                }
            }

        } catch (Sql2oException ex) {
            throw new Sql2oException(ex.getMessage());
        }
    }

    public static void createSongTablesWithSampleData(Sql2o sql2o, List<Song> sample_songs) throws Sql2oException {
        try (Connection conn = sql2o.open()) {

            conn.createQuery("DROP TABLE IF EXISTS Songs CASCADE;").executeUpdate();
            conn.createQuery("DROP TABLE IF EXISTS SongGenres;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS Songs("
                    + "songId VARCHAR(50) PRIMARY KEY,"
                    + "songName VARCHAR(50) NOT NULL,"
                    + "artistName VARCHAR(50) NOT NULL,"
                    + "albumName VARCHAR(50),"
                    + "releaseYear integer"
                    + ");";

            conn.createQuery(sql).executeUpdate();

            sql = "CREATE TABLE IF NOT EXISTS SongGenres("
                    + "songId VARCHAR(50) REFERENCES Songs,"
                    + "genre VARCHAR(30)"
                    + ");";

            conn.createQuery(sql).executeUpdate();

            String song_sql = "INSERT INTO Songs(songId, songName, artistName, albumName, releaseYear) VALUES(:songId, :songName, :artistName, :albumName, :releaseYear);";
            String song_genres_sql = "INSERT INTO SongGenres(songId, genre) VALUES (:songId, :genre);";

            for (Song s: sample_songs) {
                conn.createQuery(song_sql)
                        .addParameter("songId", s.getSongId())
                        .addParameter("songName", s.getSongName())
                        .addParameter("artistName", s.getArtistName())
                        .addParameter("albumName", s.getAlbumName())
                        .addParameter("releaseYear", s.getReleaseYear())
                        .executeUpdate();

                // Insert song-genre info.

                for (String genre : s.getGenres()) {
                    conn.createQuery(song_genres_sql)
                            .addParameter("songId", s.getSongId())
                            .addParameter("genre", genre)
                            .executeUpdate();
                }
            }
        } catch (Sql2oException ex) {
            throw new Sql2oException(ex.getMessage());
        }
    }

    public static void createSotwSubmissionTablesWithSampleData(Sql2o sql2o, List<SongOfTheWeekSubmission> sample_submissions) throws Sql2oException {
            try (Connection conn = sql2o.open()) {
                conn.createQuery("DROP TABLE IF EXISTS SotwSubmissions CASCADE;").executeUpdate();
                conn.createQuery("DROP TABLE IF EXISTS SotwSubmissionsInstruments;").executeUpdate();

                String sql = "CREATE TABLE IF NOT EXISTS SotwSubmissions("
                        + "submissionid VARCHAR(50) PRIMARY KEY,"
                        + "musicianid VARCHAR(50) REFERENCES Musicians,"
                        + "avsubmission VARCHAR(50) NOT NULL"
                        + ");";

                conn.createQuery(sql).executeUpdate();

                sql = "CREATE TABLE IF NOT EXISTS SotwSubmissionsInstruments("
                        + "submissionid VARCHAR(50) REFERENCES SotwSubmissions,"
                        + "instrument VARCHAR(30)"
                        + ");";

                conn.createQuery(sql).executeUpdate();

                String sotw_submissions_sql = "INSERT INTO SotwSubmissions(submissionid, musicianid, avsubmission) VALUES(:submissionid, :musicianid, :avsubmission);";
                String sotw_submissions_instruments_sql = "INSERT INTO SotwSubmissionsInstruments(submissionid, instrument) VALUES (:submissionid, :instrument);";

                for (SongOfTheWeekSubmission s: sample_submissions) {
                    conn.createQuery(sotw_submissions_sql)
                            .addParameter("submissionid", s.getSubmission_id())
                            .addParameter("musicianid", s.getMusician_id())
                            .addParameter("avsubmission", s.getAVSubmission())
                            .executeUpdate();

                    // Insert sotw-submissions-instruments info.

                    for (String instrument : s.getInstruments()) {
                        conn.createQuery(sotw_submissions_instruments_sql)
                                .addParameter("submissionid", s.getSubmission_id())
                                .addParameter("instrument", instrument)
                                .executeUpdate();
                    }
                }


            } catch (Sql2oException ex) {
                throw new Sql2oException(ex.getMessage());
            }
    }

    public static void createSotwEventTablesWithSampleData(Sql2o sql2o, List<SongOfTheWeekEvent> sample_events) throws Sql2oException {
            try (Connection conn = sql2o.open()) {
                conn.createQuery("DROP TABLE IF EXISTS SotwEvents CASCADE;").executeUpdate();
                conn.createQuery("DROP TABLE IF EXISTS SotwEventsSubmissions;").executeUpdate();

                String sql = "CREATE TABLE IF NOT EXISTS SotwEvents("
                        + "eventid VARCHAR(50) PRIMARY KEY,"
                        + "adminid VARCHAR(50) REFERENCES Musicians,"
                        + "start_week VARCHAR(50) NOT NULL,"
                        + "end_week VARCHAR(50) NOT NULL,"
                        + "songid VARCHAR(50) NOT NULL"
                        + ");";

                conn.createQuery(sql).executeUpdate();

                sql = "CREATE TABLE IF NOT EXISTS SotwEventsSubmissions("
                        + "eventid VARCHAR(50) REFERENCES SotwEvents,"
                        + "submission VARCHAR(50)"
                        + ");";

                conn.createQuery(sql).executeUpdate();

                String sotw_events_sql = "INSERT INTO SotwEvents(eventid, adminid, start_week, end_week, songid) VALUES(:eventid, :adminid, :start_week, :end_week, :songid);";
                String sotw_events_submissions_sql = "INSERT INTO SotwEventsSubmissions(eventid, submission) VALUES (:eventid, :submission);";

                for (SongOfTheWeekEvent s: sample_events) {
                    conn.createQuery(sotw_events_sql)
                            .addParameter("eventid", s.getEventId())
                            .addParameter("adminid", s.getAdminId())
                            .addParameter("start_week", s.getStart_week())
                            .addParameter("end_week", s.getEnd_week())
                            .addParameter("songid", s.getSongId())
                            .executeUpdate();

                    // Insert sotw-events-submissions info.

                    for (String t_id : s.getSubmissions()) {
                        conn.createQuery(sotw_events_submissions_sql)
                                .addParameter("eventid", s.getEventId())
                                .addParameter("submission", t_id)
                                .executeUpdate();
                    }
                }

            } catch (Sql2oException ex) {
                throw new Sql2oException(ex.getMessage());
            }
        }

    // Get either the test or the production Database URL
    private static String getDatabaseUrl() throws URISyntaxException {
        if (USE_TEST_DATABASE == false) {
            return System.getenv("DATABASE_URL");
        } else {
            return System.getenv("TEST_DATABASE_URL");
        }
    }

    // Add Musician to the database connected to the conn object.
    private static void add(Connection conn, Musician Musician) throws Sql2oException {
        String sql = "INSERT INTO Musicians(id, name, genre) VALUES(:id, :name, :genre);";
        conn.createQuery(sql)
                .bind(Musician)
                .executeUpdate();
    }
}
