package com.essentia.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;

import com.essentia.dbHelpers.ActivityDBHelper;
import com.essentia.dbHelpers.ActivityHRDetailDBHelper;
import com.essentia.dbHelpers.LocationDBHelper;
import com.essentia.hrm.HRProvider;
import com.essentia.notification.ForegroundNotificationDisplayStrategy;
import com.essentia.notification.NotificationState;
import com.essentia.notification.NotificationStateManager;
import com.essentia.notification.OngoingState;
import com.essentia.support.Constants;
import com.essentia.support.Scope;
import com.essentia.tracker.component.TrackerComponent;
import com.essentia.tracker.component.TrackerGPS;
import com.essentia.tracker.component.TrackerHRM;
import com.essentia.tracker.filter.PersistentGpsLoggerListener;
import com.essentia.util.Formatter;
import com.essentia.util.HRZones;
import com.essentia.util.ValueModel;
import com.essentia.workout.workout_pojos.Workout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */
public class Tracker extends android.app.Service implements
        LocationListener, Constants {

    private final Handler handler = new Handler();

    TrackerGPS trackerGPS = new TrackerGPS();
    TrackerHRM trackerHRM = new TrackerHRM();
    HRZones hrZones;
    long mActivityId = 0;
    double mElapsedTimeMillis = 0;
    double mElapsedDistance = 0;
    double mHeartbeats = 0;
    double mHeartbeatMillis = 0; // since we might loose HRM connectivity...
    int mMaxHR = 0;
    int hrCount = 0;
    int burnedCalorie = 0;
    long previousTimeMillis = 0;
    final boolean mWithoutGps = false;

    TrackerState nextState;
    final ValueModel<TrackerState> state = new ValueModel<TrackerState>(TrackerState.INIT);
    int mLocationType = LocationDBHelper.TYPE_START;

    /**
     * Last location given by LocationManager
     */
    Location mLastLocation = null;

    /**
     * Last location given by LocationManager when in state STARTED
     */
    Location mActivityLastLocation = null;

    private PersistentGpsLoggerListener mDBWriter = null;
    PowerManager.WakeLock mWakeLock = null;

    private Workout workout = null;

    private NotificationStateManager notificationStateManager;

    private NotificationState activityOngoingState;
    private SQLiteDatabase mDB;
    private LocationDBHelper locationDBHelper;
    private ActivityHRDetailDBHelper hrDetailDBHelper;

    @Override
    public void onCreate() {
        locationDBHelper = new LocationDBHelper(this);
        hrDetailDBHelper = new ActivityHRDetailDBHelper(this);
        mDB = locationDBHelper.getWritableDatabase();
        notificationStateManager = new NotificationStateManager(
                new ForegroundNotificationDisplayStrategy(this));
        hrZones = new HRZones();
        wakeLock(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This service requires to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mDB != null) {
            mDB.close();
            mDB = null;
        }

        if (locationDBHelper != null) {
            locationDBHelper.close();
            locationDBHelper = null;
        }
        reset();
    }

    public void setup() {
        TrackerComponent.ResultCode result = trackerGPS.onInit(getApplicationContext());
        if (result == TrackerComponent.ResultCode.RESULT_OK) {
            connect();
            return;
        }
    }


    public void connect() {
        wakeLock(true);
        TrackerComponent.ResultCode result = trackerGPS.onConnecting(getApplicationContext(), this);
        if (result == TrackerComponent.ResultCode.RESULT_PENDING) {
            return;
        }
    }

    private long createActivity(int sport) {
        /**
         * Create an Activity instance
         */
        ContentValues tmp = new ContentValues();
        tmp.put(ActivityDBHelper.SPORT, sport);
        tmp.put(ActivityDBHelper.START_TIME, getStartTime());
        tmp.put(ActivityDBHelper.DATE, Formatter.getTodayDate());
        mActivityId = mDB.insert(ActivityDBHelper.TABLE, "nullColumnHack", tmp);

        tmp.clear();
        tmp.put(LocationDBHelper.ACTIVITY_ID, mActivityId);
        mDBWriter = new PersistentGpsLoggerListener(this, mDB, tmp);
        return mActivityId;
    }

    public void start(Workout workout_) {

        // connect workout and tracker
        this.workout = workout_;
        workout.setTracker(this);

        /**
         * create the DB activity
         */
        createActivity(workout.getSport());

        // do bindings
        doBind();

        // Let workout do initializations
        workout.onInit(workout);

        mElapsedDistance = 0;
        mHeartbeats = 0;
        mHeartbeatMillis = 0;
        mMaxHR = 0;
        // TODO: check if mLastLocation is recent enough
        mActivityLastLocation = null;

        // New location update will be tagged with START
        setNextLocationType(LocationDBHelper.TYPE_START);

        state.set(TrackerState.STARTED);

        activityOngoingState = new OngoingState(new Formatter(this), workout, this);

        /**
         * And finally let workout know that we started
         */
        workout.onStart(Scope.WORKOUT, this.workout);
    }

    private void doBind() {
        /**
         * Let components populate bindValues
         */
        HashMap<String, Object> bindValues = new HashMap<String, Object>();
        Context ctx = getApplicationContext();
        bindValues.put(TrackerComponent.KEY_CONTEXT, ctx);
//        bindValues.put(Workout.KEY_FORMATTER, new Formatter(ctx));
//        bindValues.put(Workout.KEY_HRZONES, new HRZones(ctx));
        bindValues.put(Workout.KEY_MUTE, new Boolean(workout.getMute()));

        /**
         * and then give them to workout
         */
        workout.onBind(workout, bindValues);
    }

    public void pause() {
        setNextLocationType(LocationDBHelper.TYPE_PAUSE);
        if (mActivityLastLocation != null) {
            /**
             * This saves mLastLocation as a PAUSE location
             */
            internalOnLocationChanged(mActivityLastLocation);
        }
        this.onPause();
        saveActivity();

    }

    public void stop() {
        setNextLocationType(LocationDBHelper.TYPE_PAUSE);
        if (mActivityLastLocation != null) {
            /**
             * This saves mLastLocation as a PAUSE location
             */
            internalOnLocationChanged(mActivityLastLocation);
        }
        this.onPause();
        saveActivity();
    }

    private void internalOnLocationChanged(Location arg0) {
        onLocationChanged(arg0);
    }

    public void resume() {
        switch (state.get()) {
            case INIT:
            case ERROR:
            case INITIALIZING:
            case CLEANUP:
            case INITIALIZED:
            case CONNECTING:
            case CONNECTED:
                assert (false);
                return;
            case PAUSED:
            case STOPPED:
                break;
            case STARTED:
                return;
        }
        onResume();
        // TODO: check is mLastLocation is recent enough
        mActivityLastLocation = mLastLocation;
        state.set(TrackerState.STARTED);
        setNextLocationType(LocationDBHelper.TYPE_RESUME);

        if (mActivityLastLocation != null) {
            /**
             * save last know location as resume location
             */
            internalOnLocationChanged(mActivityLastLocation);
        }

    }

    public void reset() {
        wakeLock(false);
        if (workout != null) {
            workout.setTracker(null);
            workout = null;
        }
    }


    public void completeActivity() {
        setNextLocationType(LocationDBHelper.TYPE_END);
        if (mActivityLastLocation != null) {
            mDBWriter.onLocationChanged(mActivityLastLocation);
        }
        saveActivity();
        notificationStateManager.cancelNotification();
        reset();
    }

    private void saveActivity() {
        ContentValues tmp = new ContentValues();
        tmp.put(ActivityDBHelper.DISTANCE, mElapsedDistance);
        tmp.put(ActivityDBHelper.DURATION, getDuration());
        tmp.put(ActivityDBHelper.AVG_HR, getAvgHR());
        if (mMaxHR > 0)
            tmp.put(ActivityDBHelper.MAX_HR, mMaxHR);

        String key[] = {
                Long.toString(mActivityId)
        };
        mDB.update(ActivityDBHelper.TABLE, tmp, "id = ?", key);
    }

    void setNextLocationType(int newType) {
        ContentValues key = mDBWriter.getKey();
        key.put(LocationDBHelper.TYPE, newType);
        mDBWriter.setKey(key);
        mLocationType = newType;
    }


    public double getTime() {
        return mElapsedTimeMillis / 1000;
    }

    public double getDistance() {
        return Math.round(mElapsedDistance)/1000;
    }

    public Location getLastKnownLocation() {
        return mLastLocation;
    }

    public long getActivityId() {
        return mActivityId;
    }

    public void saveHRDetails(int hrValue, long timeStamp){
        if(hrValue!=-1) {
            return;
        }
        ContentValues vals = new ContentValues();
        vals.put(ActivityHRDetailDBHelper.ACTIVITY_ID, mActivityId);
        vals.put(ActivityHRDetailDBHelper.HR_VALUE, hrValue);
        vals.put(ActivityHRDetailDBHelper.TIME_IN_MS, timeStamp);
        hrDetailDBHelper.create(hrDetailDBHelper, vals);
    }
    @Override
    public void onLocationChanged(Location arg0) {

        if (mActivityLastLocation != null) {
            double distDiff = arg0.distanceTo(mActivityLastLocation);
            mElapsedDistance += distDiff;
        }
            mActivityLastLocation = arg0;

            mDBWriter.onSyncLocationChanged(arg0);

            switch (mLocationType) {
                case LocationDBHelper.TYPE_START:
                case LocationDBHelper.TYPE_RESUME:
                    setNextLocationType(LocationDBHelper.TYPE_GPS);
                    break;
                case LocationDBHelper.TYPE_GPS:
                    break;
                case LocationDBHelper.TYPE_PAUSE:
                    break;
                case LocationDBHelper.TYPE_END:
                    assert (false);
                    break;
            }

        notificationStateManager.displayNotificationState(activityOngoingState);
        mLastLocation = arg0;
    }

    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String arg0) {
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public TrackerState getState() {
        return state.get();
    }

    public void registerTrackerStateListener(ValueModel.ChangeListener<TrackerState> listener) {
        state.registerChangeListener(listener);
    }

    public void unregisterTrackerStateListener(ValueModel.ChangeListener<TrackerState> listener) {
        state.unregisterChangeListener(listener);
    }

    /**
     * Service interface stuff...
     */
    public class LocalBinder extends android.os.Binder {
        public Tracker getService() {
            return Tracker.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void wakeLock(boolean get) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            mWakeLock = null;
        }
        if (get) {
            PowerManager pm = (PowerManager) this
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "Essentia");
            if (mWakeLock != null) {
                mWakeLock.acquire();
            }
        }
    }


    public Double getCurrentSpeed() {
        return getCurrentSpeed(System.currentTimeMillis(), 3000);
    }

    public Double getCurrentPace() {
        Double speed = getCurrentSpeed();
        if (speed == null)
            return 0.00;
        if (speed == 0.0)
            return 0.0;
        return 1000 / (speed * 60);
    }

    private Double getCurrentSpeed(long now, long maxAge) {
        if (mLastLocation == null)
            return 0.0;
        if (!mLastLocation.hasSpeed())
            return 0.0;
        if (now > mLastLocation.getTime() + maxAge)
            return 0.0;
        return (double) mLastLocation.getSpeed();
    }


    public Integer getCurrentBatteryLevel() {
        HRProvider hrProvider = trackerHRM.getHrProvider();
        if (hrProvider == null)
            return null;
        return hrProvider.getBatteryLevel();
    }


    public void onPause(){
        onStopTimer();
    }
    public void onResume(){
        onStartTimer();
    }

    public Workout getWorkout() {
        return workout;
    }

    private long timeMs = 0L;
    private long timeSwapBuff = 0L;
    private long startTime = 0L;
    private long updatedTime = 0L;
    private String duration = "00:00:00";

    public void onStartTimer(){
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimerThread, 0);
    }
    public void onStopTimer(){
        timeSwapBuff += timeMs;
        handler.removeCallbacks(updateTimerThread);
    }

    /**
     * Runnable to object to update the time and save hr details
     */
    private Runnable updateTimerThread = new Runnable(){

        @Override
        public void run() {
            duration = "";
            timeMs = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeMs;
            mElapsedTimeMillis = updatedTime;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            mins = mins % 60;
            if(hours>0){
                duration = String.format("%02d", hours) + ":";
            }
            duration += String.format("%02d", mins) + ":" + String.format("%02d", secs);

            //Save the hr details into database
            compHROperations();
            handler.postDelayed(this, 0);
        }
    };

    /**
     * Compute HR operations
     * 1. Add duration to each zone
     * 2. Save hr details along with the time stamp
     */
    public void compHROperations(){
        int hrValue = getCurrentHRValue();
        if(hrValue!=-1) {
            //Add duration to each zone
            populateZoneDurationData(updatedTime, hrZones.getCurrentHRZone(hrValue));

            //Save hr details
            saveHRDetails(hrValue, updatedTime);
        }
    }
    public String getDuration(){
        return duration;
    }
    public long getDurationLong(){
        return updatedTime;
    }


    public String getCalorieBurned(){
         calculateCalorie();
         return String.valueOf((int) burnedCalorie);
    }
    public void calculateCalorie(){
        double weight = 60;
        double calorieBurned = (mElapsedDistance/1000) * 0.63 * weight;
        burnedCalorie = (int) calorieBurned;
    }
    public String getMaxHR(){
        return String.valueOf(mMaxHR);
    }

    /**
     * Get the time when activity has started
     * @return Start Time
     */
    public String getStartTime(){
        Date myDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("KK:mm:ss a");
        return dateFormat.format(myDate);
    }

    public Integer getCurrentHRValue(long now, long maxAge) {
        HRProvider hrProvider = trackerHRM.getHrProvider();
        if (hrProvider == null)
            return -1;
        int hrValue = hrProvider.getHRValue();
        if (now > hrProvider.getHRValueTimestamp() + maxAge) {
            return -1;
        }
        mHeartbeats += hrValue;
        hrCount++;
        mMaxHR = Math.max(mMaxHR, hrValue);
        return hrValue;
    }

    public Integer getCurrentHRValue() {
        return getCurrentHRValue(System.currentTimeMillis(), 3000);
    }

    public double getHeartbeats() {
        return mHeartbeats;
    }

    public String getAvgHR(){
        double avgHR = mHeartbeats/hrCount;
        String txtAvgHR = (avgHR<1)?"--":String.valueOf(avgHR);
        return txtAvgHR;
    }

    /**
     * Populate the duration for each heart rate zone
     * @param ms Current duration
     * @param currentHRZone Current HR zone
     */
    public void populateZoneDurationData(long ms, int currentHRZone){
        long milliSec = ms - previousTimeMillis;
        switch(currentHRZone){
            case HRZones.ZONE1:
                hrZones.addZonesDuration(milliSec, HRZones.ZONE1);
                break;
            case HRZones.ZONE2:
                hrZones.addZonesDuration(milliSec, HRZones.ZONE2);
                break;
            case HRZones.ZONE3:
                hrZones.addZonesDuration(milliSec, HRZones.ZONE3);
                break;
            case HRZones.ZONE4:
                hrZones.addZonesDuration(milliSec, HRZones.ZONE4);
                break;
            case HRZones.ZONE5:
                hrZones.addZonesDuration(milliSec, HRZones.ZONE5);
                break;
        }
        previousTimeMillis = ms;
    }
    public String getZoneDurationData(int hrZone){
        switch(hrZone){
            case 1:
                return hrZones.getZone1DurationDetails();
            case 2:
                return hrZones.getZone2DurationDetails();
            case 3:
                return hrZones.getZone3DurationDetails();
            case 4:
                return hrZones.getZone4DurationDetails();
            case 5:
                return hrZones.getZone5DurationDetails();
        }
        return "";
    }
    public HRZones getHRZones(){
        return hrZones;
    }
}
