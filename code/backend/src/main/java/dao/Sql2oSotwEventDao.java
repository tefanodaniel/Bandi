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
            String eventId = (String) row.get("eventId");
            String adminId = (String) row.get("adminId");
            String start_week = (String) row.get("start_week");
            String end_week = (String) row.get("end_week");
            String songId = (String) row.get("songId");
            String submission = (String) row.get("submission");

            if (!alreadyAdded.contains(eventId)) {
                alreadyAdded.add(eventId);
                sotwEvents.put(eventId, new SongOfTheWeekEvent(eventId, adminId, start_week, end_week, songId));
            }

            SongOfTheWeekEvent e = sotwEvents.get(eventId);
            e.addSubmissions(submission);

        }
        System.out.println("SOTW Event Extraction successful");
        return new ArrayList<SongOfTheWeekEvent>(sotwEvents.values());
    }

    @Override
    public SongOfTheWeekEvent create(String eventId, String adminId, String start_week, String end_week, String songId) throws DaoException {
        String sotw_event_sql = "INSERT INTO sotwevents (eventId, adminId, start_week, end_week, songId)" +
                "VALUES (:eventId, :adminId, :start_week, :end_week, :songId)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sotw_event_sql)
                    .addParameter("eventId", eventId)
                    .addParameter("adminId", adminId)
                    .addParameter("start_week", start_week)
                    .addParameter("end_week", end_week)
                    .addParameter("songId", songId)
                    .executeUpdate();

            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    };



    @Override
    public SongOfTheWeekEvent create(String eventId, String adminId, String start_week, String end_week, String songId, Set<String> submissions) throws DaoException {
        String sotw_event_sql = "INSERT INTO sotwevents (eventId, adminId, start_week, end_week, songId)" +
                "VALUES (:eventId, :adminId, :start_week, :end_week, :songId)";
        String sotw_event_submissions_sql = "INSERT INTO sotwseventsubmissions (eventId, submission) VALUES (:eventId, :submission)";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sotw_event_sql)
                    .addParameter("eventId", eventId)
                    .addParameter("adminId", adminId)
                    .addParameter("start_week", start_week)
                    .addParameter("end_week", end_week)
                    .addParameter("songId", songId)
                    .executeUpdate();

            for (String submissionId : submissions) {
                conn.createQuery(sotw_event_submissions_sql)
                        .addParameter("eventId", eventId)
                        .addParameter("submission", submissionId)
                        .executeUpdate();
            }

            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    };

    @Override
    public SongOfTheWeekEvent read(String eventId) throws DaoException {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT * FROM (SELECT S.eventId as EID, * FROM sotwevents as S) as R\n"
                    + "LEFT JOIN sotweventssubmissions as G ON R.EID=G.eventId\n"
                    + "WHERE R.EID=:eventId;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("eventId", eventId).executeAndFetchTable().asList();

            if (queryResults.size() == 0) {
                return null;
            }

            String adminId = (String) queryResults.get(0).get("adminId");
            String start_week = (String) queryResults.get(0).get("start_week");
            String end_week = (String) queryResults.get(0).get("end_week");
            String songId = (String) queryResults.get(0).get("songId");

            SongOfTheWeekEvent e = new SongOfTheWeekEvent(eventId, adminId, start_week, end_week, songId);
            for (Map row : queryResults) {
                if (row.get("submission") != null) {
                    e.addSubmissions((String) row.get("submission"));
                }
            }

            return e;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read event with id " + eventId, ex);
        }
    };

    @Override
    public List<SongOfTheWeekEvent> readAll() throws DaoException {
        String sql = "SELECT * FROM (SELECT S.eventId as EID, * FROM sotwevents as S) as R\n"
                + "LEFT JOIN sotweventssubmissions as G ON R.EID=G.eventId;";

        try (Connection conn = sql2o.open()) {
            List<SongOfTheWeekEvent> events = this.extractSOTWEventsFromDatabase(sql, conn);
            System.out.println(events);
            return events;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read events from database", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent updateAdmin(String eventId, String adminId) throws DaoException{
        String sql = "UPDATE sotwevents SET adminId=:adminId WHERE eventId=:eventId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("eventId", eventId).addParameter("adminId", adminId).executeUpdate();
            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the admin of event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent updateStartWeek(String eventId, String new_week) throws DaoException{
        String sql = "UPDATE sotwevents SET start_week=:start_week WHERE eventId=:eventId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("eventId", eventId).addParameter("start_week", new_week).executeUpdate();
            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the start_week of event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent updateEndWeek(String eventId, String new_week) throws DaoException {
        String sql = "UPDATE sotwevents SET end_week=:end_week WHERE eventId=:eventId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("eventId", eventId).addParameter("end_week", new_week).executeUpdate();
            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the end_week of event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent updateSong(String eventId, String songId) throws DaoException {
        String sql = "UPDATE sotwevents SET songId=:songId WHERE eventId=:eventId;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("eventId", eventId).addParameter("songId", songId).executeUpdate();
            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the song of event", ex);
        }
    };

    @Override
    public Set<String> readAllSubmissionsGivenEvent(String eventId) throws DaoException{
        String getCurrentSubmissionsSQL = "SELECT * FROM sotweventssubmissions WHERE eventId=:eventId";
        try (Connection conn = sql2o.open()) {
            // Get current submissions stored in DB for this event
            List<Map<String, Object>> rows = conn.createQuery(getCurrentSubmissionsSQL).addParameter("eventId", eventId).executeAndFetchTable().asList();
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
    public SongOfTheWeekEvent addSubmissionToEvent(String eventId, String submissionId) throws DaoException {
        String getCurrentSubmissionsSQL = "SELECT * FROM sotweventssubmissions WHERE eventId=:eventId";
        String addSubmissionSQL = "INSERT INTO sotweventssubmissions (eventId, submission) VALUES (:eventId, :submission)";
        try (Connection conn = sql2o.open()) {
            // Get current submissions stored in DB for this event
            List<Map<String, Object>> rows = conn.createQuery(getCurrentSubmissionsSQL).addParameter("eventId", eventId).executeAndFetchTable().asList();
            HashSet<String> currentSubmissions = new HashSet<String>();
            for (Map row : rows) {
                currentSubmissions.add((String) row.get("submission"));
            }

           // Add new submission to the database, if they aren't already in there
            if (!currentSubmissions.contains(submissionId)) {
                    conn.createQuery(addSubmissionSQL).addParameter("eventId", eventId).addParameter("submission", submissionId).executeUpdate();
                }

            // Return the updated event
            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to add a submission to event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent removeSubmissionFromEvent(String eventId, String submissionId) throws DaoException {
        String getCurrentSubmissionsSQL = "SELECT * FROM sotweventssubmissions WHERE eventId=:eventId";
        String removeSubmissionSQL = "DELETE FROM sotweventssubmissions WHERE eventId=:eventId AND submission=:submission";
        try (Connection conn = sql2o.open()) {
            // Get current submissions stored in DB for this event
            List<Map<String, Object>> rows = conn.createQuery(getCurrentSubmissionsSQL).addParameter("eventId", eventId).executeAndFetchTable().asList();
            HashSet<String> currentSubmissions = new HashSet<String>();
            for (Map row : rows) {
                currentSubmissions.add((String) row.get("submission"));
            }

            // Delete submission association from event if it exists
            for (String s : currentSubmissions) {
                if (s.equals(submissionId)) {
                    conn.createQuery(removeSubmissionSQL).addParameter("eventId", eventId).addParameter("submission", submissionId).executeUpdate();
                }
            }

            // Return the updated event
            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to remove submission from event", ex);
        }
    };

    @Override
    public SongOfTheWeekEvent deleteEvent(String eventId) throws DaoException {
        String deleteeventssubmissionsSQL = "DELETE FROM sotweventssubmissions WHERE eventId=:eventId;";
        String deleteeventSQL = "DELETE FROM sotwevents WHERE eventId=:eventId;";
        try (Connection conn = sql2o.open()) {
            // Get songs to return before we delete
            SongOfTheWeekEvent eventToDelete = this.read(eventId);

            // Delete associated submissions
            conn.createQuery(deleteeventssubmissionsSQL).addParameter("eventId", eventId).executeUpdate();

            // Delete event
            conn.createQuery(deleteeventSQL).addParameter("eventId", eventId).executeUpdate();


            return eventToDelete;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the event", ex);
        }
    };

}
