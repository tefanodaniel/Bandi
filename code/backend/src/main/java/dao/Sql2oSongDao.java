package dao;

import exceptions.DaoException;
import model.Song;
import model.SongOfTheWeekEvent;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.Set;

public class Sql2oSongDao implements SongDao {
    private final Sql2o sql2o;

    public Sql2oSongDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public Song create(String songId, String songName, String artistName, String albumName, Integer releaseYear,
                       Set<String> genres) throws DaoException {
        return null;
    };

    public Song read(String songId) throws DaoException {
        return null;
    };

    public List<Song> readAll() throws DaoException {
        return null;
    };

    public Song updateSongName(String songId, String songName) throws DaoException {
        return null;
    };

    public Song updateArtistName(String songId, String artistName) throws DaoException {
        return null;
    };

    public Song updateAlbumName(String songId, String albumName) throws DaoException {
        return null;
    };

    public Song updateReleaseYear(String songId, Integer releaseYear) throws DaoException {
        return null;
    };

    public SongOfTheWeekEvent updateGenres(String songId, Set<String> genres) throws DaoException {
        return null;
    };

    public Song deleteSong(String songId) throws DaoException {
        return null;
    }



}
