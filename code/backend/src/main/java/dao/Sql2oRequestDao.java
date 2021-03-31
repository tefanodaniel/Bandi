package dao;

import exceptions.DaoException;
import model.FriendRequest;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sql2oRequestDao implements RequestDao {

    private final Sql2o sql2o;

    /**
     * Construct Sql2oCourseDao.
     *
     * @param sql2o A Sql2o object is injected as a dependency;
     *   it is assumed sql2o is connected to a database that contains tables called
     *   "Requests" with columns: "senderid" and "recipientid"
     *   "MusicianFriends" with columns: "id" and "friendid"
     */
    public Sql2oRequestDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public FriendRequest create(String senderID, String recipientID) throws DaoException {
        String sql = "INSERT INTO Requests(senderid, recipientid) VALUES(:senderid, :recipientid);";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql).addParameter("senderid", senderID).addParameter("recipientid", recipientID).executeUpdate();
            return this.read(senderID, recipientID);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public FriendRequest read(String senderID, String recipientID) throws DaoException {

        try (Connection conn = sql2o.open()) {

            String sql = "SELECT * FROM friendrequests AS fr " +
                         "WHERE fr.senderid = :senderid " +
                         "AND fr.recipientid = :recipientid;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("senderid", senderID)
                    .addParameter("recipientid", recipientID).executeAndFetchTable().asList();

            if (queryResults.size() == 0) { // no such musician is present
                return null;
            }

            // Extract attributes
            boolean name = (boolean) queryResults.get(0).get("accepted");

            FriendRequest fr = new FriendRequest(senderID, recipientID);
            return fr;

        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a friend request from " + senderID + "to " + recipientID, ex);
        }
    }

    @Override
    public List<FriendRequest> readAllFrom(String senderID) throws DaoException {
        try (Connection conn = sql2o.open()) {

            String sql = "SELECT * FROM friendrequests AS fr " +
                    "WHERE fr.senderid = :senderid;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("senderid", senderID)
                    .executeAndFetchTable().asList();

            if (queryResults.size() == 0) { // user has no sent out friend request
                return null;
            }

            List<FriendRequest> from = new ArrayList<>();
            for (Map row : queryResults) {

                // Extract data from this row
                String senderid = (String) row.get("senderid");
                String recipientid = (String) row.get("recipientid");
                boolean accepted = (boolean) row.get("accepted");

                from.add(new FriendRequest(senderid, recipientid));
            }

            return from;

        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a friend request from " + senderID, ex);
        }
    }

    @Override
    public String acceptRequest(String senderID, String recipientID) throws DaoException {

        String makeFriendsSql1 = "INSERT INTO MusicianFriends(id, friendid) VALUES(:senderid, :recipientid);";
        String makeFriendsSql2 = "INSERT INTO MusicianFriends(id, friendid) VALUES(:recipientid, :senderid);";
        String deleteRequestSql = "DELETE FROM Requests WHERE senderid=:senderid AND recipientid=:recipientid;";

        try (Connection conn = sql2o.open()) {
            conn.createQuery(makeFriendsSql1).addParameter("senderid", senderID).addParameter("recipientid", recipientID).executeUpdate();
            conn.createQuery(makeFriendsSql2).addParameter("senderid", senderID).addParameter("recipientid", recipientID).executeUpdate();
            conn.createQuery(deleteRequestSql).addParameter("senderid", senderID).addParameter("recipientid", recipientID).executeUpdate();
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to accept friend request", ex);
        }
        return recipientID;
    }

    @Override
    public String declineRequest(String senderID, String recipientID) throws DaoException {

        String deleteRequestSql = "DELETE FROM Requests WHERE senderid=:senderid AND recipientid=:recipientid;";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(deleteRequestSql).addParameter("senderid", senderID).addParameter("recipientid", recipientID).executeUpdate();
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to decline friend request", ex);
        }
        return recipientID;
    }

}
