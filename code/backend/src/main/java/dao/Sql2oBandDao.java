package dao;

import model.Musician;
import model.Band;
import exceptions.DaoException;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public Band create(String id, String name, String genre,
                       int size, int capacity, List<String> members) throws DaoException {
        String sql = "WITH inserted AS ("
                + "INSERT INTO Bands(id, name, genre, size, capacity, members)" +
                "VALUES(:id, :name, :genre, :size, :capacity, %s) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            Band band = new Band(id, name, genre, size, capacity, members);
            String sql_with_id = String.format(sql, band.getMemberString());

            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("genre", genre)
                    .addParameter("size", size)
                    .addParameter("capacity", capacity)
                    .executeAndFetchFirst(Band.class);
        } catch(Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Band read(String id) throws DaoException {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT id, name, genre, size, capacity FROM Bands WHERE id = :id;")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a Band with id " + id, ex);
        }
    }

    public List<Band> readAll() throws DaoException {
        try(Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT id, name, genre, size, capacity FROM Bands;").executeAndFetch(Band.class);
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
    public Band add(String id, String musID) throws DaoException {
        /*
        try (Connection conn = sql2o.open()) {
            return conn.createQuery()
                    //FIXME
                    .executeAndFetchFirst(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to add new member", ex);
        }*/
        return null;
    }

    @Override
    public Band remove(String id, String musID) throws DaoException {
        /*
        try (Connection conn = sql2o.open()) {
            return conn.createQuery()
                    //FIXME
                    .executeAndFetchFirst(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to remove member", ex);
        }*/
        return null;
    }

    @Override
    public Band delete(String id) throws DaoException {
        String sql = "WITH deleted AS(+"
                +"DELETE FROM Bands WHERE id = :id RETURNING *"
                + ") SELECTED * FROM deleted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the Band", ex);
        }
    }
}