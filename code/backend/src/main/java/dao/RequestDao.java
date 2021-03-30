package dao;
import exceptions.DaoException;
import model.FriendRequest;

import java.util.List;

public interface RequestDao {

    FriendRequest create(String senderID, String recipientID) throws DaoException;

    List<FriendRequest> readAllFrom(String senderID) throws DaoException;

    boolean acceptRequest(String senderID, String recipientID) throws DaoException;

    boolean declineRequest(String senderID, String recipientID) throws DaoException;

    FriendRequest deleteRequest(String senderID, String recipientID) throws DaoException;

}
