package com.example.custos.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Notifications implements Comparable<Notifications> {

    private String friendName;
    private String request_time;
    private String UID;
    private String request_type;
    private String imageURL;
    private String eventId;
    private String userToken;

    public Notifications(){

    }
    public Notifications(String friendName, String request_time) {
        this.friendName = friendName;
        this.request_time = request_time;
    }

    public Notifications(String friendName, String request_time,
                         String uid, String request_type,
                         String imageURL,String eventId,String userToken) {
        this.friendName = friendName;
        this.request_time = request_time;
        this.UID = uid;
        this.request_type = request_type;
        this.imageURL = imageURL;
        this.eventId = eventId;
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    @Override
    public int compareTo(Notifications notifications) {
        Calendar timeAcceptFriend = Calendar.getInstance();
        SimpleDateFormat acceptTime = new SimpleDateFormat("hh:mm a");
        String timeAccept = acceptTime.format(timeAcceptFriend.getTime());
        if(timeAccept!=null){
            timeAccept = getRequest_time();

        }
        Calendar timeAcceptFriend2 = Calendar.getInstance();
        SimpleDateFormat acceptTime2= new SimpleDateFormat("hh:mm a");
        String timeAccept2 = acceptTime2.format(timeAcceptFriend2.getTime());
        if(timeAccept2!=null){
            timeAccept2 = notifications.getRequest_time();
        }
        return timeAccept2.compareTo(timeAccept);
    }
}
