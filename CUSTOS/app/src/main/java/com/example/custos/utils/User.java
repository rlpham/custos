package com.example.custos.utils;

import java.util.HashMap;

public class User {

    private String userName;
    private String userEmail;
    private String userId;
    private String userAddress;
    private String UID;
    private String imageURL;
    private HashMap<String,User> acceptList;
    public User(){

    }
    public User(String uid, String email,String name){
        this.UID =uid;
        this.userEmail = email;
        this.userName = name;
        acceptList = new HashMap<>();
    }
    public User(String uid, String email,String name,String img){
        this.UID =uid;
        this.userEmail = email;
        this.userName = name;
        this.imageURL = img;
        acceptList = new HashMap<>();
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserEmail(){
        return userEmail;
    }
    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }
    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
