package model;

public interface Request {

    String getSenderID();

    void setSenderID(String senderID);

    String getSenderName();

    void setSenderName(String senderName);

    String getRecipientID();

    void setRecipientID(String recipientID);

    String getRecipientName();

    void setRecipientName(String recipientName);
}
