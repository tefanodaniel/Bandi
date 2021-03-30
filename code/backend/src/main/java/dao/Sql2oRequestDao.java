package dao;

import exceptions.DaoException;
import model.FriendRequest;

import java.util.List;

public class Sql2oRequestDao implements RequestDao {

    @Override
    public FriendRequest create(String senderID, String recipientID) throws DaoException {
        return null;
    }

    @Override
    public List<FriendRequest> readAllFrom(String senderID) throws DaoException {
        return null;
    }

    @Override
    public boolean acceptRequest(String senderID, String recipientID) throws DaoException {
        return false;
    }

    @Override
    public boolean declineRequest(String senderID, String recipientID) throws DaoException {
        return false;
    }

    @Override
    public FriendRequest deleteRequest(String senderID, String recipientID) throws DaoException {
        return null;
    }
}
