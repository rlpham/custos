package com.example.custos.utils;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String ID;
    private String name;
    private String area;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String description;
    private String location_name;
    private ArrayList<User> invited_users;
    private boolean isSafety;

    public Event(String ID, String name, String area, String startDate, String startTime,
                 String endDate, String endTime, String description, String location_name,
                 ArrayList<User> invited_users) {
        this.ID = ID;
        this.name = name;
        this.area = area;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.description = description;
        this.location_name = location_name;
        this.invited_users = invited_users;
    }

//    public Event(String ID, String name, String area, String date, String time, String description, String location_name) {
//        this.ID = ID;
//        this.name = name;
//        this.area = area;
//        this.date = date;
//        this.time = time;
//        this.description = description;
//        this.location_name = location_name;
//    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getArea() { return area; }

    public String getLocation_name() { return location_name; }

    public String getID() {
        return ID;
    }

    public ArrayList<User> getInvited_users() { return invited_users; }

    public void setIsSafety(boolean safety) {
        this.isSafety = safety;
    }

    public boolean getIsSafety() {
        return isSafety;
    }

    public void setStartDate(String date) {
        this.startDate = date;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndDate(String date) {
        this.endDate = date;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndTime(String time) {
        this.endTime = time;
    }

    public String getEndTime() {
        return endTime;
    }
}
