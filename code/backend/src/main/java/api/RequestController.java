package api;

import exceptions.ApiError;
import exceptions.DaoException;
import model.FriendRequest;
import model.Musician;
import spark.Route;

import java.util.List;

import static api.ApiServer.musicianDao;
import static api.ApiServer.requestDao;
import static api.ApiServer.gson;

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
            List<FriendRequest> requests = requestDao.readAllTo(recipientID);
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
            List<FriendRequest> requests = requestDao.readAllFrom(senderID);
            res.type("application/json");
            return gson.toJson(requests);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // send friend request
    public static Route postFriendRequest = (req, res) -> {
        try {
            String senderID = req.params("senderid");
            String recipientID = req.params("recipientid");
            String senderName = musicianDao.read(senderID).getName();
            String recipientName = musicianDao.read(recipientID).getName();
            FriendRequest fr = requestDao.createRequest(senderID, senderName, recipientID, recipientName);
            if (fr == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(fr);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };

    // respond (accept or decline) friend request (delete route)
    public static Route respondToRequest = (req, res) -> {
        try {
            String senderID = req.params("senderid");
            String recipientID = req.params("recipientid");
            String action = req.params("action");

            FriendRequest fr = null;

            if (action.equals("accept"))  {
                fr = requestDao.acceptRequest(senderID, recipientID);
            } else if (action.equals("decline")) {
                fr = requestDao.declineRequest(senderID, recipientID);
            } else {
                throw new ApiError("Invalid action to perform on request", 505);
            }

            if (fr == null) {
                throw new ApiError("Resource not found", 404); // Bad request
            }
            res.type("application/json");
            return gson.toJson(fr);
        } catch (DaoException ex) {
            throw new ApiError(ex.getMessage(), 500);
        }
    };
}
