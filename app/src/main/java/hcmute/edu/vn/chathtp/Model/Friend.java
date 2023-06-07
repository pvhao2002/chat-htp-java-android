package hcmute.edu.vn.chathtp.Model;

import java.io.Serializable;

public class Friend implements Serializable{
    private String friendId;
    private User sender;
    private User receiver;
    private String status;

    public Friend() {
    }
    public Friend(String friendId, User sender, User receiver, String status) {
        this.friendId = friendId;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }
    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friendId='" + friendId + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", status='" + status + '\'' +
                '}';
    }
}
