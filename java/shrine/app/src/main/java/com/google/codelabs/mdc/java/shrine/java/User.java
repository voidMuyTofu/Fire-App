package com.google.codelabs.mdc.java.shrine.java;

public class User {
    private String iduser;
    private String username;
    private String imageurl;

    public User(String id, String username, String imageurl){
        this.iduser = id;
        this.username = username;
        this.imageurl = imageurl;
    }
    public User(){

    }

    public String getId() {
        return iduser;
    }

    public void setId(String id) {
        this.iduser = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
