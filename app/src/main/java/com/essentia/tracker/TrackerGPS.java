package com.essentia.tracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.essentia.workout.workout_pojos.TickListener;
import com.example.kyawzinlatt94.essentia.R;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.location.LocationManager.PASSIVE_PROVIDER;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public class TrackerGPS extends DefaultTrackerComponent implements TickListener {

    private final boolean mWithoutGps = false;
    private int frequency_ms = 0;
    private Location mLastLocation;
    private final Tracker tracker;

    public static final String NAME = "GPS";
    private GpsStatus mGpsStatus;
    private Callback mConnectCallback;

    @Override
    public String getName() {
        return NAME;
    }

    public TrackerGPS(Tracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public ResultCode onInit(final Callback callback, Context context) {
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (lm == null) {
                return ResultCode.RESULT_NOT_SUPPORTED;
            }
            if (lm.getProvider(LocationManager.GPS_PROVIDER) == null) {
                return ResultCode.RESULT_NOT_SUPPORTED;
            }
        } catch (Exception ex) {
            return ResultCode.RESULT_ERROR;
        }
        return ResultCode.RESULT_OK;
    }

    @Override
    public ResultCode onConnecting(final Callback callback, Context context) {
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            frequency_ms = Integer.valueOf(preferences.getString(context.getString(
                    R.string.pref_pollInterval), "500"));
            if (mWithoutGps == false) {
                String frequency_meters = preferences.getString(context.getString(
                        R.string.pref_pollDistance), "5");
                lm.requestLocationUpdates(GPS_PROVIDER,
                        frequency_ms,
                        Integer.valueOf(frequency_meters),
                        tracker);
                mGpsStatus = new GpsStatus(context);
                mGpsStatus.start(this);
                mConnectCallback = callback;
                return ResultCode.RESULT_PENDING;
            } else {
                String list[] = {
                        GPS_PROVIDER,
                        NETWORK_PROVIDER,
                        PASSIVE_PROVIDER };
                mLastLocation = null;
                for (String s : list) {
                    Location tmp = lm.getLastKnownLocation(s);
                    if (mLastLocation == null || tmp.getTime() > mLastLocation.getTime()) {
                        mLastLocation = tmp;
                    }
                }
                if (mLastLocation != null) {
                    mLastLocation.removeSpeed();
                    mLastLocation.removeAltitude();
                    mLastLocation.removeAccuracy();
                    mLastLocation.removeBearing();
                }
                gpsLessLocationProvider.run();
                return ResultCode.RESULT_OK;
            }


        } catch (Exception ex) {
            return ResultCode.RESULT_ERROR;
        }
    }

    @Override
    public boolean isConnected() {
        if (mWithoutGps)
            return true;

        if (mGpsStatus == null)
            return false;

        return mGpsStatus.isFixed();
    }

    @Override
    public ResultCode onEnd(Callback callback, Context context) {
        if (mWithoutGps == false) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            try {
                lm.removeUpdates(tracker);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (mGpsStatus != null) {
                mGpsStatus.stop(this);
            }
            mGpsStatus = null;
            mConnectCallback = null;
        }

        return ResultCode.RESULT_OK;
    }

    private final Runnable gpsLessLocationProvider = new Runnable() {

        Location location = null;
        final Handler handler = new Handler();

        @Override
        public void run() {
            if (location == null) {
                location = new Location(mLastLocation);
                mLastLocation = null;
            }
            location.setTime(System.currentTimeMillis());
            switch (tracker.getState()) {
                case INIT:
                case CLEANUP:
                case ERROR:
                    /* end loop be returning directly here */
                    return;
                case INITIALIZING:
                case INITIALIZED:
                case STARTED:
                case PAUSED:
                    /* continue looping */
                    break;
            }
            tracker.onLocationChanged(location);
            handler.postDelayed(this, frequency_ms);
        }
    };

    @Override
    public void onTick() {
        if (mGpsStatus == null)
            return;

        if (!mGpsStatus.isFixed())
            return;

        if (mConnectCallback == null)
            return;

        Callback tmp = mConnectCallback;

        mConnectCallback = null;
        mGpsStatus.stop(this);
        //note: Don't reset mGpsStatus, it's used for isConnected()

        tmp.run(this, ResultCode.RESULT_OK);
    }
}
