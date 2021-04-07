package api;

import exceptions.ApiError;
import exceptions.DaoException;
import model.Song;
import spark.Route;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static api.ApiServer.songDao;
import static api.ApiServer.gson;

public class SongController {

    // Get all Songs
    public static Route getAllSongs = (req, res) -> {
        try {
            List<Song> songs = songDao.readAll();
            return gson.toJson(songs);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get (read) a song given songId
    public static Route getSongById = (req, res) -> {
        try {
            String songid = req.params("songid");
            Song s = songDao.read(songid);
            if (s == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(s);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // post (create) a song
    public static Route postSong = (req, res) -> {
        try {
            Song song = gson.fromJson(req.body(), Song.class);
            Set<String> genres = song.getGenres();

            if (genres == null) { genres = new HashSet<String>(); }
            songDao.create(song.getSongId(), song.getSongName(), song.getArtistName(), song.getAlbumName(), song.getReleaseYear(), genres);
            res.status(201);
            return gson.toJson(song);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // put (updated) a song with info
    public static Route putSong = (req, res) -> {

        try {

            String songid = req.params("songid");
            Song song = gson.fromJson(req.body(), Song.class);
            if (song == null) {
                throw new ApiError("Resource not found", 404);
            }

            if (! (song.getSongId().equals(songid))) {
                throw new ApiError("song ID does not match the resource identifier", 400);
            }

            String songName = song.getSongName();
            String artistName = song.getArtistName();
            String albumName = song.getAlbumName();
            Integer releaseYear = song.getReleaseYear();
            Set<String> genres = song.getGenres();

            // Update specific fields:
            boolean flag = false;
            if (songName != null) {
                flag = true;
                song = songDao.updateSongName(songid, songName);
            } if (artistName != null) {
                flag = true;
                song = songDao.updateArtistName(songid, artistName);
            } if (albumName != null) {
                flag = true;
                song = songDao.updateAlbumName(songid, albumName);
            } if (releaseYear != 0) {
                flag = true;
                song = songDao.updateReleaseYear(songid, releaseYear);
            } if (genres != null) {
                flag = true;
                song = songDao.updateGenres(songid, genres);
            } if (!flag) {
                throw new ApiError("Nothing to update", 400);
            } if (song == null) {
                throw new ApiError("Resource not found", 404);
            }

            return gson.toJson(song);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // delete (remove) a song
    public static Route deleteSong = (req, res) -> {
        try {
            String songId = req.params("songId");
            Song song = songDao.deleteSong(songId);
            if (song == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(song);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };
}
