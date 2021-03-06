package com.example.pascal.apitest.model;

/**
 * Created by jeroen on 18-5-2018.
 */

public class Playlist {
    private String name;
    private String id;
    private String desc;

    public Playlist(String name, String id, String desc) {
        this.name = name;
        this.id = id;
        this.desc = desc;
    }

    public Playlist(String name, String id) {
        this.name = name;
        this.id = id;
        this.desc = "";
    }

    public Playlist() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
