package com.example.pascal.apitest.model;

/**
 * Created by jeroen on 18-5-2018.
 */

public class User {
    private String username;
    private String realname;
    private int active = 1;

    public User(String username, String realname) {
        this.username = username;
        this.realname = realname;
    }
    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Override
    public String toString(){
        return realname + " - " + username;
    }


    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
