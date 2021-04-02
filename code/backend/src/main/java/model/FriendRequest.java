package model;

public class FriendRequest {

    private String senderID;
    private String recipientID;

    public FriendRequest(String senderID, String recipientID) {
        this.senderID = senderID;
        this.recipientID = recipientID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

}
