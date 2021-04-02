package dao;
import exceptions.DaoException;
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
            String songName = (String) row.get("songName");
            String artistName = (String) row.get("artistName");
            String albumName = (String) row.get("albumName");
            Integer releaseYear = (Integer) row.get("releaseYear");
            String genre = (String) row.get("genres");

        }
        return null;
    }

    public SongOfTheWeekEvent create(String eventId, String adminId, String start_week, String end_week, String songId) throws DaoException {
        return null;
    };

    public SongOfTheWeekEvent read(String eventId) throws DaoException {
        return null;
    };

    public List<SongOfTheWeekEvent> readAll() throws DaoException {
        return null;
    };

    public SongOfTheWeekEvent updateAdmin(String eventId, String adminId) throws DaoException{
        return null;
    };

    public SongOfTheWeekEvent updateStartWeek(String eventId, String new_week) throws DaoException{
        return null;
    };

    public SongOfTheWeekEvent updateEndWeek(String eventId, String new_week) throws DaoException {
        return null;
    };

    public SongOfTheWeekEvent updateSong(String eventId, String songId) throws DaoException {
        return null;
    };

    public Set<SongOfTheWeekSubmission> readAllGivenEvent(String eventId) throws DaoException{
        return null;
    };

    public SongOfTheWeekSubmission addSubmissionToEvent(String eventId, String submissionId) throws DaoException {
        return null;
    };

    public SongOfTheWeekSubmission removeSubmissionFromEvent(String eventId, String submissionId) throws DaoException {
        return null;
    };

    public SongOfTheWeekEvent deleteEvent(String eventId) throws DaoException {
        return null;
    }; // this should delete each submission asssociated with it in return


}
