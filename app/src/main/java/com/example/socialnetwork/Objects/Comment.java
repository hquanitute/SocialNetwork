package com.example.socialnetwork.Objects;

import java.util.Date;

public class Comment {
    String comment_id;
    String comment_account;
    String commented_post;
    Date commenting_date;
    String content;

    public Comment() {
    }

    public Comment(String comment_id, String comment_account, String commented_post, Date commenting_date, String content) {
        this.comment_id = comment_id;
        this.comment_account = comment_account;
        this.commented_post = commented_post;
        this.commenting_date = commenting_date;
        this.content = content;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_account() {
        return comment_account;
    }

    public void setComment_account(String comment_account) {
        this.comment_account = comment_account;
    }

    public String getCommented_post() {
        return commented_post;
    }

    public void setCommented_post(String commented_post) {
        this.commented_post = commented_post;
    }

    public Date getCommenting_date() {
        return commenting_date;
    }

    public void setCommenting_date(Date commenting_date) {
        this.commenting_date = commenting_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
