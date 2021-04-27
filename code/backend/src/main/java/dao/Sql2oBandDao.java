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
            Band b = null;
            if (queryResults.size() > 0) {
                // Extract non-list attributes
                String bandId = (String) queryResults.get(0).get("id");
                String name = (String) queryResults.get(0).get("name");
                int capacity = (int) queryResults.get(0).get("capacity");

                b = new Band(bandId, name, capacity, new HashSet<String>(), new HashSet<String>());
                for (Map row : queryResults) {
                    b.addGenre((String) row.get("genre"));
                    b.addMember((String) row.get("member"));
                }
            } else {
                System.out.println("Warning! Attempted to read Band that doesn't exist!");
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

    @Override
    public List<Band> readAll(Map<String, String[]> query) throws DaoException {
        try (Connection conn = sql2o.open()) {
            Set<String> keys = query.keySet();
            Iterator<String> iter = keys.iterator();
            String key = iter.next();
            String filterOn;
            if (key.equals("members")) {
                filterOn = "'\"" + query.get(key)[0] + "\"'" + " = ANY (" + key + ");";
            }
            else {
                filterOn = "UPPER(" + key + ") LIKE '%" + query.get(key)[0].toUpperCase() + "%'";
                if (query.size() > 1) {
                    while (iter.hasNext()) {
                        String attribute = iter.next();
                        String filter = query.get(attribute)[0];
                        filterOn = filterOn + " AND UPPER(" + attribute + ") LIKE '%" +
                                filter.toUpperCase() + "%'";
                    }
                }
                filterOn = filterOn + ";";
            }
            String sql = "SELECT id, name, genre, size, capacity FROM Bands WHERE " + filterOn;

            return conn.createQuery(sql).executeAndFetch(Band.class);
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

}