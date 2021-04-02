package dao;

import exceptions.DaoException;
import model.SongOfTheWeekSubmission;
import org.sql2o.Sql2o;

import java.util.Set;

public class Sql2oSotwSubmissionDao implements SotwSubmissionDao {
    private final Sql2o sql2o;

    public Sql2oSotwSubmissionDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public SongOfTheWeekSubmission create(String submissionId, String musicianId,
                                          String avSubmission, Set<String> instruments) throws DaoException {
        return null;
    };

    public SongOfTheWeekSubmission read(String submissionId) throws DaoException {
        return null;
    };

    public SongOfTheWeekSubmission readAll() throws DaoException {
        return null;
    };

    public SongOfTheWeekSubmission updateAVSubmission(String submissionId, String avSubmission) throws DaoException {
        return null;
    };

    public SongOfTheWeekSubmission updateInstruments(String submissionId, Set<String> instruments) throws DaoException {
        return null;
    };

    public SongOfTheWeekSubmission delete(String submissionId) throws DaoException {
        return null;
    };
}
