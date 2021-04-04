package dao;
import exceptions.DaoException;
import model.Song;
import model.SongOfTheWeekEvent;
import model.SongOfTheWeekSubmission;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.*;

public class Sql2oSotwEventDao implements SotwEventDao {
    private final Sql2o sql2o;

    public Sql2oSotwEventDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<SongOfTheWeekEvent> extractSOTWEventsFromDatabase(String sql, Connection conn) throws Sql2oException {
        List<Map<String, Object>> queryResults = conn.createQuery(sql).executeAndFetchTable().asList();
        HashSet<String> alreadyAdded = new HashSet<String>();
        Map<String, SongOfTheWeekEvent> sotwEvents = new HashMap<String, SongOfTheWeekEvent>();

        for (Map row : queryResults) {
            String eventid = (String) row.get("eventid");
            String adminid = (String) row.get("adminid");
            String start_week = (String) row.get("start_week");
            String end_week = (String) row.get("end_week");
            String songid = (String) row.get("songid");
            String submission = (String) row.get("submission");

            if (!alreadyAdded.contains(eventid)) {
                alreadyAdded.add(eventid);
                sotwEvents.put(eventid, new SongOfTheWeekEvent(eventid, adminid, start_week, end_week, songid));
            }

            SongOfTheWeekEvent e = sotwEvents.get(eventid);
            e.addSubmissions(submission);

        }
        System.out.println("SOTW Event Extraction successful");
        return new ArrayList<SongOfTheWeekEvent>(sotwEvents.values());
    }

    @Override
    public SongOfTheWeekEvent create(String eventid, String adminid, String start_week, String end_week, String songid) throws DaoException {
        String sotw_event_sql = "INSERT INTO sotwevents (eventid, adminid, start_week, end_week, songid)" +
                "VALUES (:eventid, :adminid, :start_week, :end_week, :songid)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sotw_event_sql)
                    .addParameter("eventid", eventid)
                    .addParameter("adminid", adminid)
                    .addParameter("start_week", start_week)
                    .addParameter("end_week", end_week)
                    .addParameter("songid", songid)
                    .executeUpdate();

            return this.read(eventid);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    };



    @Override
    public SongOfTheWeekEvent create(String eventid, String adminid, String start_week, String end_week, String songid, Set<String> submissions) throws DaoException {
        String sotw_event_sql = "INSERT INTO sotwevents (eventid, adminid, start_week, end_week, songid)" +
                "VALUES (:eventid, :adminid, :start_week, :end_week, :songid)";
        String sotw_event_submissions_sql = "INSERT INTO sotweventssubmissions (eventid, submission) VALUES (:eventid, :submission)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sotw_event_sql)
                    .addParameter("eventid", eventid)
                    .addParameter("adminid", adminid)
                    .addParameter("start_week", start_week)
                    .addParameter("end_week", end_week)
                    .addParameter("songid", songid)
                    .executeUpdate();

            for (String submissionId : submissions) {
                conn.createQuery(sotw_event_submissions_sql)
                        .addParameter("eventid", eventid)
                        .addParameter("submission", submissions)
                        .executeUpdate();
            }

            return this.read(eventid);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    };

    @Override
    public SongOfTheWeekEvent read(String eventid) throws DaoException {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM (SELECT S.eventid as EID, * FROM sotwevents as S) as R\n"
                    + "LEFT JOIN sotweventssubmissions as G ON R.EID=G.eventid\n"
                    + "WHERE R.EID=:eventid;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("eventid", eventid).executeAndFetchTable().asList();

            if (queryResults.size() == 0) {
                return null;
            }

            String adminid = (String) queryResults.get(0).get("adminid");
            String start_week = (String) queryResults.get(0).get("start_week");
            String end_week = (String) queryResults.get(0).get("end_week");
            String songid = (String) queryResults.get(0).get("songid");

            SongOfTheWeekEvent e = new SongOfTheWeekEvent(eventid, adminid, start_week, end_week, songid);
            for (Map row : queryResults) {
                if (row.get("submission") != null) {
                    e.addSubmissions((String) row.get("submission"));
                }
            }

            return e;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read event with id " + eventid, ex);
        }
    };

    @Override
    public List<SongOfTheWeekEvent> readAll() throws DaoException {
        String sql = "SELECT * FROM (SELECT S.eventid as EID, * FROM sotwevents as S) as R\n"
                + "LEFT JOIN sotweventssubmissions as G ON R.EID=G.eventid;";

        try (Connection conn = sql2o.open()) {
            List<SongOfTheWeekEvent> events = this.extractSOTWEventsFromDatabase(sql, conn);
            System.out.println(events);
            return events;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read events from database", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent updateAdmin(String eventid, String adminid) throws DaoException{
        String sql = "UPDATE sotwevents SET adminid=:adminid WHERE eventid=:eventid;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("eventid", eventid).addParameter("adminid", adminid).executeUpdate();
            return this.read(eventid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the admin of event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent updateStartWeek(String eventid, String new_week) throws DaoException{
        String sql = "UPDATE sotwevents SET start_week=:start_week WHERE eventid=:eventid;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("eventid", eventid).addParameter("start_week", new_week).executeUpdate();
            return this.read(eventid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the start_week of event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent updateEndWeek(String eventid, String new_week) throws DaoException {
        String sql = "UPDATE sotwevents SET end_week=:end_week WHERE eventid=:eventid;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("eventid", eventid).addParameter("end_week", new_week).executeUpdate();
            return this.read(eventid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the end_week of event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent updateSong(String eventid, String songid) throws DaoException {
        String sql = "UPDATE sotwevents SET songid=:songid WHERE eventid=:eventid;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("eventid", eventid).addParameter("songid", songid).executeUpdate();
            return this.read(eventid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song of event", ex);
        }
    };

    @Override
    public Set<String> readAllSubmissionsGivenEvent(String eventid) throws DaoException{
        String getCurrentSubmissionsSQL = "SELECT * FROM sotweventssubmissions WHERE eventid=:eventid";
        try (Connection conn = sql2o.open()) {
            // Get current submissions stored in DB for this event
            List<Map<String, Object>> rows = conn.createQuery(getCurrentSubmissionsSQL).addParameter("eventid", eventid).executeAndFetchTable().asList();
            HashSet<String> currentSubmissions = new HashSet<String>();
            for (Map row : rows) {
                currentSubmissions.add((String) row.get("submission"));
            }

            // Return list of submissions for this event id
            return currentSubmissions;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to add a submission to event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent addSubmissionToEvent(String eventid, String submissionId) throws DaoException {
        String getCurrentSubmissionsSQL = "SELECT * FROM sotweventssubmissions WHERE eventid=:eventid";
        String addSubmissionSQL = "INSERT INTO sotweventssubmissions (eventid, submission) VALUES (:eventid, :submission)";
        try (Connection conn = sql2o.open()) {
            // Get current submissions stored in DB for this event
            List<Map<String, Object>> rows = conn.createQuery(getCurrentSubmissionsSQL).addParameter("eventid", eventid).executeAndFetchTable().asList();
            HashSet<String> currentSubmissions = new HashSet<String>();
            for (Map row : rows) {
                currentSubmissions.add((String) row.get("submission"));
            }

           // Add new submission to the database, if they aren't already in there
            if (!currentSubmissions.contains(submissionId)) {
                    conn.createQuery(addSubmissionSQL).addParameter("eventid", eventid).addParameter("submission", submissionId).executeUpdate();
                }

            // Return the updated event
            return this.read(eventid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to add a submission to event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent removeSubmissionFromEvent(String eventid, String submissionId) throws DaoException {
        String getCurrentSubmissionsSQL = "SELECT * FROM sotweventssubmissions WHERE eventid=:eventid";
        String removeSubmissionSQL = "DELETE FROM sotweventssubmissions WHERE eventid=:eventid AND submission=:submission";
        try (Connection conn = sql2o.open()) {
            // Get current submissions stored in DB for this event
            List<Map<String, Object>> rows = conn.createQuery(getCurrentSubmissionsSQL).addParameter("eventid", eventid).executeAndFetchTable().asList();
            HashSet<String> currentSubmissions = new HashSet<String>();
            for (Map row : rows) {
                currentSubmissions.add((String) row.get("submission"));
            }

            // Delete submission association from event if it exists
            for (String s : currentSubmissions) {
                if (s.equals(submissionId)) {
                    conn.createQuery(removeSubmissionSQL).addParameter("eventid", eventid).addParameter("submission", submissionId).executeUpdate();
                }
            }

            // Return the updated event
            return this.read(eventid);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to remove submission from event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent deleteEvent(String eventid) throws DaoException {
        String deleteeventssubmissionsSQL = "DELETE FROM sotweventssubmissions WHERE eventid=:eventid;";
        String deleteeventSQL = "DELETE FROM sotwevents WHERE eventid=:eventid;";
        try (Connection conn = sql2o.open()) {
            // Get songs to return before we delete
            SongOfTheWeekEvent eventToDelete = this.read(eventid);

            // Delete associated submissions
            conn.createQuery(deleteeventssubmissionsSQL).addParameter("eventid", eventid).executeUpdate();

            // Delete event
            conn.createQuery(deleteeventSQL).addParameter("eventid", eventid).executeUpdate();


            return eventToDelete;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the event", ex);
        }
    };

}
