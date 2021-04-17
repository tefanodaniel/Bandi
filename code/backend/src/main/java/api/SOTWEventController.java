package api;

import com.google.gson.JsonSyntaxException;
import exceptions.ApiError;
import exceptions.DaoException;
import model.Musician;
import model.SongOfTheWeekEvent;
import model.SongOfTheWeekSubmission;
import spark.Route;

import java.util.*;

import static api.ApiServer.*;

public class SOTWEventController {

    // get (read) all sotw events
    public static Route getAllSOTWEvents = (req, res) -> {
        try {
            List<SongOfTheWeekEvent> events = sotw_eventDao.readAll();
            return gson.toJson(events);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get (read) a sotw event given event id
    public static Route getSOTWEventById = (req, res) -> {
        try {
            String eventid = req.params("eventid");
            SongOfTheWeekEvent s = sotw_eventDao.read(eventid);
            if (s == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(s);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get (read) a sotw event given genre, start day and end day
    public static Route findSOTWEventByDesc = (req, res) -> {
        try {
            SongOfTheWeekEvent event;
            String genre = req.params("genre");
            String startday = req.params("startday");
            String endday = req.params("endday");
            System.out.println("Attempting to find event for genre : "+ genre);

            if((genre != null) && (startday != null) && (endday != null)) {
                event = sotw_eventDao.findEvent(startday, endday, genre);
            }
            else {
                throw new ApiError("Bad request insufficient query parameters", 404);
            }
            res.type("application/json");


            if (event == null) {
                return gson.toJson(null);
//                throw new ApiError("Resource not found for parameters, genre : " + genre + " start day :  " + startday
//                        + " end day : " + endday, 404); // Bad request
            }
            System.out.println("Found an event :" + event);
            return gson.toJson(event);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // post (create) a sotw event
    public static Route postSOTWEvent = (req, res) -> {
        try {
            SongOfTheWeekEvent event = gson.fromJson(req.body(), SongOfTheWeekEvent.class);
            System.out.println(event);
            Set<String> submissions = event.getSubmissions();

            if (submissions == null) { submissions = new HashSet<String>(); }
            //check if an event already exists for this genre, startday, endday
            SongOfTheWeekEvent check = sotw_eventDao.findEvent(event.getStartDay(), event.getEndDay(), event.getGenre());
            if(check != null){
                return null;
            }

            sotw_eventDao.create(event.getEventId(), event.getAdminId(), event.getStartDay(), event.getEndDay(), event.getSongId(), event.getGenre(), submissions);
            res.status(201);
            return gson.toJson(event);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // put (update) an sotw event
    public static Route putSOTWEvent = (req, res) -> {
        // doesn't include adding or removing submissions
        try {

            String eventid = req.params("eventid");
            SongOfTheWeekEvent event = gson.fromJson(req.body(), SongOfTheWeekEvent.class);
            if (event == null) {
                throw new ApiError("Resource not found", 404);
            }

            if (! (event.getEventId().equals(eventid))) {
                throw new ApiError("event ID does not match the resource identifier", 400);
            }

            String startDay = event.getStartDay();
            String endDay = event.getEndDay();
            String songId = event.getSongId();

            // Update specific fields:
            boolean flag = false;
            if (startDay != null) {
                flag = true;
                event = sotw_eventDao.updateStartDay(eventid, startDay);
            } if (endDay != null) {
                flag = true;
                event = sotw_eventDao.updateEndDay(eventid, endDay);
            } if (songId != null) {
                flag = true;
                event = sotw_eventDao.updateSong(eventid, songId);
            } if (!flag) {
                throw new ApiError("Nothing to update", 400);
            } if (event == null) {
                throw new ApiError("Resource not found", 404);
            }

            return gson.toJson(event);
        } catch (DaoException | JsonSyntaxException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get (read All) submissions given sotw event id
    public static Route getSOTWEventSubmissions = (req, res) -> {
        try {
            String eventid = req.params("eventid");
            Set<String> submission_ids = sotw_eventDao.readAllSubmissionsGivenEvent(eventid);
            List<SongOfTheWeekSubmission> submissions = new ArrayList<SongOfTheWeekSubmission>();
            for (String sid : submission_ids) {
                submissions.add(sotw_submissionDao.read(sid));
            }
            return gson.toJson(submissions);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // put (add) a submission to an sotw event
    public static Route putSubmissionToSOTWEvent = (req, res) -> {
        try {
            String eventid = req.params("eventid");
            String submissionid = req.params("submissionid");
            SongOfTheWeekEvent event = sotw_eventDao.read(eventid);
            SongOfTheWeekSubmission submission = sotw_submissionDao.read(submissionid);
            if (event == null){
                throw new ApiError("Event Resource not found", 404);
            }
            if (submission == null) {
                throw new ApiError("Submission Resource not found", 404);
            }

            /** is this needed?
             if (!event.getEventId().equals(eventId)) {
             throw new ApiError("Event ID does not match the resource identifier", 400);
             }

             if (!submission.getSubmission_id().equals(submissionId)) {
             throw new ApiError("Submission ID does not match the resource identifier", 400);
             }*/

            event = sotw_eventDao.addSubmissionToEvent(eventid, submissionid);
            if (event == null) {
                throw new ApiError("Updated Event resource not found", 404);
            }
            return gson.toJson(event);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // delete (remove) a submission from an sotw event
    public static Route deleteSubmissionFromSOTWEvent = (req, res) -> {
        try {
            String eventid = req.params("eventid");
            String submissionid = req.params("submissionid");
            SongOfTheWeekEvent event = sotw_eventDao.read(eventid);
            SongOfTheWeekSubmission submission = sotw_submissionDao.read(submissionid);
            if (event == null){
                throw new ApiError("Event Resource not found", 404);
            }
            if (submission == null) {
                throw new ApiError("Submission Resource not found", 404);
            }

            event = sotw_eventDao.removeSubmissionFromEvent(eventid, submissionid);
            if (event == null) {
                throw new ApiError("Updated Event Resource not found", 404);
            }

            res.type("application/json");
            return gson.toJson(event);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // delete a sotw event
    public static Route deleteSOTWEvent = (req, res) -> {
        try {
            String eventid = req.params("eventid");
            SongOfTheWeekEvent event = sotw_eventDao.deleteEvent(eventid);
            if (event == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(event);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };
}
