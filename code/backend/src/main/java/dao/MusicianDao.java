package dao;
import model.Musician;
import java.util.List;
import exceptions.DaoException;

/**
 * Data Access Object for model.Musician.
 */
public interface MusicianDao {

    /**
     * Create a Musician.
     *
     * @param id The musician id.
     * @param name The musician name.
     * @param genre The associated genre of music.
     * @param instrument The associated type of instrument
     * @param experience Self-declared level of expertise
     * @return The client object created.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician create(int id, String name, String genre, String instrument, String experience) throws DaoException;

    /**
     * Create a Musician.
     *
     * @param id The musician id.
     * @param name The musician name.
     * @param genre The associated genre of music.
     * @return The client object created.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician create(int id, String name, String genre) throws DaoException;

    /**
     * Read a Musician provided its offeringName.
     *
     * @param id The unique identifier for each Musician object.
     * @return The Musician object read from the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician read(int id) throws DaoException;

    /**
     * Read all Musicians from the database.
     *
     * @return All the Musicians in the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    List<Musician> readAll() throws DaoException;

    /**
     * Read all Musicians from the database with genre containing genreQuery.
     *
     * @param genreQuery A search term.
     * @return All Musicians retrieved.
     * @throws DaoException A generic exception for CRUD operations.
     */
    List<Musician> readAll(String genreQuery) throws DaoException;
    // We might eventually want more readAll for appropriate filters. (like location/instrument).


    /**
     * Update the name of a Musicians provided the id.
     *
     * @param id The unique identifier for each Musician object.
     * @param name The Musician name.
     * @return The updated Musician object.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician update(int id, String name) throws DaoException;
    // Need either multiple update functions for diff fields or
    // a single update function where everything not provided is set to null.

    /**
     * Delete a Musician provided its offeringName.
     *
     * @param id The unique identifier for each Musician object.
     * @return The Musician object deleted from the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician delete(int id) throws DaoException;
}
