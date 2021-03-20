package dao;
import model.Musician;
import model.Band;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exceptions.DaoException;
import model.Musician;

public interface BandDao {

    /**
     * Create a Band
     *
     * @param id the uniquely generated id of the band
     * @param name the band name
     * @param capacity the maximum number of musicians in this band
     * @param genres the associated genres
     * @param members the Musicians associated with the band
     * @return client object created
     * @throws DaoException, a generic exception for CRUD operations
     */
    Band create(String id, String name, int capacity,
                Set<String> genres, Set<String> members) throws DaoException;

    /**
     * Read a Band given its offeringName
     *
     * @param id the unique identifier for each Band object.
     * @return the Band object read from the data source
     * @throws DaoException, a generic exception for CRUD operations
     */
    Band read(String id) throws DaoException;

    /**
     * Read all bands from the database.
     *
     * @return all bands from the data source.
     * @throws DaoException, a generic exception for CRUD operations.
     */
    List<Band> readAll() throws DaoException;

    /**
     * Read all bands from the database that a specific musician belongs to.
     *
     * @return all matching bands from the data source.
     * @throws DaoException, a generic exception for CRUD operations.
     */
    List<Band> readAll(String musicianId) throws DaoException;

    /**
     * Read all bands from the database with genre containing query map.
     *
     * @param query map of query parameters
     * @return all Bands retrieved
     * @throws DaoException, a generic exception for CRUD operations.
     */
    List<Band> readAll(Map<String, String[]> query) throws DaoException;

    /**
     * Update the name of a Band, given its ID.
     *
     * @param id the unique identifier for each Band object.
     * @param name the Band name
     * @return the updated Band object
     * @throws DaoException A generic exception for CRUD operations
     */
    Band update(String id, String name) throws DaoException;

    /**
     * Add new Musician to the Band, given its ID.
     *
     * @param id the unique identifier for each Band object
     * @param musID id of the new Musician to be added to the band
     * @return the updated Band object
     * @throws DaoException, A generic exception for CRUD operations
     */
    Band add(String id, String musID) throws DaoException;

    /**
     * Remove a Musician from a band, given the Band ID and Musician ID
     *
     * @param id the unique identifier for each Band object
     * @param musID the unique identifier for each musician object
     * @throws DaoException, a generic exception for CRUD operations
     */
    Band remove(String id, String musID) throws DaoException;

    /**
     * Delete a Band, given its offeringName
     *
     * @param id the unique identifier for each Band object
     * @return the Band object deleted from the data source.
     * @throws DaoException, a generic exception for CRUD operations.
     */
    Band delete(String id) throws DaoException;
}
