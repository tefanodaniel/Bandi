package dao;
import exceptions.DaoException;
import model.FriendRequest;

import java.util.List;

public interface RequestDao {

    FriendRequest createRequest(String senderID, String recipientID) throws DaoException;

    List<FriendRequest> readAllFrom(String senderID) throws DaoException;

    FriendRequest read(String senderID, String recipientID) throws DaoException;

    String acceptRequest(String senderID, String recipientID) throws DaoException;

    String declineRequest(String senderID, String recipientID) throws DaoException;

}
