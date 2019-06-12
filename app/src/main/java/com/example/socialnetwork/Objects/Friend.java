package com.example.socialnetwork.Objects;

public class Friend {
    String idfriend;
    String name_friend;

    public Friend(String idfriend, String name_friend) {
        this.idfriend = idfriend;
        this.name_friend = name_friend;
    }

    public Friend() {
    }

    public String getIdfriend() {
        return idfriend;
    }

    public void setIdfriend(String idfriend) {
        this.idfriend = idfriend;
    }

    public String getName_friend() {
        return name_friend;
    }

    public void setName_friend(String name_friend) {
        this.name_friend = name_friend;
    }
}
