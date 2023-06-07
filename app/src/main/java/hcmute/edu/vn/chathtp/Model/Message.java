package hcmute.edu.vn.chathtp.Model;

import androidx.annotation.NonNull;

public class Message {
    private String message_id;
    private User sender;
    private User receiver;
    private String message;
    private boolean isseen;

    public Message() {
    }

    public Message(String message_id, User sender, User receiver, String message, boolean isseen) {
        this.message_id = message_id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
    }

    public Message(User sender, User receiver, String message, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "message_id='" + message_id + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", message='" + message + '\'' +
                ", isseen=" + isseen +
                '}';
    }
}