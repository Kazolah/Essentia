package com.essentia.workout.workout_pojos;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;

import com.essentia.support.Scope;
import com.essentia.tracker.Tracker;
import com.essentia.util.HRZones;
import com.essentia.workout.feedback.RUTextToSpeech;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */

/**
 * This class is the top level object for a workout, it is being called by
 * WorkoutActivity, and by the Workout components
 */

@TargetApi(Build.VERSION_CODES.FROYO)
public class Workout implements WorkoutComponent, WorkoutInfo {

    boolean paused = false;
//    int sport = DB.ACTIVITY_ID.SPORT_RUNNING;
    private boolean mute;
    class PendingFeedback {
        int depth = 0;
        final HashSet<Feedback> set = new HashSet<Feedback>(); // For uniquing

        void init() {
            depth++;
        }

        void add(Feedback f) {
            if (set.contains(f))
                return;
            set.add(f);

            try {
                f.emit(Workout.this, tracker.getApplicationContext());
            } catch (Exception ex) {
                // make sure that no small misstake crashes a workout...
                ex.printStackTrace();
            }
        }

        boolean end() {
            --depth;
            if (depth == 0) {
                set.clear();
//                Workout.this.textToSpeech.emit();
            }
            return depth == 0;
        }
    }

    final PendingFeedback pendingFeedback = new PendingFeedback();

    Tracker tracker = null;
    SharedPreferences audioCuePrefs;
    HRZones hrZones = null;
    RUTextToSpeech textToSpeech = null;

    public static final String KEY_TTS = "tts";
    public static final String KEY_COUNTER_VIEW = "CountdownView";
    public static final String KEY_FORMATTER = "Formatter";
    public static final String KEY_HRZONES = "HrZones";
    public static final String KEY_MUTE = "mute";

    public Workout() {
        hrZones = new HRZones();
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public void onInit(Workout w) {
//        for (Step a : steps) {
//            a.onInit(this);
//        }
    }

    public void onBind(Workout w, HashMap<String, Object> bindValues) {
//        if (bindValues.containsKey(Workout.KEY_HRZONES))
//            hrZones = (HRZones) bindValues.get(Workout.KEY_HRZONES);
        if (bindValues.containsKey(Workout.KEY_TTS))
            textToSpeech = (RUTextToSpeech) bindValues.get(Workout.KEY_TTS);
//        for (Step a : steps) {
//            a.onBind(w, bindValues);
//        }
    }

    public void onEnd(Workout w) {
        assert (w == this);

//        for (Step a : steps) {
//            a.onEnd(this);
//        }
    }

    @Override
    public void onRepeat(int current, int limit) {
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
    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public double get(Scope scope, Dimension d) {
        switch (d) {
            case DISTANCE:
                return getDistance(scope);
            case TIME:
                return getTime(scope);
            case SPEED:
                return getSpeed(scope);
            case PACE:
                return getPace(scope);
            case HR:
                return getHeartRate(scope);
            case HRZ:
                return getHeartRateZone(scope);
        }
        return 0;
    }

    @Override
    public double getDistance(Scope scope) {
        switch (scope) {
            case WORKOUT:
                return tracker.getDistance();
            case CURRENT:
                break;
        }
        return 0;
    }

    @Override
    public double getTime(Scope scope) {
        switch (scope) {
            case WORKOUT:
                return tracker.getTime();
            case CURRENT:
                return System.currentTimeMillis() / 1000; // now
        }
        return 0;
    }

    @Override
    public double getSpeed(Scope scope) {
        switch (scope) {
            case WORKOUT:
                double d = getDistance(scope);
                double t = getTime(scope);
                if (t == 0)
                    return (double) 0;
                return d / t;
            case CURRENT:
                Double s = tracker.getCurrentSpeed();
                if (s != null)
                    return s;
                return 0;
        }
        return 0;
    }

    @Override
    public double getPace(Scope scope) {
        double s = getSpeed(scope);
        if (s != 0)
            return 1.0d / s;
        return 0;
    }

    @Override
    public double getDuration(Scope scope, Dimension dimension) {
        return 0;
    }

    @Override
    public double getRemaining(Scope scope, Dimension dimension) {
        double curr = this.get(scope, dimension);
        double duration = this.getDuration(scope, dimension);
        if (duration > curr) {
            return duration - curr;
        } else {
            return 0;
        }
    }

    double getHeartbeats(Scope scope) {
        switch (scope) {
            case WORKOUT:
                return tracker.getHeartbeats();
            case CURRENT:
                return 0;
        }
        return 0;
    }

    @Override
    public double getHeartRate(Scope scope) {
        switch (scope) {
            case CURRENT: {
                Integer val = tracker.getCurrentHRValue();
                if (val == null)
                    return 0;
                return val;
            }
            case LAP:
            case STEP:
            case WORKOUT:
                break;
        }

        double t = getTime(scope); // in seconds
        double b = getHeartbeats(scope); // total (estimated) beats during
        // workout

        if (t != 0) {
            return (60 * b) / t; // bpm
        }
        return 0.0;
    }

    @Override
    public double getHeartRateZone(Scope scope) {
//        return hrZones.getZone(getHeartRate(scope));
        return 0.00;
    }

    @Override
    public int getSport() {
//        return sport;
        return 0;
    }

    @Override
    public boolean isEnabled(Dimension dim, Scope scope) {
        return false;
    }

    private void initFeedback() {
        pendingFeedback.init();
    }

    public void addFeedback(Feedback f) {
        pendingFeedback.add(f);
    }

    private void emitFeedback() {
        pendingFeedback.end();
    }



    /**
     * @return flattened list of all steps in workout
     */
    static public class StepListEntry {
        public StepListEntry(Step step, int level, Step parent) {
            this.level = level;
            this.step = step;
            this.parent = parent;
        }

        public final int level;
        public final Step parent;
        public final Step step;
    }


    @Override
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
                return String.valueOf(tracker.getDistance());
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
                    return String.valueOf(speed);
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

    public String getCurrentHRValue(){
        String hrValue = String.valueOf(getCurrentHRValueInt());
        hrValue = (hrValue.equals("-1"))?"--":hrValue;
        return hrValue;
    }

    public int getCurrentHRValueInt(){
        Integer hrInt = tracker.getCurrentHRValue();
        hrInt = (hrInt==null)?-1:hrInt;
        return hrInt;
    }

}
