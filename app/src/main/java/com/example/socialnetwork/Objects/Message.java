package com.example.socialnetwork.Objects;

public class Message {
    String idSender;
    String idReceiver;
    String message;

    public Message() {
    }

    public Message(String idSender, String idReceiver, String message) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
    }

    public String getIdSender() {
        return idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public String getMessage() {
        return message;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
