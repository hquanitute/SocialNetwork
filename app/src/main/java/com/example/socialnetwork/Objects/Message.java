package com.example.socialnetwork.Objects;

public class Message {
    String idSender;
    String idReceiver;
    String message;
    String image;

    public Message() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Message(String idSender, String idReceiver, String message, String image) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.image=image;
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
