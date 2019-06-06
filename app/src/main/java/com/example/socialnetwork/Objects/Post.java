package com.example.socialnetwork.Objects;

import java.util.Date;

public class Post {
    String post_id;
    String account_name;
    String text;
    String image;
    Date posting_date;

    public Post() {
    }

    public Post(String post_id, String account_name, String text, String image, Date posting_date) {
        this.post_id = post_id;
        this.account_name = account_name;
        this.text = text;
        this.image = image;
        this.posting_date = posting_date;
    }

    public Date getPosting_date() {
        return posting_date;
    }

    public void setPosting_date(Date posting_date) {
        this.posting_date = posting_date;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}