package com.example.custos.utils;

import java.util.HashMap;

public class Friends {

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
}
