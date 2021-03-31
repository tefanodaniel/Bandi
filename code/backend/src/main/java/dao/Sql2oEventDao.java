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
        try(Connection conn = sql2o.open()) {
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

            return null;
            // return this.read(id);
        } catch(Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }
}
