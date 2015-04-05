package com.essentia.workout.workout_pojos;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;

import com.essentia.support.ApplicationContext;
import com.essentia.support.Scope;
import com.essentia.tracker.Tracker;
import com.essentia.util.HRZones;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */

/**
 * This class is the top level object for a workout, it is being called by
 * WorkoutActivity, and by the Workout components
 */

@TargetApi(Build.VERSION_CODES.FROYO)
public class Workout{
    boolean paused = false;
    String sport = "Running";
    private boolean mute;

    Tracker tracker = null;
    HRZones hrZones = null;

    public Workout() {
        hrZones = new HRZones(ApplicationContext.userObject);
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }


    public void onEnd(Workout w) {
        assert (w == this);
    }

    public void onStart(Scope s, Workout w) {
        initFeedback();
        emitFeedback();
    }

    public void onTick() {
        initFeedback();
        emitFeedback();
    }

    public void onPause(Workout w) {
        initFeedback();
        tracker.pause();
        emitFeedback();
        paused = true;
    }

    public void onStop(Workout w) {
        initFeedback();
        tracker.completeActivity();
        emitFeedback();
    }

    public void onResume(Workout w) {
        initFeedback();
        tracker.resume();
        emitFeedback();
        paused = false;
    }

    public void onComplete(Scope s, Workout w) {
    }
    public boolean isPaused() {
        return paused;
    }


    public String getSport() {
        return sport;
    }
    public void setSport(String sport){
        this.sport = sport;
    }

    private void initFeedback() {

    }


    private void emitFeedback() {

    }

    public Location getLastKnownLocation() {
        return tracker.getLastKnownLocation();
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public boolean getMute() {
        return mute;
    }

    public HRZones getHRZones(){
        return this.hrZones;
    }

    public String getMetrics(String metricsRef){
        String result = "";
        switch (metricsRef){
            case "Heart Rate":
                return getCurrentHRValue();
            case "HRMax":
                if(tracker!=null){
                    return tracker.getMaxHR();
                }
                return "--";
            case "Distance":
                return getDistanceString();
            case "Calorie":
                if(tracker!=null){
                    return tracker.getCalorieBurned();
                }
                return "--";
            case "HRAvg":
                if(tracker!=null){
                    return tracker.getAvgHR();
                }
                return "--";

            case "Duration":
                if(tracker!=null){
                    return tracker.getDuration();
                }
                return "--";
            case "Speed":
                if(tracker!=null){
                    double speed = tracker.getCurrentSpeed();
                    return String.format("%.2f",speed);
                }
                return "--";
            case "Pace":
                if(tracker!=null){
                    return String.valueOf(tracker.getCurrentPace());
                }
                return "--";
        }
        return result;
    }
    private String getDistanceString(){
        double distance = 0;
        String distanceWithUnit;
        try {
            distance = tracker.getDistance() / 1000;
            distanceWithUnit = String.format("%.2f", distance) + " km";
            if (distance<1)
                distanceWithUnit = tracker.getDistance() + " m";
        }catch(Exception e){
            distanceWithUnit = "0 km";
        }
        return distanceWithUnit;
    }
    public String getCurrentHRValue(){
        String hrValue = String.valueOf(getCurrentHRValueInt());
        hrValue = (hrValue.equals("-1"))?"--":hrValue;
        return hrValue;
    }

    public int getCurrentHRValueInt(){
        Integer hrInt = 0;
        try {
            hrInt = tracker.getCurrentHRValue();
        }catch(NullPointerException e){
            e.printStackTrace();
            hrInt = 0;
        }
        hrInt = (hrInt==null)?-1:hrInt;
        return hrInt;
    }

}
