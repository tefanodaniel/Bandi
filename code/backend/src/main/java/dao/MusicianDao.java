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
     * @param location The location of a musician.
     * @return The client object created.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician create(String id, String name, String genre, String instrument, String experience, String location) throws DaoException;

    /**
     * Create a Musician.
     *
     * @param id The musician id.
     * @param name The musician name.
     * @param genre The associated genre of music.
     * @return The client object created.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician create(String id, String name, String genre) throws DaoException;

    /**
     * Read a Musician provided its offeringName.
     *
     * @param id The unique identifier for each Musician object.
     * @return The Musician object read from the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician read(String id) throws DaoException;

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
     * @param query A search term.
     * @return All Musicians retrieved.
     * @throws DaoException A generic exception for CRUD operations.
     */
    List<Musician> readAll(String query) throws DaoException;


    /**
     * Update the name of a Musicians provided the id.
     *
     * @param id The unique identifier for each Musician object.
     * @param name The Musician name.
     * @return The updated Musician object.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician updateName(String id, String name) throws DaoException;

    Musician updateGenre(String id, String name) throws DaoException;

    Musician updateInstrument(String id, String name) throws DaoException;

    Musician updateExperience(String id, String name) throws DaoException;

    Musician updateLocation(String id, String name) throws DaoException;

    /**
     * Delete a Musician provided its offeringName.
     *
     * @param id The unique identifier for each Musician object.
     * @return The Musician object deleted from the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician delete(String id) throws DaoException;


}
