package dao;

import exceptions.DaoException;
import model.BandInvite;
import model.FriendRequest;
import model.Request;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sql2oRequestDao implements RequestDao {

    private final Sql2o sql2o;

    /**
     * Construct Sql2oRequestDao.
     *
     * @param sql2o A Sql2o object is injected as a dependency;
     *   it is assumed sql2o is connected to a database that contains tables called
     *   "Requests" with columns: "senderid", "sendername", "recipientid", "recipientname", "type"
     *   "MusicianFriends" with columns: "id" and "friendid"
     *   "BandMembers" with columns: "member" and "band"
     */
    public Sql2oRequestDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Request createRequest(String senderID, String senderName, String recipientID, String recipientName, String type) throws DaoException {

        String sql;
        if (type.equals("friend")) {
            sql = "INSERT INTO Requests(senderid, sendername, recipientid, recipientname, type) " +
                    "VALUES(:senderid, :sendername, :recipientid, :recipientname, :type);";
        } else if (type.equals("band")) {
            sql = "INSERT INTO Requests(senderid, sendername, recipientid, recipientname, type) " +
                    "VALUES(:senderid, :sendername, :recipientid, :recipientname, :type);";
        } else {
            sql = ""; // should never happen
        }

        try (Connection conn = sql2o.open()) {

            Request result = this.read(senderID, recipientID, type);
            if (result == null) {
                conn.createQuery(sql)
                        .addParameter("senderid", senderID)
                        .addParameter("sendername", senderName)
                        .addParameter("recipientid", recipientID)
                        .addParameter("recipientname", recipientName)
                        .addParameter("type", type)
                        .executeUpdate();
                return this.read(senderID, recipientID, type);
            }
            return result;

        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Request read(String senderID, String recipientID, String type) throws DaoException {

        try (Connection conn = sql2o.open()) {

            String sql;
            sql = "SELECT * FROM requests AS r " +
                         "WHERE r.senderid = :senderid " +
                         "AND r.recipientid = :recipientid " +
                         "AND r.type = :type;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("senderid", senderID)
                    .addParameter("recipientid", recipientID).addParameter("type", type).executeAndFetchTable().asList();

            if (queryResults.size() == 0) { // recipient doesn't exist
                return null;
            }

            String senderName = (String) queryResults.get(0).get("sendername");
            String recipientName = (String) queryResults.get(0).get("recipientname");

            Request r;
            if (type.equals("friend")) {
                r = new FriendRequest(senderID, senderName, recipientID, recipientName);
            }
            else {
                r = new BandInvite(senderID, senderName, recipientID, recipientName);
            }
            return r;

        } catch (Sql2oException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException("Unable to read a " + type + " request from " + senderID + " to " + recipientID, ex);
        }
    }

    @Override
    public List<Request> readAllFrom(String senderID, String type) throws DaoException {
        try (Connection conn = sql2o.open()) {

            String sql = "SELECT * FROM requests AS r " +
                    "WHERE r.senderid = :senderid AND r.type = :type;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("senderid", senderID)
                    .addParameter("type", type).executeAndFetchTable().asList();

            if (queryResults.size() == 0) { // user has no sent out friend request
                return new ArrayList<>();
            }

            List<Request> from = new ArrayList<>();
            for (Map row : queryResults) {
                String recipientID = (String) row.get("recipientid");
                String senderName = (String) row.get("sendername");
                String recipientName = (String) row.get("recipientname");
                if (type.equals("friend")) {
                    from.add(new FriendRequest(senderID, senderName, recipientID, recipientName));
                } else {
                    from.add(new BandInvite(senderID, senderName, recipientID, recipientName));
                }

            }

            return from;

        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read " + type + " requests from " + senderID, ex);
        }
    }

    @Override
    public List<Request> readAllTo(String recipientID, String type) throws DaoException {
        try (Connection conn = sql2o.open()) {

            String sql = "SELECT * FROM requests AS r " +
                    "WHERE r.recipientid = :recipientid AND r.type = :type;";

            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("recipientid", recipientID)
                    .addParameter("type", type).executeAndFetchTable().asList();

            if (queryResults.size() == 0) { // user has no incoming friend requests
                return new ArrayList<>();
            }

            List<Request> to = new ArrayList<>();
            for (Map row : queryResults) {
                String senderID = (String) row.get("senderid");
                String senderName = (String) row.get("sendername");
                String recipientName = (String) row.get("recipientname");
                if (type.equals("friend")) {
                    to.add(new FriendRequest(senderID, senderName, recipientID, recipientName));
                } else {
                    to.add(new BandInvite(senderID, senderName, recipientID, recipientName));
                }
            }

            return to;

        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read " + type + " requests to " + recipientID, ex);
        }
    }

    @Override
    public Request acceptRequest(String senderID, String recipientID, String type) throws DaoException {
        String deleteRequestSql = "DELETE FROM Requests WHERE senderid=:senderid AND recipientid=:recipientid AND type=:type;";

        if (type.equals("friend")) {
            String makeFriendsSql1 = "INSERT INTO MusicianFriends(id, friendid) VALUES(:senderid, :recipientid);";
            String makeFriendsSql2 = "INSERT INTO MusicianFriends(id, friendid) VALUES(:recipientid, :senderid);";

            try (Connection conn = sql2o.open()) {
                conn.createQuery(makeFriendsSql1).addParameter("senderid", senderID).addParameter("recipientid", recipientID).executeUpdate();
                conn.createQuery(makeFriendsSql2).addParameter("senderid", senderID).addParameter("recipientid", recipientID).executeUpdate();
                conn.createQuery(deleteRequestSql).addParameter("senderid", senderID).addParameter("recipientid", recipientID)
                        .addParameter("type", type).executeUpdate();
            } catch (Sql2oException ex) {
                throw new DaoException("Unable to accept friend request", ex);
            }
            return new FriendRequest(senderID, null,  recipientID, null);
        } else {
            String joinBandSql = "INSERT INTO BandMembers(member, band) VALUES(:recipientid, :senderid);";

            try (Connection conn = sql2o.open()) {
                conn.createQuery(joinBandSql).addParameter("senderid", senderID).addParameter("recipientid", recipientID).executeUpdate();
                conn.createQuery(deleteRequestSql).addParameter("senderid", senderID).addParameter("recipientid", recipientID)
                        .addParameter("type", type).executeUpdate();

            } catch (Sql2oException ex) {
                throw new DaoException("Unable to accept band invite", ex);
            }
            return new BandInvite(senderID, null,  recipientID, null);

        }


    }

    @Override
    public Request declineRequest(String senderID, String recipientID, String type) throws DaoException {

        String deleteRequestSql = "DELETE FROM Requests WHERE senderid=:senderid AND recipientid=:recipientid AND type=:type;";
        try (Connection conn = sql2o.open()) {
            conn.createQuery(deleteRequestSql).addParameter("senderid", senderID).addParameter("recipientid", recipientID)
                    .addParameter("type", type).executeUpdate();
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to decline " + type + " request", ex);
        }

        if (type.equals("friend")) {
            return new FriendRequest(senderID, null,  recipientID, null);
        } else {
            return new BandInvite(senderID, null,  recipientID, null);

        }
    }

}
