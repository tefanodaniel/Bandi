package util;

import model.Band;
import model.Musician;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * A utility class with methods to establish JDBC connection, set schemas, etc.
 */
public final class Database {
    public static boolean USE_TEST_DATABASE = false;

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
        createMusiciansTableWithSampleData(sql2o, DataStore.sampleMusicians());
        createBandsTableWithSampleData(sql2o, DataStore.sampleBands());
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
        String databaseUrl = System.getenv("DATABASE_URL");
        if (USE_TEST_DATABASE) {
            databaseUrl = System.getenv("TEST_DATABASE_URL");
        }
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
    public static void createMusiciansTableWithSampleData(Sql2o sql2o, List<Musician> samples) throws Sql2oException {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS Musicians;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS Musicians("
                    + "id VARCHAR(30) PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "genre VARCHAR(30) NOT NULL,"
                    + "instrument VARCHAR(50),"
                    + "experience VARCHAR(30),"
                    + "location VARCHAR(30)"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "INSERT INTO Musicians(id, name, genre) VALUES(:id, :name, :genre);";
            for (Musician Musician : samples) {
                conn.createQuery(sql).bind(Musician).executeUpdate();
            }
        } catch (Sql2oException ex) {
            throw new Sql2oException(ex.getMessage());
        }
    }

    /**
     * Create Bands table schema and add sample bands to it.
     *
     * @param sql2o a Sql2o object connected to the database to be used in this application.
     * @param sampleBands a list of sample bands
     * @throws Sql2oException an generic exception thrown by Sql2o encapsulating anny issues with the Sql2o ORM.
     */
    private static void createBandsTableWithSampleData(Sql2o sql2o, List<Band> sampleBands) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS Bands;").executeUpdate();

            String sql = "CREATE TABLE IF NOT EXISTS Bands("
                    + "id VARCHAR(30) PRIMARY KEY,"
                    + "name VARCHAR(30) NOT NULL,"
                    + "genre VARCHAR(30) NOT NULL,"
                    + "size INTEGER,"
                    + "capacity INTEGER"
                    + ");";
            conn.createQuery(sql).executeUpdate();

            sql = "INSERT INTO Bands(id, name, genre) VALUES(:id, :name, :genre);";
            for (Band band : sampleBands) {
                conn.createQuery(sql).bind(band).executeUpdate();
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
