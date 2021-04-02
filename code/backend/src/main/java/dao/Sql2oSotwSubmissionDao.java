package dao;

import exceptions.DaoException;
import model.Song;
import model.SongOfTheWeekSubmission;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.*;

public class Sql2oSotwSubmissionDao implements SotwSubmissionDao {
    private final Sql2o sql2o;

    public Sql2oSotwSubmissionDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public SongOfTheWeekSubmission create(String submissionId, String musicianId,
                                          String avSubmission, Set<String> instruments) throws DaoException {
        String sotw_submission_sql = "INSERT INTO sotwsubmissions (submissionId, musicianId, avSubmission)" +
                "VALUES (:submissionId, :musicianId, :avSubmission)";
        String sotw_submissions_instruments_sql = "INSERT INTO sotwsubmissionsinstruments (submissionId, instrument) VALUES (:submissionId, :instrument)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sotw_submission_sql)
                    .addParameter("submissionId", submissionId)
                    .addParameter("musicianId", musicianId)
                    .addParameter("avSubmission", avSubmission)
                    .executeUpdate();

            for (String instrument : instruments) {
                conn.createQuery(sotw_submissions_instruments_sql)
                        .addParameter("submissionId", submissionId)
                        .addParameter("instrument", instrument)
                        .executeUpdate();
            }

            return this.read(submissionId);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    };

    public SongOfTheWeekSubmission read(String submissionId) throws DaoException {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM (SELECT S.submissionId as SID, * FROM sotwsubmissions as S) as R\n"
                    + "LEFT JOIN sotwsubmissionsinstruments as G ON R.SID=G.submissionId\n"
                    + "WHERE R.SID=:submissionId;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("submissionId", submissionId).executeAndFetchTable().asList();

            if (queryResults.size() == 0) {
                return null;
            }

            String musicianId = (String) queryResults.get(0).get("musicianId");
            String avSubmission = (String) queryResults.get(0).get("avSubmission");

            SongOfTheWeekSubmission s = new SongOfTheWeekSubmission(submissionId, musicianId, avSubmission);
            for (Map row : queryResults) {
                if (row.get("instrument") != null) {
                    s.addInstrument((String) row.get("instrument"));
                }
            }

            return s;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read song with id " + submissionId, ex);
        }
    };

    public List<SongOfTheWeekSubmission> readAll() throws DaoException {
        String sql = "SELECT * FROM (SELECT S.submissionId as SID, * FROM sotwsubmissions as  S) as R\n" +
                "LEFT JOIN sotwsubmisssionsinstruments as G on R.SID=G.submissionId;";

        try (Connection conn = sql2o.open()) {
            List<SongOfTheWeekSubmission> submissions = this.extractSubmissionsFromDatabase(sql, conn);
            System.out.println(submissions);
            return submissions;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read submissions from database", ex);
        }
    };

    public SongOfTheWeekSubmission updateAVSubmission(String submissionId, String avSubmission) throws DaoException {
        String sql = "UPDATE sotwsubmissions SET avSubmission=:avSubmission WHERE submissionId=:submissionId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("submissionId", submissionId).addParameter("avSubmission", avSubmission).executeUpdate();
            return this.read(submissionId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the av link", ex);
        }
    };

    public SongOfTheWeekSubmission updateInstruments(String submissionId, Set<String> newInstruments) throws DaoException {
        String getCurrentInstrumentsSQL = "SELECT * FROM sotwsubmissionsinstruments WHERE submissionId=:submissionId";
        String deleteInstrumentSQL = "DELETE FROM sotwsubmissionsinstruments WHERE submissionId=:submissionId AND instrument=:instrument";
        String insertInstrumentSQL = "INSERT INTO sotwsubmissionsinstruments (submissionId, instrument) VALUES (:submissionId, :instrument)";
        try (Connection conn = sql2o.open()) {
            // Get current instruments stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentInstrumentsSQL).addParameter("submissionId", submissionId).executeAndFetchTable().asList();
            HashSet<String> currentInstruments = new HashSet<String>();
            for (Map row : rows) {
                currentInstruments.add((String) row.get("instrument"));
            }

            // Delete any values currently in the database that aren't in the new set of instruments to store
            for (String instrument : currentInstruments) {
                if (!newInstruments.contains(instrument)) {
                    conn.createQuery(deleteInstrumentSQL).addParameter("submissionId", submissionId).addParameter("instrument", instrument).executeUpdate();
                }
            }

            // Add new instruments to the database, if they aren't already in there
            for (String instrument : newInstruments) {
                if (!currentInstruments.contains(instrument)) {
                    conn.createQuery(insertInstrumentSQL).addParameter("submissionId", submissionId).addParameter("instrument", instrument).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(submissionId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song genre", ex);
        }
    };

    public SongOfTheWeekSubmission delete(String submissionId) throws DaoException {
        String deletesubmissioninstrumentsSQL = "DELETE FROM sotwsubmissionsinstruments WHERE submissionId=:submissionId;";
        String deletesubmissionSQL = "DELETE FROM sotwsubmissions WHERE submissionId=:submissionId;";
        try (Connection conn = sql2o.open()) {
            // Get songs to return before we delete
            SongOfTheWeekSubmission submissionToDelete = this.read(submissionId);

            // Delete associated genres
            conn.createQuery(deletesubmissioninstrumentsSQL).addParameter("submissionId", submissionId).executeUpdate();

            // Delete musician
            conn.createQuery(deletesubmissionSQL).addParameter("submissionId", submissionId).executeUpdate();


            return submissionToDelete;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the musician", ex);
        }

    };

    /* *
     * Query the database and parse the results to create a list of submissions such that each one has the proper list attributes
     *  This is necessary because we store our database in normalized form.
     * @param sql The SQL query string
     * @return the list of songs
     * @throws Sql2oException if query fails
     */
    private List<SongOfTheWeekSubmission> extractSubmissionsFromDatabase(String sql, Connection conn) throws Sql2oException {
        List<Map<String, Object>> queryResults = conn.createQuery(sql).executeAndFetchTable().asList();
        HashSet<String> alreadyAdded = new HashSet<String>();
        Map<String, SongOfTheWeekSubmission> submissions = new HashMap<String, SongOfTheWeekSubmission>();
        for (Map row : queryResults) {
            // Extract data from this row
            String submissionId = (String) row.get("submissionId");
            String musicianId = (String) row.get("musicianId");
            String avSubmission = (String) row.get("avSubmission");
            String instrument = (String) row.get("instrument");
            if (!alreadyAdded.contains(submissionId)){
                alreadyAdded.add(submissionId);
                submissions.put(submissionId, new SongOfTheWeekSubmission(submissionId, musicianId, avSubmission, new HashSet<String>()));
            }

            SongOfTheWeekSubmission s = submissions.get(submissionId);
            s.addInstrument(instrument);
        }
        System.out.println("SOTW Submission Extraction successful");
        return new ArrayList<SongOfTheWeekSubmission>(submissions.values());
    }
}
