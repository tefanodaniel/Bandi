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
    public Musician create(String id, String name, String genre, String instrument,
                           String experience, String location) throws DaoException {
        String sql = "WITH inserted AS ("
                + "INSERT INTO Musicians(id, name, genre, instrument, experience, location) " +
                "VALUES(:id, :name, :genre, :instrument, :experience, :location) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("genre", genre)
                    .addParameter("instrument", instrument)
                    .addParameter("experience", experience)
                    .addParameter("location", location)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Musician create(String id, String name, String genre) throws DaoException {
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
    public Musician read(String id) throws DaoException {
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
    public Musician updateName(String id, String name) throws DaoException {
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
    public Musician updateGenre(String id, String genre) throws DaoException {
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET genre = :genre WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("genre", genre)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician genre", ex);
        }
    }

    @Override
    public Musician updateInstrument(String id, String instrument) throws DaoException {
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET instrument = :instrument WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("instrument", instrument)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician instrument", ex);
        }
    }

    @Override
    public Musician updateExperience(String id, String experience) throws DaoException {
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
        return null; // stub
    }
}
