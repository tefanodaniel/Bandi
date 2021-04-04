package model;

public class FriendRequest {

    private String senderID;
    private String senderName;
    private String recipientID;
    private String recipientName;

    public FriendRequest(String senderID, String senderName, String recipientID, String recipientName) {
        this.senderID = senderID;
        this.senderName = senderName;
        this.recipientID = recipientID;
        this.recipientName = recipientName;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
