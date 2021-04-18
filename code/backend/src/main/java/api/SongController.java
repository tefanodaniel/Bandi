package api;

import exceptions.ApiError;
import exceptions.DaoException;
import model.Song;
import spark.Route;

import java.io.*;
import java.util.*;

import static api.ApiServer.songDao;
import static api.ApiServer.gson;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.csv.*;

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

    // utility functions for fetching clean csv data from shazam charts
    public static int count_quotes(String line) {
        int quotes = 0;
        for(int i = 0; i < line.length(); i++) {
            if(line.charAt(i) == '"') quotes++;
        }

        return quotes;
    }

    public static int count_commas(String line) {
        int commas = 0;
        for(int i = 0; i < line.length(); i++) {
            if(line.charAt(i) == ',') commas++;
        }

        return commas;
    }

    // post a song with given genre
    public static Route generateSongByGenre = (req, res) -> {
        try {
            String genre = req.params("genre");

            StringBuffer stringBuffer = new StringBuffer();
            byte[] data = null;
            Map<?, ?> randomElement = null;

            try {
                URL url = new URL("https://www.shazam.com/services/charts/csv/genre/world/" + genre);
                HttpURLConnection connection2 = (HttpURLConnection) url.openConnection();

                System.out.println("About to fetch a new song :)");

                InputStream inputStream2 = connection2.getInputStream();

                BufferedReader reader2 = new BufferedReader(new InputStreamReader(inputStream2));

                String line = null;
                List<String> csvlines = new ArrayList<>();
                int csv_line_count = 0;
                while ((line = reader2.readLine()) != null) {
                    if(line != null) {
                        csv_line_count++;
                        if(csv_line_count > 1) {
                            int commas = count_commas(line);
                            int quotes = count_quotes(line);
                            if((commas == 2) && (quotes == 0)){
                                csvlines.add(line);
                            }
                            else if((commas == 2) && (quotes == 4)) {
                                csvlines.add(line);
                            }
                        }
                    }
                }

                //System.out.println("Finished cleanup");

                for(int t = 0; t < csvlines.size(); t++) {
                    String temp_line = csvlines.get(t);
                    //System.out.println("Processing new line : " + temp_line);
                    for(int i = 0; i < temp_line.length(); i++) {
                        char c = temp_line.charAt(i);
                        stringBuffer.append(String.valueOf(c));
                    }
                    stringBuffer.append("\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            finally {
                //System.out.println("\nPrinting string buffer\n");
                //System.out.println(stringBuffer);
                data = stringBuffer.toString().getBytes();
            }

            CsvSchema csv = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            MappingIterator<Map<?, ?>> mappingIterator =  csvMapper.reader().forType(Map.class).with(csv).readValues(data);

            List<Map<?, ?>> list  = mappingIterator.readAll();
            //for(int i = 0; i < list.size(); i++) {
            //    System.out.println(list.get(i));
            //}

            Integer filter_size = list.size();
            if (filter_size/5 > 1) {
                filter_size = filter_size/5;
            }

            Boolean new_song = false;
            Integer failed_attempts = 0;

            System.out.println("finding a new random top 50 song\n");

            while(!new_song){
                Random rand = new Random();
                randomElement = list.get(rand.nextInt(filter_size));
                Song s = songDao.readGivenName((String) randomElement.get("Title"));
                if(s == null){
                    new_song = true;
                }
                else {
                   failed_attempts++;
                   if(failed_attempts > filter_size) {
                       filter_size = list.size();
                   }
                }
            }
            System.out.println(randomElement);
            String artistName = (String) randomElement.get("Artist");
            String songName = (String) randomElement.get("Title");
            String songId = UUID.randomUUID().toString();
            Song song = new Song(songId, songName, artistName);
            song.addGenre(genre);
            songDao.create(song.getSongId(), song.getSongName(), song.getArtistName(), song.getAlbumName(), song.getReleaseYear(), song.getGenres());
            res.status(201);
            return gson.toJson(song);
        } catch (Exception ex) {
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
