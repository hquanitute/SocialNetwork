package com.example.socialnetwork.Interface;

import com.example.socialnetwork.Objects.Account;
import com.example.socialnetwork.Objects.Post;

public interface EventListener {
    void sendComment(Post oldPost, String comment,int position);
    void addfriend(Account account);
}
