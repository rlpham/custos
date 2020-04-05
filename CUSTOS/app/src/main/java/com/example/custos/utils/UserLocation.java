package com.example.custos.utils;

public class UserLocation {
    private String userAddress;
    private double latitude;
    private double longitude;
    public UserLocation(){


    }
    public  UserLocation(String UID, double lat,double lon){
        userAddress=UID;
        latitude=lat;
        longitude=lon;

    }
    public double getLat(){
        return latitude;
    }
    public double getLon(){
        return longitude;
    }
    public void setLat(double lat){
        latitude=lat;
    }
    public void setLon(double lon){
        longitude=lon;
    }
    public String getUID(){
        return userAddress;
    }
    public void setUID(String uid){
        userAddress=uid;

    }

}
