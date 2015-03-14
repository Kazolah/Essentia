package com.essentia.util;

import android.util.Pair;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public class HRZones {

    private static final int DEFAULT_MAX_HR = 250;
    private int hrMax = DEFAULT_MAX_HR;
    public static final int ZONE1 = 1;
    public static final int ZONE2 = 2;
    public static final int ZONE3 = 3;
    public static final int ZONE4 = 4;
    public static final int ZONE5 = 5;


    private String zone1DurationDetails = "";
    private String zone2DurationDetails = "";
    private String zone3DurationDetails = "";
    private String zone4DurationDetails = "";
    private String zone5DurationDetails = "";
    private int zone1Duration = 0;
    private int zone2Duration = 0;
    private int zone3Duration = 0;
    private int zone4Duration = 0;
    private int zone5Duration = 0;
    private int zone1Percent = 0;
    private int zone2Percent = 0;
    private int zone3Percent = 0;
    private int zone4Percent = 0;
    private int zone5Percent = 0;
    public HRZones(){

    }

    public int getCurrentHRZone(int hrValue){
        int HRZone = 1;
        int hrPercentage = getCurrentHRPercentage(hrValue);
        if(hrPercentage<=100 && hrPercentage>=90){
            HRZone = 5;
        }else if(hrPercentage<90 && hrPercentage>=80){
            HRZone = 4;
        }else if(hrPercentage<80 && hrPercentage>=70){
            HRZone = 3;
        }else if(hrPercentage<70 && hrPercentage>=60){
            HRZone = 2;
        }else {
            HRZone = 1;
        }
        return HRZone;
    }
    public Pair<Integer, Integer> getCurrentHRValueBound(int hrValue, int hrZone){
        Pair<Integer,Integer> hrBound;
        int lowerHRLimit;
        int higherHRLimit;
        switch(hrZone){
            case 1:
                higherHRLimit = (int)(hrMax * 0.59);
                hrBound = new Pair<Integer, Integer>(0,higherHRLimit);
                break;
            case 2:
                lowerHRLimit = (int)(hrMax* 0.6);
                higherHRLimit = (int)(hrMax * 0.69);
                hrBound = new Pair<Integer, Integer>(lowerHRLimit,higherHRLimit);
                break;
            case 3:
                lowerHRLimit = (int)(hrMax* 0.7);
                higherHRLimit = (int)(hrMax * 0.79);
                hrBound = new Pair<Integer, Integer>(lowerHRLimit,higherHRLimit);
                break;
            case 4:
                lowerHRLimit = (int)(hrMax* 0.8);
                higherHRLimit = (int)(hrMax * 0.89);
                hrBound = new Pair<Integer, Integer>(lowerHRLimit,higherHRLimit);
                break;
            case 5:
                lowerHRLimit = (int)(hrMax* 0.9);
                higherHRLimit = hrMax;
                hrBound = new Pair<Integer, Integer>(lowerHRLimit,higherHRLimit);
                break;
            default:
                higherHRLimit = (int)(hrMax * 0.45);
                hrBound = new Pair<Integer, Integer>(0,higherHRLimit);
                break;
        }
        return hrBound;
    }
    public  Pair<Integer, Integer> getCurrentHRPercentBound(int hrZone){
        Pair<Integer,Integer> hrBound;
        int lowerPercentLimit;
        int higherPercentLimit;
        switch(hrZone){
            case 0:
                lowerPercentLimit = 0;
                higherPercentLimit = 50;
                hrBound = new Pair<Integer, Integer>(lowerPercentLimit,higherPercentLimit);
                break;
            case 1:
                lowerPercentLimit = 50;
                higherPercentLimit = 60;
                hrBound = new Pair<Integer, Integer>(lowerPercentLimit,higherPercentLimit);
                break;
            case 2:
                lowerPercentLimit = 60;
                higherPercentLimit = 70;
                hrBound = new Pair<Integer, Integer>(lowerPercentLimit,higherPercentLimit);
                break;
            case 3:
                lowerPercentLimit = 70;
                higherPercentLimit = 80;
                hrBound = new Pair<Integer, Integer>(lowerPercentLimit,higherPercentLimit);
                break;
            case 4:
                lowerPercentLimit = 80;
                higherPercentLimit = 90;
                hrBound = new Pair<Integer, Integer>(lowerPercentLimit,higherPercentLimit);
                break;
            case 5:
                lowerPercentLimit = 90;
                higherPercentLimit = 100;
                hrBound = new Pair<Integer, Integer>(lowerPercentLimit,higherPercentLimit);
                break;
            default:
                lowerPercentLimit = 0;
                higherPercentLimit = 50;
                hrBound = new Pair<Integer, Integer>(lowerPercentLimit,higherPercentLimit);
                break;
        }
        return hrBound;
    }
    public int getCurrentHRPercentage(int hrValue){
        double ratio = (double)hrValue / hrMax;
        int hrPercentage =  (int)(ratio * 100);
        return hrPercentage;
    }
    public String getZone1DurationDetails(){
        return zone1DurationDetails;
    }
    public String getZone2DurationDetails(){
        return zone2DurationDetails;
    }
    public String getZone3DurationDetails(){
        return zone3DurationDetails;
    }
    public String getZone4DurationDetails(){
        return zone4DurationDetails;
    }
    public String getZone5DurationDetails(){
        return zone5DurationDetails;
    }

    public void setZone1DurationDetails(String data){
        zone1DurationDetails = "Zone 1:\n" + data;
    }
    public void setZone2DurationDetails(String data){
        zone2DurationDetails = "Zone 2:\n" + data;
    }
    public void setZone3DurationDetails(String data){
        zone3DurationDetails = "Zone 3:\n" + data;
    }
    public void setZone4DurationDetails(String data){
        zone4DurationDetails = "Zone 4:\n" + data;
    }
    public void setZone5DurationDetails(String data){
        zone5DurationDetails = "Zone 5:\n" + data;
    }

    public void addZonesDuration(long milliSec, int currentHRZone){
        int ms = (int) milliSec;
        switch (currentHRZone){
            case HRZones.ZONE1:
                zone1Duration += ms;
                setZone1DurationDetails(parseDuration(zone1Duration));
                break;
            case HRZones.ZONE2:
                zone2Duration += ms;
                setZone2DurationDetails(parseDuration(zone2Duration));
                break;
            case HRZones.ZONE3:
                zone3Duration += ms;
                setZone3DurationDetails(parseDuration(zone3Duration));
                break;
            case HRZones.ZONE4:
                zone4Duration += ms;
                setZone4DurationDetails(parseDuration(zone4Duration));
                break;
            case HRZones.ZONE5:
                zone5Duration += ms;
                setZone5DurationDetails(parseDuration(zone5Duration));
                break;
        }
    }
    private String parseDuration(int timeMs){
        String duration = "";
        int secs = (int) (timeMs / 1000);
        int mins = secs / 60;
        int hours = mins / 60;
        secs = secs % 60;
        mins = mins % 60;
        if(hours!=0) {
            duration = String.format("%02d", hours) + ":";
        }
        duration += String.format("%02d", mins) + ":" + String.format("%02d", secs);
        return duration;
    }
    public double getZonePercentage(int zone, long totalTimeMs){
        double percent = 0;
        switch(zone){
            case 1:
                percent = calculatePercentage(zone1Duration, totalTimeMs);
                break;
            case 2:
                percent = calculatePercentage(zone2Duration, totalTimeMs);
                break;
            case 3:
                percent = calculatePercentage(zone3Duration, totalTimeMs);
                break;
            case 4:
                percent = calculatePercentage(zone4Duration, totalTimeMs);
                break;
            case 5:
                percent = calculatePercentage(zone5Duration, totalTimeMs);
                break;
        }
        return percent;
    }
    private double calculatePercentage(int zoneDuration, long totalTimeMs){
        if(totalTimeMs==0)
            return 0;
        else
            return ((double)zoneDuration/(double)totalTimeMs) * 100;
    }
}
