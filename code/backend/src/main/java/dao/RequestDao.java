package dao;
import exceptions.DaoException;
import model.Request;
import model.Request;

import java.util.List;

public interface RequestDao {

    Request createRequest(String senderID, String senderName, String recipientID, String recipientName, String type) throws DaoException;

    List<Request> readAllFrom(String senderID, String type) throws DaoException;

    List<Request> readAllTo(String recipientID, String type) throws DaoException;

    Request read(String senderID, String recipientID, String type) throws DaoException;

    Request acceptRequest(String senderID, String recipientID, String type) throws DaoException;

    Request declineRequest(String senderID, String recipientID, String type) throws DaoException;

}
