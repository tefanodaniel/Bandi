package dao;

import model.Band;
import exceptions.DaoException;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.list;

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
    public Band create(int id, String name, String genre, int size, int capacity) throws DaoException {
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
                    .executeAndFecthcFirst(Band.class)
        } catch(Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    Band read(int id) throws DaoException {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Bands WHERE id = :id;")
                    .add Parameter("id", id)
                    .executeAndFetchFirst(Band.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a course with id " + id, ex);
        }
        return null;
    }

    @Override
    List<Band> readAll() throws DaoException {
        try(Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM Bands;").executeAndFetch(Course.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read bands form the database", ex);
        }
    }

    @Override
    List<Band> readAll(String genreQuery) throws DaoException {
        return null;
    }

    @Override
    Band update(int id, String name) throws DaoException{
        return null;
    }

    @Override
    Band add(int id, Musician newMem) throws DaoException {
        return null;
    }

    @Override
    Band remove(int id, Musician member, int musID) throws DaoException {
        return null;
    }

    @Override
    Band delete(int id) throws DaoException {
        return null;
    }
}