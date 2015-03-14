package com.essentia.tracker.component;

import android.content.Context;
import android.location.LocationManager;

import com.essentia.tracker.GpsStatus;
import com.essentia.tracker.Tracker;

import static android.location.LocationManager.GPS_PROVIDER;

/**
 * Created by kyawzinlatt94 on 3/4/15.
 */
public class TrackerGPS {
    GpsStatus mGpsStatus;
    public TrackerComponent.ResultCode onInit(Context context){
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (lm == null) {
                return TrackerComponent.ResultCode.RESULT_NOT_SUPPORTED;
            }
            if (lm.getProvider(LocationManager.GPS_PROVIDER) == null) {
                return TrackerComponent.ResultCode.RESULT_NOT_SUPPORTED;
            }
        } catch (Exception ex) {
            return TrackerComponent.ResultCode.RESULT_ERROR;
        }
        return TrackerComponent.ResultCode.RESULT_OK;
    }

    public TrackerComponent.ResultCode onConnecting(Context context, Tracker tracker) {
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //5 metres between location update, 0.5 secs interval
            lm.requestLocationUpdates(GPS_PROVIDER, 500, 5, tracker);
                return TrackerComponent.ResultCode.RESULT_PENDING;
        } catch (Exception ex) {
            return TrackerComponent.ResultCode.RESULT_ERROR;
        }
    }
}
