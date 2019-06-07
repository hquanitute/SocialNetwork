package com.example.socialnetwork.Objects;

public class Account {
    String id;
    String imageURL;
    String account_name;

    public Account() {
    }

    public Account(String id, String imageURL, String account_name) {
        this.id = id;
        this.imageURL=imageURL;
        this.account_name = account_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }
}
