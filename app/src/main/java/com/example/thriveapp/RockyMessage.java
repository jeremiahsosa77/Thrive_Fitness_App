package com.example.thriveapp;

public class RockyMessage {
    public static String SENT_BY_USER = "USER";
    public static String SENT_BY_BOT = "BOT";

    String message;
    String sentBy;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public RockyMessage(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }
}
