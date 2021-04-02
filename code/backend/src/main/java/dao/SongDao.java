package dao;
import exceptions.DaoException;
import model.Musician;
import model.Song;
import model.SongOfTheWeekEvent;

import java.util.List;
import java.util.Set;

public interface SongDao {
    /**
     *
     * @param songId A unique song Id.
     * @param songName The name of the chosen song.
     * @param artistName The name of the artist.
     * @param albumName The name of the album.
     * @param releaseYear The year the album was released.
     * @param genres The list of genres the song belongs to.
     * @return
     * @throws DaoException
     */
    Song create(String songId, String songName, String artistName, String albumName, Integer releaseYear,
                Set<String> genres) throws DaoException;

    /** Read song given unique id.
     *
     * @param songId
     * @return Song
     * @throws DaoException
     */
    Song read(String songId) throws DaoException;

    /**
     * Read all Songs from the database.
     *
     * @return All the songs in the data source.
     * @throws DaoException A generic exception for CRUD operations.
     */
    List<Song> readAll() throws DaoException;


    /** Update the Song Name.
     *
     * @param songId
     * @param songName
     * @return Song
     * @throws DaoException
     */
    Song updateSongName(String songId, String songName) throws DaoException;

    /** Update the Artist Name associated with a specific song.
     *
     * @param songId
     * @param artistName
     * @return Song
     * @throws DaoException
     */
    Song updateArtistName(String songId, String artistName) throws DaoException;

    /** Update the Album Name associated with a specific song.
     *
     * @param songId
     * @param albumName
     * @return Song
     * @throws DaoException
     */
    Song updateAlbumName(String songId, String albumName) throws DaoException;

    /** Update the Release year associated with a specific song.
     *
     * @param songId
     * @param releaseYear
     * @return Song
     * @throws DaoException
     */

    Song updateReleaseYear(String songId, Integer releaseYear) throws DaoException;

    /** Update the genres associated with a specific song
     *
     * @param songId
     * @param genres
     * @return Song
     * @throws DaoException
     */

    SongOfTheWeekEvent updateGenres(String songId, Set<String> genres) throws DaoException;

    /** Delete a song from database given unique id.
     *
     * @param songId
     * @return Song
     * @throws DaoException
     */
     Song deleteSong(String songId) throws DaoException;

}
