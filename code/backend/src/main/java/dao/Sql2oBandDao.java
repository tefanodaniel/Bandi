package dao;

import model.Musician;
import model.Band;
import exceptions.DaoException;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

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

    public Band create(String id, String name, String genre, int size, int capacity) throws DaoException {
        String sql = "WITH inserted AS ("
                + "INSERT INTO Bands(id, name, genre, size, capacity)" +
                "VALUES(:id, :name, :genre, :size, :capacity) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
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
            return conn.createQuery("SELECT * FROM Bands WHERE id = :id;")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a course with id " + id, ex);
        }
    }

    @Override
    public Band create(String name, String genre, List<Musician> members) throws DaoException {
        return null;
    }

    public List<Band> readAll() throws DaoException {
        try(Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Bands;").executeAndFetch(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read bands form the database", ex);
        }
    }

    @Override
    public List<Band> readAll(String genreQuery) throws DaoException {
        return null;
    }

    @Override
    public Band update(String id, String name) throws DaoException {
        return null;
    }

    @Override
    public Band add(String id, Musician newMem) throws DaoException {
        return null;
    }

    @Override
    public Band remove(String id, Musician member, int musID) throws DaoException {
        return null;
    }

    @Override
    public Band delete(String id) throws DaoException {
        return null;
    }

    Band update(int id, String name) throws DaoException{
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

    Band add(int id, Musician newMem) throws DaoException {
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

    Band remove(int id, Musician member, int musID) throws DaoException {
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

    Band delete(int id) throws DaoException {
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