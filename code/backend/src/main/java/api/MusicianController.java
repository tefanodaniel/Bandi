package api;

import com.google.gson.JsonSyntaxException;
import exceptions.ApiError;
import exceptions.DaoException;
import model.Musician;
import spark.Route;

import java.util.*;

import static spark.Spark.*;

import static api.ApiServer.musicianDao;
import static api.ApiServer.gson;

public class MusicianController {

    // Get all musicians (with optional query parameters)
    public static Route getAllMusicians =  (req, res) -> {
        try {
            List<Musician> musicians;
            Map<String, String[]> query = req.queryMap().toMap();
            if(query.size() > 0) {
                musicians = musicianDao.readAll(query);
            }
            else {
                musicians = musicianDao.readAll();
            }
            return gson.toJson(musicians);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // Get Musician given the id
    public static Route getMusicianById = (req, res) -> {
        try {
            String id = req.params("id");
            Musician musician = musicianDao.read(id);
            if (musician == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(musician);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // Post a Musician
    public static Route postMusician = (req, res) -> {
        try {
            Musician musician = gson.fromJson(req.body(), Musician.class);
            Set<String> instruments = musician.getInstruments();
            Set<String> genres = musician.getGenres();
            String experience = musician.getExperience();
            String location = musician.getLocation();
            String zipCode = musician.getZipCode();
            Set<String> profileLinks = musician.getProfileLinks();
            Set<String> friends = musician.getFriends();
            boolean admin = musician.getAdmin();

            if (instruments == null) { instruments = new HashSet<String>(); }
            if (genres == null) { genres = new HashSet<String>(); }
            if (experience == null) { experience = "NULL"; }
            if (location == null) { location = "NULL"; }
            if (zipCode == null) { zipCode = "NULL"; }
            if (profileLinks == null) { profileLinks = new HashSet<String>(); }
            if (friends == null) { friends = new HashSet<String>(); }
            musicianDao.create(musician.getId(), musician.getName(), genres,
                    instruments, experience, location, zipCode, profileLinks, friends, admin);
            res.status(201);
            return gson.toJson(musician);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // Put a Musician
    public static Route putMusician = (req, res) -> {

        try {

            String id = req.params("id");
            Musician musician = gson.fromJson(req.body(), Musician.class);
            if (musician == null) {
                throw new ApiError("Resource not found", 404);
            }

            if (! (musician.getId()).equals(id)) {
                throw new ApiError("musician ID does not match the resource identifier", 400);
            }

            String name = musician.getName();
            Set<String> genres = musician.getGenres();
            Set<String> instruments = musician.getInstruments();
            String experience = musician.getExperience();
            String location = musician.getLocation();
            String zipCode = musician.getZipCode();
            Set<String> profileLinks = musician.getProfileLinks();

            // no check for admin flag. We don't want to change admin on and off,
            // and since ints default to 0, we might accidentally take admin
            // permissions away.

            // Update specific fields:
            boolean flag = false;
            if (name != null) {
                flag = true;
                musician = musicianDao.updateName(id, name);
            } if (instruments != null) {
                flag = true;
                musician = musicianDao.updateInstruments(id, instruments);
            } if (genres != null) {
                flag = true;
                musician = musicianDao.updateGenres(id, genres);
            } if (experience != null) {
                flag = true;
                musician = musicianDao.updateExperience(id, experience);
            } if (location != null) {
                flag = true;
                musician = musicianDao.updateLocation(id, location);
            } if (zipCode != null) {
                flag = true;
                musician = musicianDao.updateZipCode(id, zipCode);
            } if (profileLinks != null) {
                flag = true;
                musician = musicianDao.updateProfileLinks(id, profileLinks);
            } if (!flag) {
                throw new ApiError("Nothing to update", 400);
            } if (musician == null) {
                throw new ApiError("Resource not found", 404);
            }

            return gson.toJson(musician);
        } catch (DaoException | JsonSyntaxException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // Delete Musician by id
    public static Route deleteMusician = (req, res) -> {
        try {
            String id = req.params("id");
            Musician musician = musicianDao.delete(id);
            if (musician == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(musician);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get the admin status of a Musician
    public static Route getAdminStatus = (req, res) -> {
        try {
            String id = req.params("id");
            Musician musician = musicianDao.read(id);
            if (musician == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            boolean isAdmin = musician.getAdmin();
            res.type("application/json");
            return "{\"isAdmin\":"+ isAdmin +"}";
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // update the admin status of a Musician
    public static Route putAdminStatus = (req, res) -> {
        try {
            String id = req.params("id");
            Map map = gson.fromJson(req.body(), HashMap.class);
            boolean isAdmin = (boolean) map.get("isAdmin");
            Musician m = musicianDao.updateAdmin(id, isAdmin);
            res.type("application/json");
            return gson.toJson(m);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get showtoptracks boolean of a Musician
    public static Route getShowTopTracks = (req, res) -> {
        try {
            String id = req.params("id");
            Musician musician = musicianDao.read(id);
            if (musician == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            boolean showtoptracks = musician.getShowtoptracks();
            res.type("application/json");
            return "{\"showtoptracks\":"+ showtoptracks +"}";
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // update showtoptracks boolean of a Musician
    public static Route putShowTopTracks = (req, res) -> {
        try {
            String id = req.params("id");
            Map map = gson.fromJson(req.body(), HashMap.class);
            boolean showtoptracks = (boolean) map.get("showtoptracks");
            Musician m = musicianDao.updateShowtoptracks(id, showtoptracks);
            res.type("application/json");
            return gson.toJson(m);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };
}
