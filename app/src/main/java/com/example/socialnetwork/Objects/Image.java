package com.example.socialnetwork.Objects;

public class Image {
    String image_id;
    String link;

    public Image() {
    }

    public Image(String image_id, String link) {
        this.image_id = image_id;
        this.link = link;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}