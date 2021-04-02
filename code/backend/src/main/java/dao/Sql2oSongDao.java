package dao;

import exceptions.DaoException;
import model.Song;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.*;

public class Sql2oSongDao implements SongDao {
    private final Sql2o sql2o;

    public Sql2oSongDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public Song create(String songId, String songName, String artistName, String albumName, Integer releaseYear,
                       Set<String> genres) throws DaoException {
        String song_sql = "INSERT INTO Songs (songId, songName, artistName, albumName, releaseYear)" +
                "VALUES (:songId, :songName, :artistName, :albumName, :releaseYear)";
        String song_genres_sql = "INSERT INTO SongGenres (songId, genre) VALUES (:songId, :genre)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(song_sql)
                    .addParameter("songId", songId)
                    .addParameter("songName", songName)
                    .addParameter("artistName", artistName)
                    .addParameter("albumName", albumName)
                    .addParameter("releaseYear", releaseYear)
                    .executeUpdate();

            for (String genre : genres) {
                conn.createQuery(song_genres_sql)
                        .addParameter("songId", songId)
                        .addParameter("genre", genre)
                        .executeUpdate();
            }

            return this.read(songId);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    };

    public Song read(String songId) throws DaoException {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM (SELECT S.songid as SID, * FROM songs as S) as R\n"
                    + "LEFT JOIN SongGenres as G ON R.SID=G.songId\n"
                    + "WHERE R.SID=:songId;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("songId", songId).executeAndFetchTable().asList();

            if (queryResults.size() == 0) {
                return null;
            }

            String songName = (String) queryResults.get(0).get("songName");
            String artistName = (String) queryResults.get(0).get("artistName");
            String albumName = (String) queryResults.get(0).get("albumName");
            Integer releasedYear = (Integer) queryResults.get(0).get("releasedYear");

            Song s = new Song(songId, songName, artistName, albumName, releasedYear);
            for (Map row : queryResults) {
                if (row.get("genre") != null) {
                    s.addGenre((String) row.get("genre"));
                }
            }

            return s;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read song with id " + songId, ex);
        }
    };

    public List<Song> readAll() throws DaoException {
        String sql = "SELECT * FROM (SELECT S.songId as SID, * FROM Songs as  S) as R\n" +
                "LEFT JOIN songgenres as G on R.SID=G.songId;";

        try (Connection conn = sql2o.open()) {
            List<Song> songs = this.extractSongsFromDatabase(sql, conn);
            System.out.println(songs);
            return songs;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from database", ex);
        }
    };

    public Song updateSongName(String songId, String songName) throws DaoException {
        String sql = "UPDATE Songs SET songName=:songName WHERE songId=:songId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("songId", songId).addParameter("songName", songName).executeUpdate();
            return this.read(songId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song name", ex);
        }
    };

    public Song updateArtistName(String songId, String artistName) throws DaoException {
        String sql = "UPDATE Songs SET artistName=:artistName WHERE songId=:songId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("songId", songId).addParameter("artistName", artistName).executeUpdate();
            return this.read(songId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song name", ex);
        }
    };

    public Song updateAlbumName(String songId, String albumName) throws DaoException {
        String sql = "UPDATE Songs SET albumName=:albumName WHERE songId=:songId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("songId", songId).addParameter("albumName", albumName).executeUpdate();
            return this.read(songId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song name", ex);
        }
    };

    public Song updateReleaseYear(String songId, Integer releaseYear) throws DaoException {
        String sql = "UPDATE Songs SET releaseYear=:releaseYear WHERE songId=:songId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("songId", songId).addParameter("releaseYear", releaseYear).executeUpdate();
            return this.read(songId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song name", ex);
        }
    };

    public Song updateGenres(String songId, Set<String> newGenres) throws DaoException {
        String getCurrentGenresSQL = "SELECT * FROM SongGenres WHERE songId=:songId";
        String deleteGenreSQL = "DELETE FROM SongGenres WHERE songId=:songId AND genre=:genre";
        String insertGenreSQL = "INSERT INTO SongGenres (songId, genre) VALUES (:songId, :genre)";
        try (Connection conn = sql2o.open()) {
            // Get current genres stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentGenresSQL).addParameter("songId", songId).executeAndFetchTable().asList();
            HashSet<String> currentGenres = new HashSet<String>();
            for (Map row : rows) {
                currentGenres.add((String) row.get("genre"));
            }

            // Delete any values currently in the database that aren't in the new set of genres to store
            for (String genre : currentGenres) {
                if (!newGenres.contains(genre)) {
                    conn.createQuery(deleteGenreSQL).addParameter("songId", songId).addParameter("genre", genre).executeUpdate();
                }
            }

            // Add new genres to the database, if they aren't already in there
            for (String genre : newGenres) {
                if (!currentGenres.contains(genre)) {
                    conn.createQuery(insertGenreSQL).addParameter("songId", songId).addParameter("genre", genre).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(songId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song genre", ex);
        }

    };

    public Song deleteSong(String songId) throws DaoException {
        String deleteSongGenresSQL = "DELETE FROM SongGenres WHERE songId=:songId;";
        String deleteSongSQL = "DELETE FROM Songs WHERE songId=:songId;";
        try (Connection conn = sql2o.open()) {
            // Get songs to return before we delete
            Song songToDelete = this.read(songId);

            // Delete associated genres
            conn.createQuery(deleteSongGenresSQL).addParameter("songId", songId).executeUpdate();

            // Delete musician
            conn.createQuery(deleteSongSQL).addParameter("songId", songId).executeUpdate();


            return songToDelete;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the musician", ex);
        }
    }

    /* *
     * Query the database and parse the results to create a list of songs such that each one has the proper list attributes
     *  This is necessary because we store our database in normalized form.
     * @param sql The SQL query string
     * @return the list of songs
     * @throws Sql2oException if query fails
     */
    private List<Song> extractSongsFromDatabase(String sql, Connection conn) throws Sql2oException {
        List<Map<String, Object>> queryResults = conn.createQuery(sql).executeAndFetchTable().asList();
        HashSet<String> alreadyAdded = new HashSet<String>();
        Map<String, Song> songs = new HashMap<String, Song>();
        for (Map row : queryResults) {
            // Extract data from this row
            String songId = (String) row.get("songId");
            String songName = (String) row.get("songName");
            String artistName = (String) row.get("artistName");
            String albumName = (String) row.get("albumName");
            Integer releasedYear = (Integer) row.get("releasedYear");
            String genre = (String) row.get("genre");
            if (!alreadyAdded.contains(songId)){
                alreadyAdded.add(songId);
                songs.put(songId, new Song(songId, songName, artistName, albumName, releasedYear, new HashSet<String>()));
            }

            Song s = songs.get(songId);
            s.addGenre(genre);
        }
        System.out.println("Song Extraction successful");
        return new ArrayList<Song>(songs.values());
    }




}
