package com.example.custos;

public class SetHomeLocation {
    private String address;
    private double latitude;
    private double longtitude;

    public  SetHomeLocation(){

    }
    public SetHomeLocation(double lat, double longtit){
        latitude = lat;
        longtitude = longtit;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongtitude(){
        return longtitude;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude){
        this.longtitude = longtitude;
    }
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address = address;
    }
}
