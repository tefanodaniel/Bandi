package dao;
import model.Musician;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * @param genres The associated genres of music.
     * @param instruments The associated instruments
     * @param experience Self-declared level of expertise
     * @param location The location of a musician.
     * @param friends The musician ids of this musician's friends.
     * @param admin flag for if musician is an admin or not.
     * @return The client object created.
     * @throws DaoException A generic exception for CRUD operations.
     */

    Musician create(String id, String name, Set<String> genres, Set<String> instruments,
                    String experience, String location, String zipCode,
                    Set<String> profileLinks, Set<String> friends, boolean admin) throws DaoException;


    /**
     * Create a Musician.
     *
     *
     * @param id The musician id.
     * @param name The musician name.
     * @return The client object created.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician create(String id, String name) throws DaoException;

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
    List<Musician> readAll(Map<String, String[]> query) throws DaoException;

    /**
     * Update the name of a Musicians provided the id.
     *
     * @param id The unique identifier for each Musician object.
     * @param name The Musician name.
     * @return The updated Musician object.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician updateName(String id, String name) throws DaoException;

    Musician updateGenres(String id, Set<String> genres) throws DaoException;

    Musician updateInstruments(String id, Set<String> instruments) throws DaoException;

    Musician updateExperience(String id, String name) throws DaoException;

    Musician updateLocation(String id, String name) throws DaoException;

    Musician updateZipCode(String id, String zipCode) throws DaoException;

    Musician updateAdmin(String id, boolean admin) throws DaoException;

    Musician updateProfileLinks(String id, Set<String> links) throws DaoException;

    // TODO: add function for adding friends

    /**
     * Delete a Musician provided its offeringName.
     *
     * @param id The unique identifier for each Musician object.
     * @return The Musician object deleted from the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    Musician delete(String id) throws DaoException;


}
