package com.essentia.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.essentia.dbHelpers.ActivityDBHelper;
import com.essentia.dbHelpers.ActivityHRDetailDBHelper;
import com.essentia.dbHelpers.LocationDBHelper;
import com.essentia.hrm.HRProvider;
import com.essentia.notification.ForegroundNotificationDisplayStrategy;
import com.essentia.notification.NotificationState;
import com.essentia.notification.NotificationStateManager;
import com.essentia.notification.OngoingState;
import com.essentia.support.ApplicationContext;
import com.essentia.support.Constants;
import com.essentia.support.Scope;
import com.essentia.support.UserObject;
import com.essentia.tracker.component.TrackerComponent;
import com.essentia.tracker.component.TrackerGPS;
import com.essentia.tracker.component.TrackerHRM;
import com.essentia.tracker.component.TrackerTTS;
import com.essentia.tracker.filter.PersistentGpsLoggerListener;
import com.essentia.util.Formatter;
import com.essentia.util.HRZones;
import com.essentia.util.ValueModel;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */
public class Tracker extends android.app.Service implements
        LocationListener, Constants {
    public static HRZones hrZones;
    private final Handler handler = new Handler();
    private final Handler hrHandler = new Handler();
    private final boolean HRZ_UP = true;
    private final boolean HRZ_DOWN = false;
    TrackerGPS trackerGPS = new TrackerGPS();
    TrackerHRM trackerHRM = new TrackerHRM();
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
    int targetHRZone = 0;
    TrackerState trackerState;
    final ValueModel<TrackerState> state = new ValueModel<TrackerState>(TrackerState.INIT);
    int mLocationType = LocationDBHelper.TYPE_START;
    int totalHRDuration = 0;
    long hrZoneTimer = 0;
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
    private TrackerTTS textToSpeech;
    private boolean onDistanceTrigger;
    private int distanceMileStone;

    @Override
    public void onCreate() {
        locationDBHelper = new LocationDBHelper(this);
        hrDetailDBHelper = new ActivityHRDetailDBHelper(this);
        mDB = locationDBHelper.getWritableDatabase();
        notificationStateManager = new NotificationStateManager(
                new ForegroundNotificationDisplayStrategy(this));
        textToSpeech = new TrackerTTS(getApplicationContext(), this);
        try {
            hrZones = new HRZones(ApplicationContext.userObject);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        onDistanceTrigger = prefs.getBoolean(getResources().getString(R.string.cue_distance),false);
        if(onDistanceTrigger){
            String distance = prefs.getString(getResources().getString(R.string.cue_distance_interval), "500");
            distanceMileStone = Integer.valueOf(distance);
        }

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
        onStopTimer();
        reset();

    }

    public void setup(int targetHRZone) {
        setTargetMode(targetHRZone);
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

    private long createActivity(String sport) {
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
    public void setTargetMode(int targetHRZone){
        this.targetHRZone = targetHRZone;
    }
    public void start(Workout workout_) {
        textToSpeech.emitActivityState(TrackerTTS.STARTED);
        // connect workout and tracker
        this.workout = workout_;
        workout.setTracker(this);

        /**
         * create the DB activity
         */
        createActivity(workout.getSport());

        mElapsedDistance = 0;
        mHeartbeats = 0;
        mHeartbeatMillis = 0;
        mMaxHR = 0;
        // TODO: check if mLastLocation is recent enough
        mActivityLastLocation = null;

        // New location update will be tagged with START
        setNextLocationType(LocationDBHelper.TYPE_START);
        trackerState = TrackerState.STARTED;
        state.set(TrackerState.STARTED);

        activityOngoingState = new OngoingState(new Formatter(this), workout, this);

        /**
         * And finally let workout know that we started
         */
        workout.onStart(Scope.WORKOUT, this.workout);
    }

    public void pause() {
        textToSpeech.emitActivityState(TrackerTTS.PAUSED);
        trackerState = TrackerState.PAUSED;
        setNextLocationType(LocationDBHelper.TYPE_PAUSE);
        if (mActivityLastLocation != null) {
            /**
             * This saves mLastLocation as a PAUSE location
             */
            internalOnLocationChanged(mActivityLastLocation);
        }
        this.onPause();
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
        textToSpeech.emitActivityState(TrackerTTS.RESUMED);
        trackerState = TrackerState.STARTED;
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
        textToSpeech.emitActivityState(TrackerTTS.END);
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
        tmp.put(ActivityDBHelper.DISTANCE, getDistanceInKM());
        tmp.put(ActivityDBHelper.DURATION, getDurationLong());
        tmp.put(ActivityDBHelper.CALORIE, getCalorieBurned());
        tmp.put(ActivityDBHelper.AVG_PACE, getAvgPace());
        tmp.put(ActivityDBHelper.AVG_SPEED, getAvgSpeed());
        tmp.put(ActivityDBHelper.ZONE1_INFO, hrZones.getZone1DurationDetails());
        tmp.put(ActivityDBHelper.ZONE2_INFO, hrZones.getZone2DurationDetails());
        tmp.put(ActivityDBHelper.ZONE3_INFO, hrZones.getZone3DurationDetails());
        tmp.put(ActivityDBHelper.ZONE4_INFO, hrZones.getZone4DurationDetails());
        tmp.put(ActivityDBHelper.ZONE5_INFO, hrZones.getZone5DurationDetails());
        tmp.put(ActivityDBHelper.SPORT, workout.getSport());
        tmp.put(ActivityDBHelper.AVG_HR, getAvgHR());
        tmp.put(ActivityDBHelper.MAX_HR, mMaxHR);

        String key[] = {
                Long.toString(mActivityId)
        };
        mDB.update(ActivityDBHelper.TABLE, tmp, "id = ?", key);
    }
    private String getAvgSpeed(){
        double mElapsedSecs = mElapsedTimeMillis / 1000;
        double mElapsedDistanceKM = mElapsedDistance/1000;
        double speedInSec = mElapsedDistanceKM / mElapsedSecs;
        double speed = speedInSec * 3600;
        return String.format("%.2f",speed);
    }
    private String getDistanceInKM(){
        double km = mElapsedDistance/1000;
        return String.format("%.2f", km);
    }
    private String getAvgPace(){
        if(mElapsedDistance==0){
            return "0";
        }
        double mElapsedSecs = mElapsedTimeMillis / 1000;
        double mElapsedDistanceKM = mElapsedDistance/1000;
        int paceMins = (int)((mElapsedSecs/mElapsedDistanceKM)/60);
        int paceSec = (int)((mElapsedSecs/mElapsedDistanceKM)%60);
        return String.format("%02d", paceMins)+":"+String.format("%02d", paceSec);
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
        return Math.round(mElapsedDistance);
    }

    public Location getLastKnownLocation() {
        return mLastLocation;
    }

    public long getActivityId() {
        return mActivityId;
    }

    public void saveHRDetails(int hrValue, long timeStamp){
      try {
          if (hrValue == -1) {
              return;
          }
          ContentValues vals = new ContentValues();
          vals.put(ActivityHRDetailDBHelper.ACTIVITY_ID, mActivityId);
          vals.put(ActivityHRDetailDBHelper.HR_VALUE, hrValue);
          vals.put(ActivityHRDetailDBHelper.HR_ZONE, hrZones.getCurrentHRZone(hrValue));
          vals.put(ActivityHRDetailDBHelper.TIME_IN_MS, timeStamp);
          mDB.insert("activity_hr_detail", "nullColumnHack", vals);
      }catch(Exception e){
          e.printStackTrace();
      }
    }

    /**
     * Voice feed back implemented here
     * @param arg0
     */
    @Override
    public void onLocationChanged(Location arg0) {
       if(!trackerState.equals(TrackerState.STARTED)){
           return;
       }
        if (mActivityLastLocation != null) {
            double distDiff = arg0.distanceTo(mActivityLastLocation);
            mElapsedDistance += distDiff;

            // Emit Speech
            if(mElapsedDistance%distanceMileStone < 5 && mElapsedDistance != 0 && onDistanceTrigger){
                textToSpeech.emitIntervalFeedback();
            }
        }
            mActivityLastLocation = arg0;
            try {
                mDBWriter.onSyncLocationChanged(arg0);
            }catch(IllegalStateException e){
                e.printStackTrace();
                this.stopSelf();
            }

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
    // return minutes per km
    public Double getCurrentPace() {
        Double speed = getCurrentSpeed();
        if (speed == null)
            return 0.00;
        if (speed == 0.0)
            return 0.0;
        speed = speed * 3.6;
        double pace = 60/speed;
        //1 ms = 3.6 kmh
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(pace));
    }

    /**
     * Return meter per second on ground
     * @param now
     * @param maxAge
     * @return
     */
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
        hrHandler.postDelayed(updateHRThread, 1000);
    }
    public void onStopTimer(){
        timeSwapBuff += timeMs;
        handler.removeCallbacks(updateTimerThread);
        hrHandler.removeCallbacks(updateHRThread);
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

            duration = String.format("%02d", hours) + ":"+ String.format("%02d", mins) + ":" + String.format("%02d", secs);
            handler.postDelayed(this, 0);
        }
    };
    private Runnable updateHRThread = new Runnable() {

        @Override
        public void run() {
            //Save the hr details into database
            compHROperations();
            hrHandler.postDelayed(this, 1000);
        }
    };

    int previousSecs = 0;
    /**
     * Compute HR operations
     * 1. Add duration to each zone
     * 2. Add duration detail to each zone
     * 3. Save hr details along with the time stamp
     */
    public synchronized void compHROperations(){
        int hrValue = getCurrentHRValue();

        //Proceed only if hrValue is not null
        if(hrValue!=-1) {
            //Add duration to each zone
            populateZoneDurationData(hrZones.getCurrentHRZone(hrValue));

            //Add duration detail to each zone
            populateZoneDurationDetail(updatedTime, hrZones.getCurrentHRZone(hrValue));
            int dominant = (int) updatedTime/1000;
            //Save hr details
            saveHRDetails(hrValue, dominant*1000);
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
        int HR = Integer.valueOf(getAvgHR());
        UserObject userObject = ApplicationContext.userObject;

        if(userObject == null){return;}

        double weight = Double.valueOf(userObject.getWeight());
        int age = Integer.valueOf(userObject.getAge());

        if(HR==0){
            double calorieBurned =  ((mElapsedDistance/1000)* 0.62) * 0.63 * (weight * 2.2);
            burnedCalorie = (int) calorieBurned;
        }else{
            //With Heart Rate Data
            if(userObject.getGender().equals("Male")){
                double durationInHour = (getDurationLong()*2.777777778*Math.pow(10,-7));
                double burnedCalorieDouble = ((-55.0969 + (0.6309 * HR) + (0.1988 * weight) + (0.2017 *age))/4.184) *60 * durationInHour;
                burnedCalorie = (int) burnedCalorieDouble;
            }else{
                double durationInHour = (getDurationLong()*2.777777778*Math.pow(10,-7));
                double burnedCalorieDouble = ((-20.4022 + (0.4472 * HR) + (0.1263 * weight) + (0.074 *age))/4.184) *60 * durationInHour;
                burnedCalorie = (int) burnedCalorieDouble;
            }
        }

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
        if(mHeartbeats==0)
            return "0";
        double avgHR = mHeartbeats/hrCount;

        String txtAvgHR = (avgHR<1)?"0":String.valueOf((int)avgHR);
        return txtAvgHR;
    }

    /**
     * Populate the duration for each heart rate zone
     * @param currentHRZone Current HR zone
     */
    public void populateZoneDurationData(int currentHRZone){
        totalHRDuration += 1000;
        switch(currentHRZone){
            case HRZones.ZONE1:
                hrZones.addZonesDuration(1000, HRZones.ZONE1);
                break;
            case HRZones.ZONE2:
                hrZones.addZonesDuration(1000, HRZones.ZONE2);
                break;
            case HRZones.ZONE3:
                hrZones.addZonesDuration(1000, HRZones.ZONE3);
                break;
            case HRZones.ZONE4:
                hrZones.addZonesDuration(1000, HRZones.ZONE4);
                break;
            case HRZones.ZONE5:
                hrZones.addZonesDuration(1000, HRZones.ZONE5);
                break;
        }

    }

    private int previousHRZone = -1;

    public void populateZoneDurationDetail(long ms, int currentHRZone){
        //For the first time appending
        if(previousHRZone==-1){
            hrZoneTimer = SystemClock.currentThreadTimeMillis();
            appendDurationText(currentHRZone, Formatter.parseMsIntoTime(String.valueOf(ms)), 0);
            previousHRZone = currentHRZone;
            return;
        }
        if(previousHRZone == currentHRZone){
            int timeGap = (int)(SystemClock.currentThreadTimeMillis() - hrZoneTimer);
            if(isOutZone(currentHRZone) && timeGap>20000){
                textToSpeech.emitOutOfTargetHeartRateZone();
            }
            return;
        }else{
            //Emit speech on heart rate zone switching
            boolean isOutZone = isOutZone(currentHRZone);
            if(previousHRZone<currentHRZone){
                textToSpeech.emitHeartRateZoneShifted(HRZ_UP, isOutZone);
            }else{
                textToSpeech.emitHeartRateZoneShifted(HRZ_DOWN, isOutZone);
            }
            appendDurationText(previousHRZone, Formatter.parseMsIntoTime(String.valueOf(ms)), 1);
            appendDurationText(currentHRZone, Formatter.parseMsIntoTime(String.valueOf(ms)), 0);
            previousHRZone = currentHRZone;
        }
    }
    private boolean isOutZone(int currentZone){
      if(targetHRZone == 0) {
          return false;
      }else if(currentZone != targetHRZone){
           hrZoneTimer = SystemClock.currentThreadTimeMillis();
           return true;
      }else
          return false;
    }
    /**
     * Set Duration Detail Text for each heart rate zone
     * @param currentHRZone Heart Rate Zone to append the text
     * @param duration Duration text to be appended
     * @param txtPos Variable to decide to start new line for text
     *               0 to remain in the same line
     *               >0, more than to start a new line
     */
    public void appendDurationText(int currentHRZone, String duration, int txtPos){
        switch(currentHRZone){
            case HRZones.ZONE1:
                    if(txtPos==0)
                        hrZones.setZone1DurationDetails(duration +" - ");
                    else
                        hrZones.setZone1DurationDetails(duration +"\r\n");
                break;
            case HRZones.ZONE2:
                    if(txtPos==0)
                        hrZones.setZone2DurationDetails(duration +" - ");
                    else
                        hrZones.setZone2DurationDetails(duration +"\r\n");
                break;
            case HRZones.ZONE3:
                    if(txtPos==0)
                        hrZones.setZone3DurationDetails(duration +" - ");
                    else
                        hrZones.setZone3DurationDetails(duration +"\r\n");
                break;
            case HRZones.ZONE4:
                    if(txtPos==0)
                        hrZones.setZone4DurationDetails(duration +" - ");
                    else
                        hrZones.setZone4DurationDetails(duration +"\r\n");
                break;
            case HRZones.ZONE5:
                    if(txtPos==0)
                        hrZones.setZone5DurationDetails(duration +" - ");
                    else
                        hrZones.setZone5DurationDetails(duration +"\r\n");
                break;
        }
    }
    public String getCurrentZoneLevel(int hrValue){
        int hrZone = hrZones.getCurrentHRZone(hrValue);
        return hrZones.getZoneIntensityDescription(hrZone);
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
    public int getTotalHRDuration(){
        return totalHRDuration;
    }
}
