package api;

import com.google.gson.JsonSyntaxException;
import exceptions.ApiError;
import exceptions.DaoException;
import model.SongOfTheWeekEvent;
import model.SongOfTheWeekSubmission;
import spark.Route;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // post (create) a sotw event
    public static Route postSOTWEvent = (req, res) -> {
        try {
            SongOfTheWeekEvent event = gson.fromJson(req.body(), SongOfTheWeekEvent.class);
            Set<String> submissions = event.getSubmissions();

            if (submissions == null) { submissions = new HashSet<String>(); }
            sotw_eventDao.create(event.getEventId(), event.getAdminId(), event.getStart_week(), event.getEnd_week(), event.getSongId(), submissions);
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

            String start_week = event.getStart_week();
            String end_week = event.getEnd_week();
            String songId = event.getSongId();

            // Update specific fields:
            boolean flag = false;
            if (start_week != null) {
                flag = true;
                event = sotw_eventDao.updateStartWeek(eventid, start_week);
            } if (end_week != null) {
                flag = true;
                event = sotw_eventDao.updateEndWeek(eventid, end_week);
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
