package dao;
import exceptions.DaoException;
import model.Request;
import model.Request;

import java.util.List;

public interface RequestDao {

    Request createRequest(String senderID, String senderName, String recipientID, String recipientName) throws DaoException;

    List<Request> readAllFrom(String senderID) throws DaoException;

    List<Request> readAllTo(String recipientID) throws DaoException;

    Request read(String senderID, String recipientID) throws DaoException;

    Request acceptRequest(String senderID, String recipientID) throws DaoException;

    Request declineRequest(String senderID, String recipientID) throws DaoException;

}
