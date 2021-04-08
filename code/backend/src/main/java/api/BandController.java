package api;

import com.google.gson.JsonSyntaxException;
import exceptions.ApiError;
import exceptions.DaoException;
import model.Band;
import model.Musician;
import spark.Route;

import java.util.*;

import static api.ApiServer.musicianDao;
import static api.ApiServer.bandDao;
import static api.ApiServer.gson;

public class BandController {

    // Get all bands (optional query parameters)
    // if searching for id, only pass 1 parameter
    public static Route getAllBands = (req, res) -> {
        try {
            List<Band> bands;
            Map<String, String[]> query = req.queryMap().toMap();

            if (query.get("musicianId") != null) {
                bands = bandDao.readAll(query.get("musicianId")[0]);
            } else if (query.size() > 0) {
                bands = bandDao.readAll(query);
            } else {
                bands = bandDao.readAll();
            }
            return gson.toJson(bands);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // Get Band given the id
    public static Route getBandById = (req, res) -> {
        try {
            String id = req.params("id");
            Band band = bandDao.read(id);
            if (band == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(band);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // post a Band
    public static Route postBand = (req, res) -> {
        try {
            Band band = gson.fromJson(req.body(), Band.class);

            String id = UUID.randomUUID().toString();
            Set<String> members = band.getMembers();
            Set<String> genres = band.getGenres();
            String name = band.getName();
            int capacity = band.getCapacity();

            if (members == null) { members = new HashSet<String>(); }
            if (genres == null) { genres = new HashSet<String>(); }
            if (name == null) { name = "NULL"; }
            if (capacity == 0) { capacity = 1; }

            bandDao.create(id, name, capacity, genres, members);

            res.status(201);
            return gson.toJson(band);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // put a member to a Band
    public static Route putBandMember = (req, res) -> {
        try {
            String bandId = req.params("bid");
            String musicianId = req.params("mid");
            Band band = bandDao.read(bandId);
            Musician musician = musicianDao.read(musicianId);
            if (band == null) {
                throw new ApiError("Resource not found", 404);
            }
            if (musician == null) {
                throw new ApiError("Resource not found", 404);
            }
            if (!(musician.getId()).equals(musicianId)) {
                throw new ApiError("musician ID does not match the resource identifier", 400);
            }
            if (!(band.getId()).equals(bandId)) {
                throw new ApiError("band ID does not match the resource identifier", 400);
            }

            Band b = bandDao.add(bandId, musicianId);
            if (b == null) {
                throw new ApiError("Resource not found", 404);
            }

            return gson.toJson(b);
        } catch (DaoException | JsonSyntaxException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // remove member from Band
    public static Route deleteMember = (req, res) -> {
        try {
            String bandId = req.params("bid");
            String musicianId = req.params("mid");
            Band band = bandDao.remove(bandId, musicianId);
            if (band == null) {
                throw new ApiError("Resource not found", 404);
            }

            res.type("application/json");
            return gson.toJson(band);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

}
