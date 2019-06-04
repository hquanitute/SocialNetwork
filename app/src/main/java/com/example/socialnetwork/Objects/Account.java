package com.example.socialnetwork.Objects;

public class Account {
    String id;
    String email;
    String password;
    String account_name;

    public Account() {
    }

    public Account(String id, String email, String password, String account_name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.account_name = account_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }
}
