package model;

public class FriendRequest {

    private String senderID;
    private String recipientID;
    private boolean accepted;
    private boolean denied;

    public FriendRequest(String senderID, String recipientID) {
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.accepted = false;
        this.denied = false;
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isDenied() {
        return denied;
    }

    public void setDenied(boolean denied) {
        this.denied = denied;
    }
}
