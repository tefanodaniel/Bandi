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

    @Override
    public SongOfTheWeekSubmission create(String submissionid, String musicianid, String musician_name,
                                          String avsubmission, Set<String> instruments) throws DaoException {
        String sotw_submission_sql = "INSERT INTO sotwsubmissions (submissionid, musicianid, musicianname, avsubmission)" +
                "VALUES (:submissionid, :musicianid, :musicianname, :avsubmission)";
        String sotw_submissions_instruments_sql = "INSERT INTO sotwsubmissionsinstruments (submissionid, instrument) VALUES (:submissionid, :instrument)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sotw_submission_sql)
                    .addParameter("submissionid", submissionid)
                    .addParameter("musicianid", musicianid)
                    .addParameter("musicianname", musician_name)
                    .addParameter("avsubmission", avsubmission)
                    .executeUpdate();

            for (String instrument : instruments) {
                conn.createQuery(sotw_submissions_instruments_sql)
                        .addParameter("submissionid", submissionid)
                        .addParameter("instrument", instrument)
                        .executeUpdate();
            }

            return this.read(submissionid);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    };

    @Override
    public SongOfTheWeekSubmission read(String submissionid) throws DaoException {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM (SELECT S.submissionid as SID, * FROM sotwsubmissions as S) as R\n"
                    + "LEFT JOIN sotwsubmissionsinstruments as G ON R.SID=G.submissionid\n"
                    + "WHERE R.SID=:submissionid;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("submissionid", submissionid).executeAndFetchTable().asList();

            if (queryResults.size() == 0) {
                return null;
            }

            String musicianid = (String) queryResults.get(0).get("musicianid");
            String musicianname = (String) queryResults.get(0).get("musicianname");
            String avsubmission = (String) queryResults.get(0).get("avsubmission");

            SongOfTheWeekSubmission s = new SongOfTheWeekSubmission(submissionid, musicianid, musicianname, avsubmission);
            for (Map row : queryResults) {
                if (row.get("instrument") != null) {
                    s.addInstrument((String) row.get("instrument"));
                }
            }

            return s;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read submission with id " + submissionid, ex);
        }
    };

    @Override
    public List<SongOfTheWeekSubmission> readAll() throws DaoException {
        String sql = "SELECT * FROM (SELECT S.submissionid as SID, * FROM sotwsubmissions as  S) as R\n" +
                "LEFT JOIN sotwsubmissionsinstruments as G on R.SID=G.submissionid;";

        try (Connection conn = sql2o.open()) {
            List<SongOfTheWeekSubmission> submissions = this.extractSubmissionsFromDatabase(sql, conn);
            //System.out.println(submissions);
            return submissions;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read submissions from database", ex);
        }
    };

    @Override
    public SongOfTheWeekSubmission updateAVSubmission(String submissionid, String avsubmission) throws DaoException {
        String sql = "UPDATE sotwsubmissions SET avsubmission=:avsubmission WHERE submissionid=:submissionid;";

        try (Connection conn = sql2o.open()) {
            if(avsubmission == null) throw new Sql2oException("No av link provided");
            conn.createQuery(sql).addParameter("submissionid", submissionid).addParameter("avsubmission", avsubmission).executeUpdate();
            return this.read(submissionid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the av link", ex);
        }
    };

    @Override
    public SongOfTheWeekSubmission updateInstruments(String submissionid, Set<String> newInstruments) throws DaoException {
        String getCurrentInstrumentsSQL = "SELECT * FROM sotwsubmissionsinstruments WHERE submissionid=:submissionid";
        String deleteInstrumentSQL = "DELETE FROM sotwsubmissionsinstruments WHERE submissionid=:submissionid AND instrument=:instrument";
        String insertInstrumentSQL = "INSERT INTO sotwsubmissionsinstruments (submissionid, instrument) VALUES (:submissionid, :instrument)";
        try (Connection conn = sql2o.open()) {
            if(newInstruments == null) throw new Sql2oException("No new instruments provided");
            // Get current instruments stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentInstrumentsSQL).addParameter("submissionid", submissionid).executeAndFetchTable().asList();
            HashSet<String> currentInstruments = new HashSet<String>();
            for (Map row : rows) {
                currentInstruments.add((String) row.get("instrument"));
            }

            // Delete any values currently in the database that aren't in the new set of instruments to store
            for (String instrument : currentInstruments) {
                if (!newInstruments.contains(instrument)) {
                    conn.createQuery(deleteInstrumentSQL).addParameter("submissionid", submissionid).addParameter("instrument", instrument).executeUpdate();
                }
            }

            // Add new instruments to the database, if they aren't already in there
            for (String instrument : newInstruments) {
                if (!currentInstruments.contains(instrument)) {
                    conn.createQuery(insertInstrumentSQL).addParameter("submissionid", submissionid).addParameter("instrument", instrument).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(submissionid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the submission instruments", ex);
        }
    };

    @Override
    public SongOfTheWeekSubmission delete(String submissionid) throws DaoException {
        String deletesubmissioninstrumentsSQL = "DELETE FROM sotwsubmissionsinstruments WHERE submissionid=:submissionid;";
        String deletesubmissionSQL = "DELETE FROM sotwsubmissions WHERE submissionid=:submissionid;";
        try (Connection conn = sql2o.open()) {
            // Get songs to return before we delete
            SongOfTheWeekSubmission submissionToDelete = this.read(submissionid);

            // Delete associated genres
            conn.createQuery(deletesubmissioninstrumentsSQL).addParameter("submissionid", submissionid).executeUpdate();

            // Delete musician
            conn.createQuery(deletesubmissionSQL).addParameter("submissionid", submissionid).executeUpdate();


            return submissionToDelete;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the submission", ex);
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
            String submissionid = (String) row.get("submissionid");
            String musicianid = (String) row.get("musicianid");
            String musicianname = (String) row.get("musicianname");
            String avsubmission = (String) row.get("avsubmission");
            String instrument = (String) row.get("instrument");
            if (!alreadyAdded.contains(submissionid)){
                alreadyAdded.add(submissionid);
                submissions.put(submissionid, new SongOfTheWeekSubmission(submissionid, musicianid, musicianname, avsubmission, new HashSet<String>()));
            }

            SongOfTheWeekSubmission s = submissions.get(submissionid);
            s.addInstrument(instrument);
        }
        System.out.println("SOTW Submission Extraction successful");
        return new ArrayList<SongOfTheWeekSubmission>(submissions.values());
    }
}
