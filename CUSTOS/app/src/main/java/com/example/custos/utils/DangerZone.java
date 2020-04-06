package com.example.custos.utils;

public class DangerZone {
    private String dangerZoneName;
    private String criticalLevel;
    private double latitude;
    private double longititude;
    private String description;

    public DangerZone(String dangerZoneName,String criticalLevel, double latitude, double longititude, String description) {
        this.dangerZoneName = dangerZoneName;
        this.criticalLevel = criticalLevel;
        this.latitude = latitude;
        this.longititude = longititude;
        this.description = description;
    }

    public String getDangerZoneName() {
        return dangerZoneName;
    }

    public void setDangerZoneName(String dangerZoneName) {
        this.dangerZoneName = dangerZoneName;
    }

    public String getCriticalLevel() {
        return criticalLevel;
    }

    public void setCriticalLevel(String criticalLevel) {
        this.criticalLevel = criticalLevel;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongititude() {
        return longititude;
    }

    public void setLongititude(double longititude) {
        this.longititude = longititude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
