package com.essentia.util;

import android.util.Pair;

import com.essentia.support.UserObject;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public class HRZones {

    private static final int DEFAULT_MAX_HR = 250;
    private int hrMax;
    public static final int ZONE1 = 1;
    public static final int ZONE2 = 2;
    public static final int ZONE3 = 3;
    public static final int ZONE4 = 4;
    public static final int ZONE5 = 5;

    public static final String ZONE1_DES = "Recovery Zone";
    public static final String ZONE2_DES = "Fat Burning Zone";
    public static final String ZONE3_DES = "Fitness Zone";
    public static final String ZONE4_DES = "Increase Performance Zone";
    public static final String ZONE5_DES = "Maximize Performance Zone";

    public static final String ZONE1_IN_TXT = "Zone 1";
    public static final String ZONE2_IN_TXT = "Zone 2";
    public static final String ZONE3_IN_TXT = "Zone 3";
    public static final String ZONE4_IN_TXT = "Zone 4";
    public static final String ZONE5_IN_TXT = "Zone 5";

    public static final String ZONE1_LEVEL = "VERY LIGHT";
    public static final String ZONE2_LEVEL = "LIGHT";
    public static final String ZONE3_LEVEL = "MODERATE";
    public static final String ZONE4_LEVEL = "HARD";
    public static final String ZONE5_LEVEL = "MAXIMUM";

    public static final String ZONE5_ADV = "Develops maximum performance and speed";
    public static final String ZONE4_ADV = "Increases maximum performance capacity";
    public static final String ZONE3_ADV = "Improves aerobic fitness";
    public static final String ZONE2_ADV = "Improves basic endurance and fat burning";
    public static final String ZONE1_ADV = "Improves overall health and helps recovery";


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

    UserObject userObject;
    public HRZones(UserObject userObject){
        this.userObject = userObject;
        int maxHR = Integer.parseInt(userObject.getMaxHR());
        if( maxHR==0){
            hrMax = DEFAULT_MAX_HR - Integer.valueOf(userObject.getAge());
        }else{
            hrMax = maxHR;
        }
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
        zone1DurationDetails += data;
    }
    public void setZone2DurationDetails(String data){
        zone2DurationDetails += data;
    }
    public void setZone3DurationDetails(String data){
        zone3DurationDetails += data;
    }
    public void setZone4DurationDetails(String data){
        zone4DurationDetails += data;
    }
    public void setZone5DurationDetails(String data){
        zone5DurationDetails += data;
    }

    public String getZone1Duration(){
        return Formatter.parseMsIntoTimeWithUnit(String.valueOf(zone1Duration));
    }
    public String getZone2Duration(){
        return Formatter.parseMsIntoTimeWithUnit(String.valueOf(zone2Duration));
    }
    public String getZone3Duration(){
        return Formatter.parseMsIntoTimeWithUnit(String.valueOf(zone3Duration));
    }
    public String getZone4Duration(){
        return Formatter.parseMsIntoTimeWithUnit(String.valueOf(zone4Duration));
    }
    public String getZone5Duration(){
        return Formatter.parseMsIntoTimeWithUnit(String.valueOf(zone5Duration));
    }
    public void addZonesDuration(long milliSec, int currentHRZone){
        int ms = (int) milliSec;
        switch (currentHRZone){
            case HRZones.ZONE1:
                zone1Duration += ms;
                break;
            case HRZones.ZONE2:
                zone2Duration += ms;
                break;
            case HRZones.ZONE3:
                zone3Duration += ms;
                break;
            case HRZones.ZONE4:
                zone4Duration += ms;
                break;
            case HRZones.ZONE5:
                zone5Duration += ms;
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
    public static String getZoneIntensityDescription(int heartRateZone){
        switch (heartRateZone){
            case 1:
                return "Recovery Zone";
            case 2:
                return "Fat Burning Zone";
            case 3:
                return "Fitness Zone";
            case 4:
                return "Performance Zone";
            case 5:
                return "Maximizing Zone";
            default:
                return "Not available";
        }
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

    /**
     * Calculate zone percent for total duration
     * @param zoneDuration
     * @param totalTimeMs
     * @return
     */
    private double calculatePercentage(int zoneDuration, long totalTimeMs){
        if(totalTimeMs==0)
            return 0;
        else
            return ((double)zoneDuration/(double)totalTimeMs) * 100;
    }

    public static int getMaxHeartRateZone(Double age, String gender){
        double HRmax = 0;
        if(gender.equals("Male")){
            HRmax = 205.8 - (0.685 * age);
        }else {
            HRmax = 206 - (0.88 * age);
        }
        return (int) HRmax;
    }

}
