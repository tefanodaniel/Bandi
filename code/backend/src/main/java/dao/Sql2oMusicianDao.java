package dao;

import exceptions.ApiError;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import model.Musician;
import exceptions.DaoException;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import org.sql2o.data.Row;

import java.util.*;

public class Sql2oMusicianDao implements MusicianDao {

    private final Sql2o sql2o;

    /**
     * Construct Sql2oCourseDao.
     *
     * @param sql2o A Sql2o object is injected as a dependency;
     *   it is assumed sql2o is connected to a database that  contains a table called
     *   "Musicians" with columns: "id", "name", and "genre".
     */
    public Sql2oMusicianDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Musician create(String id, String name, Set<String> genres, Set<String> instruments,
                           String experience, String location, String zipCode,
                           Set<String> profileLinks, Set<String> friends, boolean admin) throws DaoException {
        // TODO: re-implement? DONE
        String musicianSQL = "INSERT INTO Musicians (id, name, experience, location, zipCode, latitude, longitude, admin) " +
                             "VALUES (:id, :name, :experience, :location, :zipCode, :latitude, :longitude, :admin)";
        String genresSQL = "INSERT INTO MusicianGenres (id, genre) VALUES (:id, :genre)";
        String instrumentsSQL = "INSERT INTO Instruments (id, instrument) VALUES (:id, :instrument)";
        String profileLinksSQL = "INSERT INTO ProfileAVLinks (id, link) VALUES (:id, :link)";
        String friendsSQL = "INSERT INTO MusicianFriends (id, friendID) VALUES (:id, :friendID)";

        double latitude;
        double longitude;
        if (zipCode.equals("NULL")) {
            latitude = 0;
            longitude = 0;
        } else {
            double[] coordinates = getLatitudeLongitude(zipCode);
            latitude = coordinates[0];
            longitude = coordinates[1];
        }

        try (Connection conn = sql2o.open()) {
            // Insert musician into database
            conn.createQuery(musicianSQL)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("experience", experience)
                    .addParameter("location", location)
                    .addParameter("zipCode", zipCode)
                    .addParameter("latitude", latitude)
                    .addParameter("longitude", longitude)
                    .addParameter("admin", admin)
                    .executeUpdate();

            // Insert corresponding genres into database
            for (String genre : genres) {
                conn.createQuery(genresSQL)
                        .addParameter("id", id)
                        .addParameter("genre", genre)
                        .executeUpdate();
            }

            // Insert corresponding instruments into database
            for (String instrument : instruments) {
                conn.createQuery(instrumentsSQL)
                        .addParameter("id", id)
                        .addParameter("instrument", instrument)
                        .executeUpdate();
            }

            // Insert corresponding profile audio/video links into database
            for (String link : profileLinks) {
                conn.createQuery(profileLinksSQL)
                        .addParameter("id", id)
                        .addParameter("link", link)
                        .executeUpdate();
            }

            // Insert corresponding friends into database
            for (String friendID : friends) {
                conn.createQuery(friendsSQL)
                        .addParameter("id", id)
                        .addParameter("friendID", friendID)
                        .executeUpdate();
            }

            // Return musician
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    private double[] getLatitudeLongitude(String zipCode) {
        final String BASE_URL = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=us-zip-code-latitude-and-longitude";
        final String QUERY_PARAMS = "&facet=state&facet=timezone&facet=dst";
        final String ZIP_CODE = "&q=" + zipCode;
        String endpoint = BASE_URL + ZIP_CODE + QUERY_PARAMS;
        JSONObject fields = Unirest.get(endpoint).asJson().getBody().getObject()
                .getJSONArray("records")
                .getJSONObject(0)
                .getJSONObject("fields");
        double latitude = fields.getDouble("latitude");
        double longitude = fields.getDouble("longitude");
        return new double[]{latitude, longitude};
    }

    @Override
    public Musician create(String id, String name) throws DaoException {
        // TODO: re-implement? DONE
        String sql = "INSERT INTO Musicians(id, name, experience, location, zipCode, latitude, longitude, admin) " +
                     "VALUES(:id, :name, 'NULL', 'NULL', 'NULL', 0, 0, FALSE);";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("id", id)
                                 .addParameter("name", name)
                                 .executeUpdate();
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Musician read(String id) throws DaoException {
        // TODO: re-implement? Yes -- DONE
        try (Connection conn = sql2o.open()) {

            // Getting relevant information from database for given musician

            String sql = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                    "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                    "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                    "LEFT JOIN profileavlinks as L ON R.MID=L.id\n" +
                    "LEFT JOIN MusicianFriends as F ON R.MID=F.id\n" +
                    "WHERE R.mid=:id;";
            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("id", id).executeAndFetchTable().asList();

            if (queryResults.size() == 0) { // no such musician is present
                return null;
            }

            // Extract attributes
            String name = (String) queryResults.get(0).get("name");
            String exp = (String) queryResults.get(0).get("experience");
            String loc = (String) queryResults.get(0).get("location");
            String zipCode = (String) queryResults.get(0).get("zipcode");
            boolean admin = (boolean) queryResults.get(0).get("admin");

            Musician m = new Musician(id, name, new HashSet<String>(), new HashSet<String>(),
                    exp, new HashSet<String>(), loc, zipCode, new HashSet<String>(), admin);

            for (Map row : queryResults) {

                if (row.get("genre") != null) {
                    m.addGenre((String) row.get("genre"));
                }
                if (row.get("instrument") != null) {
                    m.addInstrument((String) row.get("instrument"));
                }
                if (row.get("link") != null) {
                    m.addProfileLink((String) row.get("link"));
                }
                if (row.get("friendid") != null) {
                    m.addFriend((String) row.get("friendid"));
                }

            }
            return m;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a musician with id " + id, ex);
        }
    }

    @Override
    public List<Musician> readAll() throws DaoException {
        // TODO: re-implement? Yes -- DONE
        String sql = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                "LEFT JOIN profileavlinks as L ON R.MID=L.id\n" +
                "LEFT JOIN musicianfriends as F ON R.MID=F.id;";
        try (Connection conn = sql2o.open()) {

            addDefaultDistances(conn); // this will prevent a null distance val
            List<Musician> musicians = this.extractMusiciansFromDatabase(sql, conn, "");
            return musicians;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from the database", ex);
        }
    }

    @Override
    public List<Musician> readAll(Map<String, String[]> query) throws DaoException, ApiError {
        // TODO: re-implement? DONE. See comment below.
        /*  This method can now handle creating sql queries for search queries with parameters of multiple
            values (genre: "jazz", "blues").
            Note that this function returns an empty list if no Musicians match query.
         */
        try (Connection conn = sql2o.open()) {
            // Ensure that the user performing the advanced search provided their user ID
            String[] sourceIDArray = query.get("id");
            String sourceID;
            if (sourceIDArray != null) {
                sourceID = sourceIDArray[0];
            }
            else {
                throw new ApiError("Must provide user ID performing the search", 500);
            }

            Set<String> keys = query.keySet();
            boolean distFlag = false;
            boolean additionalQFlag = false;
            String distFilter = "";

            // If advanced search includes distance, apply this filter first.
            if (keys.contains("distance")) {
                distFilter = filterByDistance(query, conn);
                distFlag = true;
            }

            // Process search query parameters that are not distance and id.
            String[] keyArray = keys.toArray(new String[keys.size()]);
            String filterOn = "";

            String tableSQL = "";
            boolean multiValTableFlag = false;

            // Locate first query parameter that is not distance or id:
            int firstKeyIndex;
            for (firstKeyIndex = 0; firstKeyIndex < keyArray.length; firstKeyIndex++) {
                String key = keyArray[firstKeyIndex];
                if (!key.equals("distance") && !key.equals("id")) {
                    additionalQFlag = true;
                    if (key.equals("admin")) {
                        // Create SQL query expression for admin boolean:
                        filterOn = key + " = " + query.get(key)[0] + "\n";
                    }
                    else if (key.equals("genre") || key.equals("instrument")) {
                        // Create SQL query expression for tables with multiple values
                        multiValTableFlag = true;

                        String[] tableQueries = multiValTableQueries(key, query);
                        String newTable = tableQueries[0];
                        tableSQL = tableQueries[1];

                        tableSQL = "WITH " + newTable + " AS (" + tableSQL + ")\n";
                        filterOn = "R.mid IN (SELECT tid FROM " + newTable + ")\n";
                    }
                    else {
                        // Create SQL query expression for String attributes:
                        for (int k = 0; k < query.get(key).length; k++) {
                            if (k == 0) {
                                filterOn = "UPPER(" + key + ") LIKE '%" + query.get(key)[0].toUpperCase() + "%'\n";
                            } else {
                                // Process queries with multiple values for the same query param:
                                filterOn = filterOn + "AND UPPER(" + key + ") LIKE '%" + query.get(key)[k].toUpperCase() + "%'\n";
                            }
                        }
                    }
                    break;
                }
            }
            // Process remaining search query parameters that are not distance or id:
            for (int i=firstKeyIndex+1; i < keyArray.length; i++) {
                String key = keyArray[i];
                if (!key.equals("distance") && !key.equals("id")) {
                    if (key.equals("admin")) {
                        // Append SQL query expression for admin boolean:
                        filterOn = filterOn + " AND " + key + " = " + query.get(key)[0] + "\n";
                    }
                    else if (key.equals("genre")|| key.equals("instrument")) {
                        // Create SQL query expression for tables with multiple values
                        String[] tableQueries = multiValTableQueries(key, query);
                        String newTable = tableQueries[0];
                        String tempSQL = tableQueries[1];

                        if (multiValTableFlag) { tableSQL = tableSQL + ", " + newTable + " AS (" + tempSQL + ")\n"; }
                        else { tableSQL = tableSQL + "WITH " + newTable + " AS (" + tempSQL + ")\n"; multiValTableFlag = true; }

                        filterOn = filterOn + "AND R.mid IN (SELECT tid FROM " + newTable + ")\n";
                    }
                    else {
                        // Append SQL query expression for String attributes:
                        for (int k = 0; k < query.get(key).length; k++) {
                            // process queries with multiple values for the same query param
                            filterOn = filterOn + " AND UPPER(" + key + ") LIKE '%" + query.get(key)[k].toUpperCase() + "%'\n";
                        }
                    }
                }
            }

            String filterSQL;
            // Create final SQL query expression based on advanced search query parameters:
            if(distFlag && additionalQFlag) {
                filterSQL = tableSQL + distFilter + "AND " + filterOn + "ORDER BY distance;";
            }
            else if (distFlag && !additionalQFlag){
                filterSQL = distFilter + "ORDER BY distance;";
            }
            else {
                addDefaultDistances(conn);
                filterSQL = tableSQL + "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                        "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                        "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                        "WHERE " + filterOn + "AND R.MID <> '" + sourceID + "';";
            }
            String resultSQL = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                    "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                    "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                    "LEFT JOIN profileavlinks as L ON R.MID=L.id\n" +
                    "LEFT JOIN musicianfriends as F ON R.MID=F.id\n" +
                    "WHERE R.mid IN (";


            // Example final filterSQL queries:
            // case: distFlag && additionalQFlag
            // filterSQL = "WITH manyInstruments AS (SELECT rt.tid FROM (SELECT m.id as tID FROM musicians as m) as Rt
            //              INNER JOIN instruments AS t0 ON  t0.id = Rt.tid
            //              AND UPPER(t0.instrument) LIKE '%VOCALS%'
            //              INNER JOIN instruments AS t1 ON  t1.id = Rt.tid
            //              AND UPPER(t1.instrument) LIKE '%GUITAR%')
            //              SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R
            //              LEFT JOIN instruments as I ON R.MID=I.id
            //              LEFT JOIN musiciangenres as G ON R.MID=G.id
            //              WHERE distance <=1500
            //              AND R.mid IN (SELECT tid FROM manyInstruments)
            //              ORDER BY distance;";
            //
            // case: distFlag && !additionalQFlag
            // filterSQL = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
            //                "LEFT JOIN instruments as I ON R.MID=I.id\n" +
            //                "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
            //                "WHERE distance <=" 5000 ORDER BY distance;";
            //
            // case: no distance advanced search query
            // filterSQL = "WITH manyGenres AS (SELECT rt.tid FROM (SELECT m.id as tID FROM musicians as m) as Rt
            //              INNER JOIN musiciangenres AS t0 ON  t0.id = Rt.tid
            //              AND UPPER(t0.genre) LIKE '%ROCK%'
            //              INNER JOIN musiciangenres AS t1 ON  t1.id = Rt.tid
            //              AND UPPER(t1.genre) LIKE '%PROGRESSIVE%')
            //              , manyInstruments AS (SELECT rt.tid FROM (SELECT m.id as tID FROM musicians as m) as Rt
            //              INNER JOIN instruments AS t0 ON  t0.id = Rt.tid
            //              AND UPPER(t0.instrument) LIKE '%VOCALS%'
            //              INNER JOIN instruments AS t1 ON  t1.id = Rt.tid
            //              AND UPPER(t1.instrument) LIKE '%GUITAR%')
            //              SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R
            //              LEFT JOIN instruments as I ON R.MID=I.id
            //              LEFT JOIN musiciangenres as G ON R.MID=G.id
            //              WHERE R.mid IN (SELECT tid FROM manyGenres)
            //              AND admin = true
            //              AND R.mid IN (SELECT tid FROM manyInstruments)
            //              AND R.MID <> '00001fakeid';";

            return getCorrespondingMusicians(conn, filterSQL, resultSQL);

        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from the database by filters", ex);
        }
    }

    private String[] multiValTableQueries(String key, Map<String, String[]> query) {
        String table;
        String newTable;
        String newTableSQL = "";

        if (key.equals("genre")) {
            table = "musiciangenres";
            newTable = "manyGenres";
        }
        else {
            table = "instruments";
            newTable = "manyInstruments";
        }

        for (int k = 0; k < query.get(key).length; k++) {
            if (k == 0) {
                newTableSQL = "SELECT rt.tid FROM (SELECT m.id as tID FROM musicians as m) as Rt\n" +
                        "INNER JOIN " + table + " AS t0 ON t0.id = Rt.tid \n" +
                        "AND " + "UPPER(t0." + key + ") LIKE '%" + query.get(key)[0].toUpperCase() + "%'\n";
            } else {
                // Process queries with multiple values for the same query param:
                newTableSQL = newTableSQL + "INNER JOIN " + table + " AS t" + k + " ON t" + k + ".id = Rt.tid \n" +
                        "AND " + "UPPER(t" + k + "." + key + ") LIKE '%" + query.get(key)[k].toUpperCase() + "%'\n";
            }
        }

        return new String[]{newTable, newTableSQL};
    }

    @Override
    public List<Musician> getAllFriendsOf(String id) throws DaoException {
        // TODO: re-implement? Yes -- DONE
        String sql = "SELECT * FROM (SELECT friendid AS id FROM musicianfriends AS mf WHERE mf.id=:id) as R \n" +
                "LEFT JOIN musicianfriends AS MF USING(id)\n" +
                "LEFT JOIN musicians AS M USING(id)\n" +
                "LEFT JOIN instruments AS I USING(id)\n" +
                "LEFT JOIN musiciangenres AS MG USING(id)\n" +
                "LEFT JOIN profileavlinks AS P USING(id);";
        try (Connection conn = sql2o.open()) {
            List<Musician> musicians = this.extractMusiciansFromDatabase(sql, conn, id);
            return musicians;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from the database", ex);
        }
    }

    @Override
    public Musician updateName(String id, String name) throws DaoException {

        // TODO: re-implement? Yes -- DONE
        String sql = "UPDATE Musicians SET name=:name WHERE id=:id;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("id", id).addParameter("name", name).executeUpdate();
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician name", ex);
        }
    }

    @Override
    public Musician updateGenres(String id, Set<String> newGenres) throws DaoException {

        String getCurrentGenresSQL = "SELECT * FROM MusicianGenres WHERE id=:id";
        String deleteGenreSQL = "DELETE FROM MusicianGenres WHERE id=:id AND genre=:genre";
        String insertGenreSQL = "INSERT INTO MusicianGenres (id, genre) VALUES (:id, :genre)";
        try (Connection conn = sql2o.open()) {
            // Get current genres stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentGenresSQL).addParameter("id", id).executeAndFetchTable().asList();
            HashSet<String> currentGenres = new HashSet<String>();
            for (Map row : rows) {
                currentGenres.add((String) row.get("genre"));
            }

            // Delete any values currently in the database that aren't in the new set of genres to store
            for (String genre : currentGenres) {
                if (!newGenres.contains(genre)) {
                    conn.createQuery(deleteGenreSQL).addParameter("id", id).addParameter("genre", genre).executeUpdate();
                }
            }

            // Add new genres to the database, if they aren't already in there
            for (String genre : newGenres) {
                if (!currentGenres.contains(genre)) {
                    conn.createQuery(insertGenreSQL).addParameter("id", id).addParameter("genre", genre).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician genre", ex);
        }
    }

    @Override
    public Musician updateInstruments(String id, Set<String> newInstruments) throws DaoException {

        String getCurrentInstrumentsSQL = "SELECT * FROM Instruments WHERE id=:id";
        String deleteInstrumentSQL = "DELETE FROM Instruments WHERE id=:id AND instrument=:instrument";
        String insertInstrumentSQL = "INSERT INTO Instruments (id, instrument) VALUES (:id, :instrument)";
        try (Connection conn = sql2o.open()) {
            // Get current instruments stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentInstrumentsSQL).addParameter("id", id).executeAndFetchTable().asList();
            HashSet<String> currentInstruments = new HashSet<String>();
            for (Map row : rows) {
                currentInstruments.add((String) row.get("instrument"));
            }

            // Delete any values currently in the database that aren't in the new set of genres to store
            for (String instrument : currentInstruments) {
                if (!newInstruments.contains(instrument)) {
                    conn.createQuery(deleteInstrumentSQL).addParameter("id", id).addParameter("instrument", instrument).executeUpdate();
                }
            }

            // Add new genres to the database, if they aren't already in there
            for (String instrument : newInstruments) {
                if (!currentInstruments.contains(instrument)) {
                    conn.createQuery(insertInstrumentSQL).addParameter("id", id).addParameter("instrument", instrument).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician instrument", ex);
        }
    }

    @Override
    public Musician updateExperience(String id, String experience) throws DaoException {

        // TODO: re-implement? Yes -- DONE
        String sql = "UPDATE Musicians SET experience=:experience WHERE id=:id;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("id", id).addParameter("experience", experience).executeUpdate();
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician experience", ex);
        }
    }

    @Override
    public Musician updateLocation(String id, String location) throws DaoException {

        // TODO: re-implement? YES -- DONE
        String sql = "UPDATE Musicians SET location=:location WHERE id=:id;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("id", id).addParameter("location", location).executeUpdate();
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician location", ex);
        }
    }

    @Override
    public Musician updateZipCode(String id, String zipCode) throws DaoException {
        // TODO: re-implement? DONE, also updates latitude and longitude using new zip code
        double[] coordinates = getLatitudeLongitude(zipCode);
        double latitude = coordinates[0];
        double longitude = coordinates[1];
        String sql = "UPDATE Musicians SET zipCode=:zipCode, latitude=:latitude, longitude=:longitude WHERE id=:id;";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("id", id)
                                 .addParameter("zipCode", zipCode)
                                 .addParameter("latitude", latitude)
                                 .addParameter("longitude", longitude)
                                 .executeUpdate();
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician zipCode", ex);
        }
    }

    @Override
    public Musician updateAdmin(String id, boolean admin) throws DaoException {
        String sql = "UPDATE Musicians SET admin=:admin WHERE id=:id;";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("id", id)
                    .addParameter("admin", admin).executeUpdate();
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician admin", ex);
        }
    }

    @Override
    public Musician updateProfileLinks(String id, Set<String> newLinks) throws DaoException {

        String getCurrentLinksSQL = "SELECT * FROM ProfileAVLinks WHERE id=:id";
        String deleteLinkSQL = "DELETE FROM ProfileAVLinks WHERE id=:id AND link=:link";
        String insertLinkSQL = "INSERT INTO ProfileAVLinks (id, link) VALUES (:id, :link)";
        try (Connection conn = sql2o.open()) {
            // Get current instruments stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentLinksSQL).addParameter("id", id).executeAndFetchTable().asList();
            HashSet<String> currentLinks = new HashSet<String>();
            for (Map row : rows) {
                currentLinks.add((String) row.get("link"));
            }

            // Delete any values currently in the database that aren't in the new set of genres to store
            for (String link : currentLinks) {
                if (!newLinks.contains(link)) {
                    conn.createQuery(deleteLinkSQL).addParameter("id", id).addParameter("link", link).executeUpdate();
                }
            }

            // Add new genres to the database, if they aren't already in there
            for (String link : newLinks) {
                if (!currentLinks.contains(link)) {
                    conn.createQuery(insertLinkSQL).addParameter("id", id).addParameter("link", link).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician links", ex);
        }


    }

    public Musician updateTopTracks(String id, Set<String> topTracks) throws DaoException {
        String deleteSQL = "DELETE FROM TopTracks WHERE id=:id";
        String insertSQL = "INSERT INTO TopTracks (id, track) VALUES (:id, :track)";
        try (Connection conn = sql2o.open()) {

            // Delete all stored top tracks for the user
            conn.createQuery(deleteSQL).addParameter("id", id).executeUpdate();

            // Add new top tracks
            for (String track : topTracks) {
                conn.createQuery(insertSQL).addParameter("id", id).addParameter("track", track).executeUpdate();
            }

            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the user's Spotify top tracks", ex);
        }
    }


    @Override
    public Musician delete(String id) throws DaoException {
        // TODO: re-implement? Yes. Need to see about cascading deletes, though See note below
        /* TODO: I'm not worrying about cascading deletes here. We should later though, because this will get a little unwieldy. */
        String deleteGenresSQL = "DELETE FROM MusicianGenres WHERE id=:id;";
        String deleteFriendsSQL = "DELETE FROM MusicianFriends WHERE id=:id;";
        String deleteInstrumentsSQL = "DELETE FROM Instruments WHERE id=:id;";
        String deleteProfileLinksSQL = "DELETE FROM ProfileAVLinks WHERE id=:id;";
        String deleteMusicianSQL = "DELETE FROM Musicians WHERE id=:id;";
        try (Connection conn = sql2o.open()) {
            // Get musician to return before we delete
            Musician musicianToDelete = this.read(id);

            // Delete associated instruments
            conn.createQuery(deleteInstrumentsSQL).addParameter("id", id).executeUpdate();

            // Delete associated profile links
            conn.createQuery(deleteProfileLinksSQL).addParameter("id", id).executeUpdate();

            // Delete associated genres
            conn.createQuery(deleteGenresSQL).addParameter("id", id).executeUpdate();

            // Delete associated friends
            conn.createQuery(deleteFriendsSQL).addParameter("id", id).executeUpdate();

            // Delete musician
            conn.createQuery(deleteMusicianSQL).addParameter("id", id).executeUpdate();


            return musicianToDelete;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the musician", ex);
        }
    }

    /**
     * Query the database and parse the results to create a list of musicians such that each one has the proper list attributes.
     * This is necessary because we store our database in normalized form.
     * @param sql The SQL query string
     * @return the list of musicians
     * @throws Sql2oException if query fails
     */
    private List<Musician> extractMusiciansFromDatabase(String sql, Connection conn, String withID) throws Sql2oException {
        List<Map<String, Object>> queryResults;
        String mid_or_id;
        if (!withID.equals("")) {
            queryResults = conn.createQuery(sql).addParameter("id", withID).executeAndFetchTable().asList();
            mid_or_id = "id";
        } else {
            queryResults = conn.createQuery(sql).executeAndFetchTable().asList();
            mid_or_id = "mid";
        }

        HashSet<String> alreadyAdded = new HashSet<String>();
        Map<String, Musician> musicians = new HashMap<String, Musician>();
        for (Map row : queryResults) {
            // Extract data from this row
            String id = (String) row.get(mid_or_id);
            String name = (String) row.get("name");
            String exp = (String) row.get("experience");
            String loc = (String) row.get("location");
            String zipCode = (String) row.get("zipcode");
            String genre = (String) row.get("genre");
            String instrument = (String) row.get("instrument");
            String link = (String) row.get("link");
            double dist = (double) row.get("distance");
            String friendID = (String) row.get("friendid");
            boolean admin = (boolean) row.get("admin");

            // Check if we've seen this musician already. If not, create new Musician object
            if (!alreadyAdded.contains(id)) {
                alreadyAdded.add(id);
                musicians.put(id, new Musician(id, name, new HashSet<String>(), new HashSet<String>(),
                        exp, new HashSet<String>(), loc, zipCode, dist, new HashSet<String>(), admin));
            }
            // Add the genre and instrument from this row to the object lists
            Musician m = musicians.get(id);
            m.addGenre(genre);
            m.addInstrument(instrument);
            m.addProfileLink(link);
            m.addFriend(friendID);
        }
        return new ArrayList<Musician>(musicians.values());
    }

    private List<Musician> getCorrespondingMusicians(Connection conn, String filterSQL, String resultSQL) {
        List<Musician> partialMusicians = this.extractMusiciansFromDatabase(filterSQL, conn, "");
        for (int i=0; i < partialMusicians.size(); i++) {
            if (i == partialMusicians.size() - 1) {
                resultSQL += "'" + partialMusicians.get(i).getId() + "');";
            } else {
                resultSQL += "'" + partialMusicians.get(i).getId() + "', ";
            }
        }
        if (partialMusicians.size() == 0) { return partialMusicians; }
        return this.extractMusiciansFromDatabase(resultSQL, conn, "");
    }

    /**
     * Query the database and calculate all distances from the starting point.
     * @param startID The starting point to calculate distances from
     * @param conn The SQL connection
     * @throws Sql2oException if query fails
     */
    private void calculateDistances(String startID, Connection conn) throws Sql2oException {
        String calculateDist = "WITH allDistances AS (SELECT m.id as MID, *, earth_distance(" +
                "  ll_to_earth(m.latitude, m.longitude)," +
                "  ll_to_earth(startPoint.latitude, startPoint.longitude)) " +
                " / 1609.344 AS newDistance " +
                "FROM musicians AS m, " +
                "LATERAL (SELECT id, latitude, longitude FROM musicians " +
                "WHERE id = '" + startID + "') AS startPoint " +
                "WHERE m.id <> startPoint.id) " +
                "UPDATE musicians SET distance = (SELECT d.newDistance " +
                "FROM allDistances AS d WHERE musicians.id = d.MID)" +
                ";";

        conn.createQuery(calculateDist).executeUpdate();
    }

    /**
     * Query the database and return the SQL query to filter musicians by distance.
     * @param query The map of search parameters
     * @param conn The SQL connection
     */
    private String filterByDistance(Map<String, String[]> query, Connection conn) {
        String miles = query.get("distance")[0];
        String sourceID = query.get("id")[0];

        calculateDistances(sourceID, conn);

        String distFilter = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                "WHERE distance <=" + miles ;

        return distFilter + "\n";
    }

    private void addDefaultDistances(Connection conn) throws DaoException {
        String sql = "UPDATE Musicians SET distance = 9999";
        conn.createQuery(sql).executeUpdate();
    }
}
