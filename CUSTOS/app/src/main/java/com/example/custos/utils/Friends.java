package com.example.custos.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Friends implements Comparable<Friends>{

    private String friendName;
    private String friendEmail;
    private String date;
    private String UID;
    private String imageURL;
    public Friends(){

    }



    public Friends(String name, String uid, String email, String img){
        this.UID =uid;
        this.friendEmail = email;
        this.friendName = name;
        this.imageURL = img;
    }
    public Friends(String uid, String email, String name, String img,String date){
        this.UID =uid;
        this.friendEmail = email;
        this.friendName = name;
        this.imageURL = img;
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getFriendName(){
        return friendName;
    }
    public void setFriendName(String friendName){
        this.friendName = friendName;
    }
    public String getFriendEmail(){
        return friendEmail;
    }
    public void setFriendEmail(String friendEmail){
        this.friendEmail = friendEmail;
    }


    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    @Override
    public int compareTo(Friends friends) {
        Calendar timeAcceptFriend = Calendar.getInstance();
        SimpleDateFormat acceptTime = new SimpleDateFormat("hh:mm a");
        String timeAccept = acceptTime.format(timeAcceptFriend.getTime());
        timeAccept = getDate();
        Calendar timeAcceptFriend2 = Calendar.getInstance();
        SimpleDateFormat acceptTime2= new SimpleDateFormat("hh:mm a");
        String timeAccept2 = acceptTime.format(timeAcceptFriend.getTime());
        timeAccept2 = friends.getDate();
        return timeAccept2.compareTo(timeAccept);
    }
}
