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

    @Override
    public Song create(String songId, String songName, String artistName, String albumName, Integer releaseYear,
                       Set<String> genres) throws DaoException {
        String song_sql = "INSERT INTO Songs (songid, songname, artistname, albumname, releaseyear)" +
                "VALUES (:songid, :songname, :artistname, :albumname, :releaseyear)";
        String song_genres_sql = "INSERT INTO SongGenres (songid, genre) VALUES (:songid, :genre)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(song_sql)
                    .addParameter("songid", songId)
                    .addParameter("songname", songName)
                    .addParameter("artistname", artistName)
                    .addParameter("albumname", albumName)
                    .addParameter("releaseyear", releaseYear)
                    .executeUpdate();

            for (String genre : genres) {
                conn.createQuery(song_genres_sql)
                        .addParameter("songid", songId)
                        .addParameter("genre", genre)
                        .executeUpdate();
            }

            return this.read(songId);
        } catch (Sql2oException ex) {
            System.out.println(ex);
            throw new DaoException(ex.getMessage(), ex);
        }
    };

    @Override
    public Song read(String songid) throws DaoException {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM (SELECT S.songid as SID, * FROM songs as S) as R\n"
                    + "LEFT JOIN SongGenres as G ON R.SID=G.songid\n"
                    + "WHERE R.SID=:songid;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("songid", songid).executeAndFetchTable().asList();

            if (queryResults.size() == 0) {
                return null;
            }

            String songName = (String) queryResults.get(0).get("songname");
            String artistName = (String) queryResults.get(0).get("artistname");
            String albumName = (String) queryResults.get(0).get("albumname");
            Integer releaseYear = (Integer) queryResults.get(0).get("releaseyear");

            Song s = new Song(songid, songName, artistName, albumName, releaseYear);
            for (Map row : queryResults) {
                if (row.get("genre") != null) {
                    s.addGenre((String) row.get("genre"));
                }
            }

            return s;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read song with id " + songid, ex);
        }
    };

    @Override
    public Song readGivenName(String songname) throws DaoException {
        try (Connection conn = sql2o.open()) {
            //check for duplication
            String sql_no_genre = "SELECT * FROM (SELECT S.songid as SID, * FROM songs as S) as R\n"
                    + "WHERE R.songname=:songname;";

            List<Map<String, Object>> queryResults_no_genre = conn.createQuery(sql_no_genre).addParameter("songname", (String) songname).executeAndFetchTable().asList();

            if (queryResults_no_genre.size() == 0) {
                return null;
            }

            if(queryResults_no_genre.size() >= 2) {
                throw new Sql2oException("Too many songs with the same name already in the database! Possible duplication");
            }

            String sql = "SELECT * FROM (SELECT S.songid as SID, * FROM songs as S) as R\n"
                    + "LEFT JOIN SongGenres as G ON R.SID=G.songid\n"
                    + "WHERE R.songname=:songname;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("songname", (String) songname).executeAndFetchTable().asList();

            String songid = (String) queryResults.get(0).get("songid");
            String artistName = (String) queryResults.get(0).get("artistname");
            String albumName = (String) queryResults.get(0).get("albumname");
            Integer releaseYear = (Integer) queryResults.get(0).get("releaseyear");

            Song s = new Song(songid, songname, artistName, albumName, releaseYear);
            for (Map row : queryResults) {
                if (row.get("genre") != null) {
                    s.addGenre((String) row.get("genre"));
                }
            }
            //System.out.println(s);
            return s;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read song with name " + songname, ex);
        }
    };

    @Override
    public List<Song> readAll() throws DaoException {
        String sql = "SELECT * FROM (SELECT S.songid as SID, * FROM Songs as  S) as R\n" +
                "LEFT JOIN songgenres as G on R.SID=G.songid;";

        try (Connection conn = sql2o.open()) {
            List<Song> songs = this.extractSongsFromDatabase(sql, conn);
            //System.out.println(songs);
            return songs;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from database", ex);
        }
    };

    @Override
    public Song updateSongName(String songid, String songname) throws DaoException {
        String sql = "UPDATE Songs SET songname=:songname WHERE songid=:songid;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("songid", songid).addParameter("songname", songname).executeUpdate();
            return this.read(songid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song name", ex);
        }
    };

    @Override
    public Song updateArtistName(String songid, String artistname) throws DaoException {
        String sql = "UPDATE Songs SET artistname=:artistname WHERE songid=:songid;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("songid", songid).addParameter("artistname", artistname).executeUpdate();
            return this.read(songid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song artist", ex);
        }
    };

    @Override
    public Song updateAlbumName(String songid, String albumname) throws DaoException {
        String sql = "UPDATE Songs SET albumname=:albumname WHERE songid=:songid;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("songid", songid).addParameter("albumname", albumname).executeUpdate();
            return this.read(songid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song album name", ex);
        }
    };

    @Override
    public Song updateReleaseYear(String songId, Integer releaseYear) throws DaoException {
        String sql = "UPDATE Songs SET releaseyear=:releaseyear WHERE songid=:songId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("songId", songId).addParameter("releaseyear", releaseYear).executeUpdate();
            return this.read(songId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song release year", ex);
        }
    };

    @Override
    public Song updateGenres(String songId, Set<String> newGenres) throws DaoException {
        String getCurrentGenresSQL = "SELECT * FROM SongGenres WHERE songid=:songId";
        String deleteGenreSQL = "DELETE FROM SongGenres WHERE songid=:songId AND genre=:genre";
        String insertGenreSQL = "INSERT INTO SongGenres (songid, genre) VALUES (:songId, :genre)";
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

    @Override
    public Song deleteSong(String songId) throws DaoException {
        String deleteSongGenresSQL = "DELETE FROM SongGenres WHERE songid=:songId;";
        String deleteSongSQL = "DELETE FROM Songs WHERE songid=:songId;";
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
            //System.out.println(row);
            // Extract data from this row
            String songId = (String) row.get("songid");
            String songName = (String) row.get("songname");
            String artistName = (String) row.get("artistname");
            String albumName = (String) row.get("albumname");
            Integer releaseYear = (Integer) row.get("releaseyear");
            String genre = (String) row.get("genre");
            if (!alreadyAdded.contains(songId)){
                alreadyAdded.add(songId);
                songs.put(songId, new Song(songId, songName, artistName, albumName, releaseYear, new HashSet<String>()));
            }

            Song s = songs.get(songId);
            s.addGenre(genre);
        }
        System.out.println("Song Extraction successful");
        return new ArrayList<Song>(songs.values());
    }

}
