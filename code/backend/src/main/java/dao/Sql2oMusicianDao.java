package dao;

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
                           String experience, String location) throws DaoException {
        // TODO: re-implement? Yes
        String sql = "WITH inserted AS ("
                + "INSERT INTO Musicians(id, name, experience, location) " +
                "VALUES(:id, :name, :experience, :location) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("experience", experience)
                    .addParameter("location", location)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Musician create(String id, String name) throws DaoException {
        // TODO: re-implement? I don't think so
        String sql = "WITH inserted AS ("
                + "INSERT INTO Musicians(id, name) " +
                "VALUES(:id, :name) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Musician read(String id) throws DaoException {
        // TODO: re-implement? Yes -- DONE
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM Musicians AS m, Instruments AS i, Genres AS g WHERE m.id=:id AND m.id=i.id AND m.id=g.id";
            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("id", id).executeAndFetchTable().asList();

            // Extract attributes
            String name = (String) queryResults.get(0).get("name");
            String exp = (String) queryResults.get(0).get("experience");
            String loc = (String) queryResults.get(0).get("location");

            Musician m = new Musician(id, name, new HashSet<String>(), new HashSet<String>(), exp, loc);
            for (Map row : queryResults) {
                m.addGenre((String) row.get("genre"));
                m.addInstrument((String) row.get("instrument"));
            }

            return m;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a course with id " + id, ex);
        }
    }

    @Override
    public List<Musician> readAll() throws DaoException {
        // TODO: re-implement? Yes -- DONE
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM Musicians AS m, Instruments AS i, Genres AS g WHERE m.id=i.id AND m.id=g.id";
            List<Map<String, Object>> queryResults = conn.createQuery(sql).executeAndFetchTable().asList();

            HashSet<String> alreadyAdded = new HashSet<String>();
            Map<String, Musician> musicians = new HashMap<String, Musician>();
            for (Map row : queryResults) {
                // Extract data from this row
                String id = (String) row.get("id");
                String name = (String) row.get("name");
                String exp = (String) row.get("experience");
                String loc = (String) row.get("location");
                String genre = (String) row.get("genre");
                String instrument = (String) row.get("instrument");

                // Check if we've seen this musician already. If not, create new Musician object
                if (!alreadyAdded.contains(id)) {
                    alreadyAdded.add(id);
                    musicians.put(id, new Musician(id, name, new HashSet<String>(), new HashSet<String>(), exp, loc));
                }
                // Add the genre and instrument from this row to the object lists
                Musician m = musicians.get(id);
                m.addGenre(genre);
                m.addInstrument(instrument);
            }
            return new ArrayList<Musician>(musicians.values());

        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from the database", ex);
        }
    }

    @Override
    public List<Musician> readAll(Map<String, String[]> query) throws DaoException {
        // TODO: re-implement? Yes
        try (Connection conn = sql2o.open()) {
            Set<String> keys = query.keySet();
            Iterator<String> iter = keys.iterator();
            String key = iter.next();
            String filterOn = "UPPER(" + key + ") LIKE '%" + query.get(key)[0].toUpperCase() + "%'";
            if (query.size() > 1) {
                while (iter.hasNext()) {
                    String attribute = iter.next();
                    String filter = query.get(attribute)[0];
                    filterOn = filterOn + " AND UPPER(" + attribute + ") LIKE '%" +
                            filter.toUpperCase() + "%'";
                }
            }
            filterOn = filterOn + ";";

            String sql = "SELECT * FROM Musicians WHERE " + filterOn;
            return conn.createQuery(sql).executeAndFetch(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from the database by filters", ex);
        }
    }

    @Override
    public Musician updateName(String id, String name) throws DaoException {
        // TODO: re-implement? No -- DONE
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET name = :name WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician name", ex);
        }
    }

    @Override
    public Musician updateGenres(String id, Set<String> genres) throws DaoException {
        // TODO: re-implement? Yes
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET genre = :genre WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    //.addParameter("genre", genres.pop())
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician genre", ex);
        }
    }

    @Override
    public Musician updateInstruments(String id, Set<String> instruments) throws DaoException {
        // TODO: re-implement? Yes
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET instrument = :instrument WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("instrument", instruments)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician instrument", ex);
        }
    }

    @Override
    public Musician updateExperience(String id, String experience) throws DaoException {
        // TODO: re-implement? No -- DONE
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET experience = :experience WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("experience", experience)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician experience", ex);
        }
    }

    @Override
    public Musician updateLocation(String id, String location) throws DaoException {
        // TODO: re-implement? No -- DONE
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET location = :location WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("location", location)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician location", ex);
        }
    }

    @Override
    public Musician delete(String id) throws DaoException {
        // TODO: re-implement? Possibly. Need to see about cascading deletes
        String sql = "WITH deleted AS ("
                + "DELETE FROM Musicians WHERE id = :id RETURNING *"
                + ") SELECT * FROM deleted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the musician", ex);
        }
    }
}
