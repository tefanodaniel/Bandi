package api;

import exceptions.ApiError;
import exceptions.DaoException;
import model.Musician;
import spark.Route;
import java.util.List;
import java.util.Map;
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
}
