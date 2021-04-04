package dao;
import exceptions.DaoException;
import model.FriendRequest;

import java.util.List;

public interface RequestDao {

    FriendRequest createRequest(String senderID, String senderName,  String recipientID, String recipientName) throws DaoException;

    List<FriendRequest> readAllFrom(String senderID) throws DaoException;

    List<FriendRequest> readAllTo(String recipientID) throws DaoException;

    FriendRequest read(String senderID, String recipientID) throws DaoException;

    FriendRequest acceptRequest(String senderID, String recipientID) throws DaoException;

    FriendRequest declineRequest(String senderID, String recipientID) throws DaoException;

}
