package dao;

import exceptions.DaoException;
import model.Band;
import model.Event;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.*;

public class Sql2oEventDao {

    private final Sql2o sql2o;

    public Sql2oEventDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public List<Event> readAll() throws DaoException {
        String sql = "SELECT * FROM (SELECT e.id as eID, * FROM events as e) as R\n"
                + "LEFT JOIN participants as P ON R.eID=P.event;";
        try (Connection conn = sql2o.open()) {
            List<Event> events = this.extractEventsFromDatabase(sql, conn);
            return events;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read events from the database", ex);
        }
    }

    private List<Event> extractEventsFromDatabase(String sql, Connection conn) throws Sql2oException {

        List<Map<String, Object>> queryResults = conn.createQuery(sql).executeAndFetchTable().asList();

        HashSet<String> alreadyAdded = new HashSet<String>();
        Map<String, Event> events = new HashMap<String, Event>();
        for (Map row : queryResults) {
            // Extract data from this row
            String id = (String) row.get("eid");
            String name = (String) row.get("name");
            String link = (String) row.get("link");
            String date = (String) row.get("date");
            int minusers = (int) row.get("minusers");
            String participant = (String) row.get("participant");

            // Check if we've seen this Event already
            if (!alreadyAdded.contains(id)) {
                alreadyAdded.add(id);
                events.put(id, new Event(id, name, link, date, minusers, new HashSet<String>()));
            }

            // Add the current participant to the event's set
            Event e = events.get(id);
            e.addParticipant(participant);
        }
        return new ArrayList<Event>(events.values());
    }

    public Event create(String id, String name, String link,
                        String date, int minusers, Set<String> participants) throws DaoException {

        String eventSQL = "INSERT INTO Events (id, name, link, date, minusers) VALUES (:id, :name, :link, :date, :minusers)";
        String participantSQL = "INSERT INTO Participants (participant, event) VALUES (:participant, :event)";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(eventSQL)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("link", link)
                    .addParameter("date", date)
                    .addParameter("minusers", minusers)
                    .executeUpdate();

            for (String participant : participants) {
                conn.createQuery(participantSQL)
                        .addParameter("participant", participant)
                        .addParameter("event", id)
                        .executeUpdate();
            }

            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    public Event add(String eventId, String musId) throws DaoException {
        String sql = "INSERT INTO Participants (participant, event) VALUES (:participant, :event);";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("participant", musId)
                    .addParameter("event", eventId)
                    .executeUpdate();
            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to add new participant", ex);
        }
    }

    public Event remove(String eventId, String musId) throws DaoException {
        String sql = "DELETE FROM Participants WHERE participant=:musId;";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("musId", musId)
                    .executeUpdate();
            return this.read(eventId);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to remove participant", ex);
        }
    }

    public Event read(String id) throws DaoException {
        String sql = "SELECT * FROM (SELECT e.id as eID, * FROM events as e) as R\n"
                + "LEFT JOIN Participants as P ON R.eID=P.event\n"
                + "WHERE R.eID=:id";
        try (Connection conn = sql2o.open()) {
            List<Map<String, Object>> queryResults =
                    conn.createQuery(sql).addParameter("id", id).executeAndFetchTable().asList();

            String eventId = (String) queryResults.get(0).get("id");
            String name = (String) queryResults.get(0).get("name");
            String link = (String) queryResults.get(0).get("link");
            String date = (String) queryResults.get(0).get("date");
            int minusers = (int) queryResults.get(0).get("minusers");

            Event e = new Event(eventId, name, link, date, minusers, new HashSet<String>());
            for (Map row : queryResults) {
                e.addParticipant((String) row.get("participant"));
            }
            return e;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read an Event with id " + id, ex);
        }
    }

}