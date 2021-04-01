package util;

import model.Band;
import model.Musician;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

/**
 * A utility class with methods to establish JDBC connection, set schemas, etc.
 */
public final class Database {
    public static boolean USE_TEST_DATABASE = true;

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

            String sql = "CREATE TABLE IF NOT EXISTS Musicians("
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

            String musician_sql = "INSERT INTO Musicians(id, name, experience, location, " +
                                                        "zipCode, latitude, longitude, distance)" +
                                " VALUES(:id, :name, :experience, :location, :zipCode, " +
                                        ":latitude, :longitude, :distance);";
            String instrument_sql = "INSERT INTO Instruments(id, instrument) VALUES(:id, :instrument);";
            String genre_sql = "INSERT INTO MusicianGenres(id, genre) VALUES(:id, :genre);";
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

    /**
     * Create Musicians table schema and add sample CS Musicians to it.
     *
     * @param sql2o a Sql2o object connected to the database to be used in this application.
     * @param samples a list of sample CS Musicians.
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
