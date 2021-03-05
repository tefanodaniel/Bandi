package dao;

import model.Musician;
import exceptions.DaoException;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

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
    public Musician create(int id, String name, String genre, String instrument, String experience) throws DaoException {
        String sql = "WITH inserted AS ("
                + "INSERT INTO Musicians(id, name, genre, instrument, experience) " +
                "VALUES(:id, :name, :genre, :instrument, :experience) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("genre", genre)
                    .addParameter("instrument", instrument)
                    .addParameter("experience", experience)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Musician create(int id, String name, String genre) throws DaoException {
        String sql = "WITH inserted AS ("
                + "INSERT INTO Musicians(id, name, genre) " +
                "VALUES(:id, :name, :genre) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("genre", genre)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Musician read(int id) throws DaoException {
        return null; // stub
    }

    @Override
    public List<Musician> readAll() throws DaoException {
        return null; // stub
    }

    @Override
    public List<Musician> readAll(String genreQuery) throws DaoException {
        return null; // stub
    }

    /** TODO: Create more update methods to update different attributes */
    @Override
    public Musician update(int id, String name) throws DaoException {
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
    public Musician delete(int id) throws DaoException {
        return null; // stub
    }
}
