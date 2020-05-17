package com.upec.androidtemplate20192020;

/**
 * Edited by Akram TOUABET on 29/03/2020 at 03:43
 */
public class User {
    private String uid, username;

    public User(String uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
