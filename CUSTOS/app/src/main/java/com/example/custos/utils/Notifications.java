package com.example.custos.utils;

public class Notifications {

    private String friendName;
    private String request_time;
    private String UID;
    private String request_type;
    private String imageURL;

    public Notifications(){

    }
    public Notifications(String friendName, String request_time) {
        this.friendName = friendName;
        this.request_time = request_time;
    }

    public Notifications(String friendName, String request_time, String uid, String request_type,String imageURL) {
        this.friendName = friendName;
        this.request_time = request_time;
        this.UID = uid;
        this.request_type = request_type;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String uid) {
        this.UID = uid;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }
}
