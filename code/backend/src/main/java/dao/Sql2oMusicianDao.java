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
     *   "courses" with two columns: "offeringName" and "title".
     */
    public Sql2oMusicianDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Musician create(String name, String genre, String instrument, String experience) throws DaoException {
        String sql = "WITH inserted AS ("
                + "INSERT INTO Musicians(name, genre, instrument, experience) " +
                "VALUES(:name, :genre, :instrument, :experience) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
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

    @Override
    public Musician update(int id, String name) throws DaoException {
        return null; // stub
    }

    @Override
    public Musician delete(int id) throws DaoException {
        return null; // stub
    }
}
