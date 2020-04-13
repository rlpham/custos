package com.example.custos.utils;

public class Event {

    private String ID;
    private String name;
    private String description;
    private String latitude;
    private String longitude;
    private String date_time;

    public Event(String ID, String name, String description, String date_time) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.date_time = date_time;
    }

    public String getName() {
        return name;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getID() {
        return ID;
    }

}
