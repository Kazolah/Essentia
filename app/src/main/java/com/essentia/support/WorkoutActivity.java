package com.essentia.support;

import java.io.Serializable;

/**
 * Created by kyawzinlatt94 on 3/14/15.
 */
public class WorkoutActivity implements Serializable{
    private String id;
    private String date;
    private String userId;
    private String duration;
    private String calorie;
    private String distance;
    private String avgPace;
    private String avgSpeed;
    private String maxHR;
    private String avgHR;
    private String sport;
    private String startTime;
    private String zone1Info = "";
    private String zone2Info = "";
    private String zone3Info = "";
    private String zone4Info = "";
    private String zone5Info = "";

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(String avgPace) {
        this.avgPace = avgPace;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getMaxHR() {
        return maxHR;
    }

    public void setMaxHR(String maxHR) {
        this.maxHR = maxHR;
    }

    public String getAvgHR() {
        return avgHR;
    }

    public void setAvgHR(String avgHR) {
        this.avgHR = avgHR;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getZone1Info() {
        return zone1Info;
    }

    public void setZone1Info(String zone1Info) {
        this.zone1Info = zone1Info;
    }

    public String getZone2Info() {
        return zone2Info;
    }

    public void setZone2Info(String zone2Info) {
        this.zone2Info = zone2Info;
    }

    public String getZone3Info() {
        return zone3Info;
    }

    public void setZone3Info(String zone3Info) {
        this.zone3Info = zone3Info;
    }

    public String getZone4Info() {
        return zone4Info;
    }

    public void setZone4Info(String zone4Info) {
        this.zone4Info = zone4Info;
    }

    public String getZone5Info() {
        return zone5Info;
    }

    public void setZone5Info(String zone5Info) {
        this.zone5Info = zone5Info;
    }
}
