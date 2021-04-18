package api;

import com.google.gson.JsonSyntaxException;
import exceptions.ApiError;
import exceptions.DaoException;
import model.SongOfTheWeekSubmission;
import spark.Route;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static api.ApiServer.sotw_submissionDao;
import static api.ApiServer.gson;

public class SOTWSubmissionController {

    public static Route getAllSubmissions = (req, res) -> {
        try {
            List<SongOfTheWeekSubmission> submissions = sotw_submissionDao.readAll();
            System.out.println(submissions);
            return gson.toJson(submissions);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get (read) a submission given submissionId
    public static Route getSubmissionById = (req, res) -> {
        try {
            String submissionid = req.params("submissionid");
            SongOfTheWeekSubmission s = sotw_submissionDao.read(submissionid);
            if (s == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(s);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // post (create) a submission from a user with musician_id
    public static Route postSubmission = (req, res) -> {
        try {
            SongOfTheWeekSubmission submission = gson.fromJson(req.body(), SongOfTheWeekSubmission.class);
            Set<String> instruments = submission.getInstruments();

            if (instruments == null) { instruments = new HashSet<String>(); }
            sotw_submissionDao.create(submission.getSubmission_id(), submission.getMusician_id(), submission.getMusician_name(), submission.getAVSubmission(), instruments);
            res.status(201);
            return gson.toJson(submission);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // put (update) a submission with info
    public static Route putSubmission = (req, res) -> {
        try {

            String submissionid = req.params("submissionid");
            SongOfTheWeekSubmission submission = gson.fromJson(req.body(), SongOfTheWeekSubmission.class);
            if (submission == null) {
                throw new ApiError("Resource not found", 404);
            }

            if (! (submission.getSubmission_id().equals(submissionid))) {
                throw new ApiError("submission ID does not match the resource identifier", 400);
            }

            String avsubmission = submission.getAVSubmission();
            Set<String> instruments = submission.getInstruments();

            // Update specific fields:
            boolean flag = false;
            if (avsubmission != null) {
                flag = true;
                submission = sotw_submissionDao.updateAVSubmission(submissionid, avsubmission);
            } if (instruments != null) {
                flag = true;
                submission = sotw_submissionDao.updateInstruments(submissionid, instruments);
            } if (!flag) {
                throw new ApiError("Nothing to update", 400);
            } if (submission == null) {
                throw new ApiError("Resource not found", 404);
            }

            return gson.toJson(submission);
        } catch (DaoException | JsonSyntaxException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // delete (remove) a submission
    public static Route deleteSubmission = (req, res) -> {
        try {
            String submissionid = req.params("submissionid");
            SongOfTheWeekSubmission submission = sotw_submissionDao.delete(submissionid);
            if (submission == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(submission);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };
}
