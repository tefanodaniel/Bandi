package api;

import exceptions.ApiError;
import exceptions.DaoException;
import model.BandInvite;
import model.FriendRequest;
import model.Musician;
import model.Request;
import spark.Route;

import java.util.List;

import static api.ApiServer.*;

public class RequestController {

    // Get Musician's Friends
    public static Route getMusicianFriends = (req, res) -> {
        try {
            String id = req.params("id");
            List<Musician> musicians = musicianDao.getAllFriendsOf(id);
            res.type("application/json");
            return gson.toJson(musicians);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get all of user's pending incoming friend requests
    public static Route getIncomingPendingRequests = (req, res) -> {
        try {
            String recipientID = req.params("recipientid");
            String type = req.params("type");
            List<Request> requests = requestDao.readAllTo(recipientID, type);
            res.type("application/json");
            return gson.toJson(requests);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // get all of user's pending outgoing friend requests
    public static Route getOutgoingPendingRequests = (req, res) -> {
        try {
            String senderID = req.params("senderid");
            String type = req.params("type");
            List<Request> requests = requestDao.readAllFrom(senderID, type);
            res.type("application/json");
            return gson.toJson(requests);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // send request
    public static Route postRequest = (req, res) -> {
        try {

            String senderID = req.params("senderid");
            String recipientID = req.params("recipientid");
            String type = req.params("type");
            String recipientName = musicianDao.read(recipientID).getName();
            String senderName;
            Request r;
            if (type.equals("friend")) {
                senderName = musicianDao.read(senderID).getName();
                r = (FriendRequest) requestDao.createRequest(senderID, senderName, recipientID, recipientName, "friend");
            } else {
                System.out.println("Here to read before posting");
                senderName = bandDao.read(senderID).getName();

                r = (BandInvite) requestDao.createRequest(senderID, senderName, recipientID, recipientName, "band");
            }

            if (r == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(r);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // respond (accept or decline) friend request (delete route)
    public static Route respondToRequest = (req, res) -> {
        try {
            String senderID = req.params("senderid");
            String recipientID = req.params("recipientid");
            String type = req.params("type");
            String action = req.params("action");

            Request r;
            if (action.equals("accept"))  {
                r = requestDao.acceptRequest(senderID, recipientID, type);
            } else if (action.equals("decline")) {
                r = requestDao.declineRequest(senderID, recipientID, type);
            } else {
                throw new ApiError("Invalid action to perform on request", 505);
            }

            if (r == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(r);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

}
