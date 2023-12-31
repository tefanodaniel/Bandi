package dao;

import model.Musician;
import model.Band;
import exceptions.DaoException;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.*;

public class Sql2oBandDao implements BandDao {

    private final Sql2o sql2o;

    /**
     * Construct Sql2oBandDao.
     *
     * @param sql2o An sql2o object is injected as a dependency;
     *              it is assumed sql2o is connected to a database that contains a table called
     *              "bands" with three columns: "name", "genre", and "members"
     */
    public Sql2oBandDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Band create(String id, String name, int capacity,
                       Set<String> genres, Set<String> members) throws DaoException {
        String bandSQL = "INSERT INTO Bands (id, name, capacity) VALUES (:id, :name, :capacity)";
        String genresSQL = "INSERT INTO BandGenres (id, genre) VALUES (:id, :genre)";
        String membersSQL = "INSERT INTO BandMembers (member, band) VALUES (:member, :band)";
        try (Connection conn = sql2o.open()) {
            // Insert band into database
            conn.createQuery(bandSQL)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("capacity", capacity)
                    .executeUpdate();

            // Insert corresponding band genres into database
            for (String genre : genres) {
                conn.createQuery(genresSQL)
                        .addParameter("id", id)
                        .addParameter("genre", genre)
                        .executeUpdate();
            }

            // Insert corresponding band members into database
            for (String member : members) {
                conn.createQuery(membersSQL)
                        .addParameter("member", member)
                        .addParameter("band", id)
                        .executeUpdate();
            }

            // Return band
            return this.read(id);
        } catch(Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Band read(String id) throws DaoException {
        String sql = "SELECT * FROM (SELECT b.id as uBID, * FROM bands as b) as R\n"
                + "LEFT JOIN BandMembers as BM ON R.uBID=BM.band\n"
                + "LEFT JOIN BandGenres as BG ON R.uBID=BG.id\n"
                + "WHERE R.uBID=:id";
        try (Connection conn = sql2o.open()) {
            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("id", id).executeAndFetchTable().asList();

            // Extract non-list attributes
            if (queryResults.size() == 0) {
                return null;
            }
            String bandId = (String) queryResults.get(0).get("id");
            String name = (String) queryResults.get(0).get("name");
            int capacity = (int) queryResults.get(0).get("capacity");

            Band b = new Band(bandId, name, capacity, new HashSet<String>(), new HashSet<String>());
            for (Map row : queryResults) {
                b.addGenre((String) row.get("genre"));
                b.addMember((String) row.get("member"));
            }
            return b;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a Band with id " + id, ex);
        }
    }

    public List<Band> readAll() throws DaoException {
        String sql = "SELECT * FROM (SELECT b.id as uBID, * FROM bands as b) as R\n"
                    + "LEFT JOIN BandMembers as BM ON R.uBID=BM.band\n"
                    + "LEFT JOIN BandGenres as BG ON R.uBID=BG.id;";
        try(Connection conn = sql2o.open()) {
            List<Band> bands = this.extractBandsFromDatabase(sql, conn);
            return bands;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read bands from the database", ex);
        }
    }

    @Override
    public List<Band> readAll(String musicianId) throws DaoException {
        String sql = "SELECT * FROM (SELECT b.id as uBID, * FROM bands as b) as R\n"
                + "LEFT JOIN BandMembers as BM ON R.uBID=BM.band\n"
                + "LEFT JOIN BandGenres as BG ON R.uBID=BG.id\n"
                + "WHERE BM.member='" + musicianId + "';";
        try(Connection conn = sql2o.open()) {
            List<Band> bands = this.extractBandsFromDatabase(sql, conn);
            return bands;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read bands from the database", ex);
        }
    }

    /**
     * Note:
     * This method returns an empty list when no bands match the search filters.
     * This method processes "capacity" filter as band max capacity.
     **/
    @Override
    public List<Band> readAll(Map<String, String[]> query) throws DaoException {
        try (Connection conn = sql2o.open()) {
            Set<String> keys = query.keySet();
            boolean multiValTableFlag = false;
            String tableSQL = "";
            String filterOn = "";

            // Process search query parameters
            String[] keyArray = keys.toArray(new String[keys.size()]);

            for (int i = 0; i < keyArray.length; i++) {
                String key = keyArray[i];
                if (key.equals("capacity")) {
                    if (i==0) {
                        filterOn = key + " <= " + query.get(key)[0] + "\n";
                    }
                    else {
                        filterOn = filterOn + " AND " + key + " <= " + query.get(key)[0] + "\n";
                    }
                }
                else if (key.equals("genre") || key.equals("member")) {
                    String[] tableQueries = multiValTableQueries(key, query);
                    String newTable = tableQueries[0];
                    String tempSQL = tableQueries[1];

                    // create new table alias
                    if (multiValTableFlag) {
                            tableSQL = tableSQL + ", " + newTable + " AS (" + tempSQL + ")\n";
                    }
                    else {
                        tableSQL = tableSQL + "WITH " + newTable + " AS (" + tempSQL + ")\n";
                        multiValTableFlag = true;
                    }

                    if (i == 0) {
                        filterOn = "R.ubid IN (SELECT tbid FROM " + newTable + ")\n";
                    } else {
                        filterOn = filterOn + "AND R.ubid IN (SELECT tbid FROM " + newTable + ")\n";
                    }
                }
                else {
                    // Create SQL query expression for other attributes:
                    if (i == 0) {
                        for (int k = 0; k < query.get(key).length; k++) {
                            if (k == 0) {
                                filterOn = "UPPER(" + key + ") LIKE '%" + query.get(key)[0].toUpperCase() + "%'\n";
                            } else {
                                // Process queries with multiple values for the same query param:
                                filterOn = filterOn + "AND UPPER(" + key + ") LIKE '%" + query.get(key)[k].toUpperCase() + "%'\n";
                            }
                        }
                    }
                    else {
                        for (int k = 0; k < query.get(key).length; k++) {
                            // process queries with multiple values for the same query param
                            filterOn = filterOn + "AND UPPER(" + key + ") LIKE '%" + query.get(key)[k].toUpperCase() + "%'\n";
                        }
                    }
                }
            }

            String filterSQL = tableSQL + "SELECT * FROM (SELECT b.id as uBID, * FROM bands as b) as R\n"
                    + "LEFT JOIN BandMembers as BM ON R.uBID=BM.band\n"
                    + "LEFT JOIN BandGenres as BG ON R.uBID=BG.id\n"
                    + "WHERE " + filterOn + ";";

            String resultSQL = "SELECT * FROM (SELECT b.id as uBID, * FROM bands as b) as R\n"
                    + "LEFT JOIN BandMembers as BM ON R.uBID=BM.band\n"
                    + "LEFT JOIN BandGenres as BG ON R.uBID=BG.id\n"
                    + "WHERE R.uBID IN (";

            List<Band> partialBands = this.extractBandsFromDatabase(filterSQL, conn);
            if (partialBands.size() == 0) { return partialBands; }

            for (int i=0; i < partialBands.size(); i++) {
                if (i == partialBands.size() - 1) {
                    resultSQL += "'" + partialBands.get(i).getId() + "');";
                } else {
                    resultSQL += "'" + partialBands.get(i).getId() + "', ";
                }
            }
            return this.extractBandsFromDatabase(resultSQL, conn);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read bands from the database by filters", ex);
        }
    }

    @Override
    public Band update(String id, String name) throws DaoException{
        String sql = "WITH updated AS ("
                + "UPDATE Bands SET name = :name WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the band", ex);
        }
    }

    @Override
    public Band add(String bandId, String musicianId) throws DaoException {
        String sql = "INSERT INTO BandMembers (member, band) VALUES (:member, :band);";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("member", musicianId)
                    .addParameter("band", bandId)
                    .executeUpdate();
            return this.read(bandId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to add new member", ex);
        }
    }

    @Override
    public Band remove(String bandId, String musicianId) throws DaoException {
        String sql = "DELETE FROM BandMembers WHERE member=:musicianId AND band=:bandId;";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("musicianId", musicianId)
                    .addParameter("bandId", bandId)
                    .executeUpdate();
            return this.read(bandId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to remove member", ex);
        }
    }

    @Override
    public Band delete(String id) throws DaoException {
        String sql = "WITH deleted AS("
                +"DELETE FROM Bands WHERE id = :id RETURNING *"
                + ") SELECT * FROM deleted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the Band", ex);
        }
    }

    /**
     * Query the database and parse the results to create a list of bands such that each one has the proper list attributes.
     * This is necessary because we store our database in normalized form.
     * @param sql The SQL query string
     * @return the list of bands
     * @throws Sql2oException if query fails
     */
    private List<Band> extractBandsFromDatabase(String sql, Connection conn) throws Sql2oException {
        List<Map<String, Object>> queryResults = conn.createQuery(sql).executeAndFetchTable().asList();

        HashSet<String> alreadyAdded = new HashSet<String>();
        Map<String, Band> bands = new HashMap<String, Band>();
        for (Map row : queryResults) {
            // Extract data from this row
            String id = (String) row.get("ubid");
            String name = (String) row.get("name");
            int capacity = (int) row.get("capacity");
            String genre = (String) row.get("genre");
            String member = (String) row.get("member");

            // Check if we've seen this band already. If not, create new Band object
            if (!alreadyAdded.contains(id)) {
                alreadyAdded.add(id);
                bands.put(id, new Band(id, name, capacity, new HashSet<String>(), new HashSet<String>()));
            }
            // Add the genre and instrument from this row to the object lists
            Band b = bands.get(id);
            b.addGenre(genre);
            b.addMember(member);
        }
        return new ArrayList<Band>(bands.values());
    }

    /**
     * Join tables and create aliases for multi-value attribute tables.
     * @param key The SQL search query filter
     * @param query The user's search query map
     * @return the new alias name and sql query
     */
    private String[] multiValTableQueries(String key, Map<String, String[]> query) {
        String table;
        String newTable;
        String field;
        String newTableSQL = "";

        if (key.equals("genre")) {
            table = "bandgenres";  // table from database
            newTable = "manyGenres";
            field = "id";
        }
        else {
            table = "bandmembers";
            newTable = "manymembers";
            field = "band"; // band id field
        }

        for (int k = 0; k < query.get(key).length; k++) {
            if (k == 0) {
                newTableSQL = "SELECT tbid FROM (SELECT b.id as tBID FROM bands as b) as Rt\n" +
                        "INNER JOIN " + table + " AS t0 ON t0." + field + "= Rt.tbid\n" +
                        "AND " + "UPPER(t0." + key + ") LIKE '%" + query.get(key)[0].toUpperCase() + "%'\n";
            } else {
                // Process queries with multiple values for the same query param:
                newTableSQL = newTableSQL + "INNER JOIN " + table + " AS t" + k + " ON t" + k + "." + field + " = Rt.tbid\n" +
                        "AND " + "UPPER(t" + k + "." + key + ") LIKE '%" + query.get(key)[k].toUpperCase() + "%'\n";
            }
        }

        return new String[]{newTable, newTableSQL};
    }

}