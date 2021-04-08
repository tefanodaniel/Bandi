package api;

import com.google.gson.JsonSyntaxException;
import exceptions.ApiError;
import exceptions.DaoException;
import model.Musician;
import model.SpeedDateEvent;
import spark.Route;

import java.util.*;

import static api.ApiServer.*;

public class SpeedDateController {

    // Get all SpeedDateEvents
    public static Route getAllSpeedDateEvents = (req, res) -> {
        try {
            List<SpeedDateEvent> events;
            Map<String, String[]> query = req.queryMap().toMap();
            events = speedDateEventDao.readAll();
            return gson.toJson(events);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // Get SpeedDateEvent given the id
    public static Route getSpeedDateEventById = (req, res) -> {
        try {
            String id = req.params("id");
            SpeedDateEvent event = speedDateEventDao.read(id);
            if (event == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(event);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // put new SpeedDateEvent participant
    public static Route putSpeedDateEventParticipant = (req, res) -> {
        try {
            String eventId = req.params("eid");
            String musicianId = req.params("mid");
            SpeedDateEvent event = speedDateEventDao.read(eventId);
            Musician musician = musicianDao.read(musicianId);
            if (event == null || musician == null) {
                throw new ApiError("Resource not found", 404);
            }

            SpeedDateEvent e = speedDateEventDao.add(eventId, musicianId);
            if (e == null) {
                throw new ApiError("Failed to add participant", 404);
            }

            return gson.toJson(e);
        } catch (DaoException | JsonSyntaxException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // remove SpeedDateEvent participant from event
    public static Route deleteSpeedDateEventParticipant = (req, res) -> {
        try {
            String eventId = req.params("eid");
            String musicianId = req.params("mid");
            SpeedDateEvent event = speedDateEventDao.remove(eventId, musicianId);
            if (event == null) {
                throw new ApiError("Resource not found", 404);
            }

            res.type("application/json");
            return gson.toJson(event);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // Post a SpeedDateEvent
    public static Route postSpeedDateEvent = (req, res) -> {
        try {
            SpeedDateEvent event = gson.fromJson(req.body(), SpeedDateEvent.class);

            String id = UUID.randomUUID().toString();
            String name = event.getName();
            String link = event.getLink();
            String date = event.getDate();
            int minusers = event.getMinusers();
            Set<String> participants = event.getParticipants();

            if (name == null) { name = "NULL"; }
            if (link == null) { link = "NULL"; }
            if (date == null) { date = "NULL"; }
            if (minusers == 0) { minusers = 1; }
            if (participants == null) {participants = new HashSet<>();}

            speedDateEventDao.create(id, name, link, date, minusers, participants);

            res.status(201);
            return gson.toJson(event);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

}
