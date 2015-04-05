package com.essentia.tracker.filter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;

import com.essentia.dbHelpers.LocationDBHelper;
import com.essentia.support.Constants;
import com.essentia.tracker.LocationListenerBase;

/**
 * Created by kyawzinlatt94 on 3/5/15.
 */
public class PersistentGpsLoggerListener extends LocationListenerBase implements
        Constants {
    private final java.lang.Object mLock;
    private ContentValues mKey;
    private LocationDBHelper locationDB;
    private SQLiteDatabase mDB;
    public PersistentGpsLoggerListener(Context context, SQLiteDatabase db, ContentValues _key) {
        this.mLock = new java.lang.Object();
        locationDB = new LocationDBHelper(context);
        setDB(db);
        setKey(_key);
    }

    public SQLiteDatabase getDB() {
        return mDB;
    }

    public void setDB(SQLiteDatabase _db) {
        mDB = _db;
    }

    public ContentValues getKey() {
        synchronized (mLock) {
            if (mKey == null)
                return null;
            return new ContentValues(mKey);
        }
    }

    public void setKey(ContentValues key) {
        synchronized (mLock) {
            if (key == null)
                mKey = null;
            else
                mKey = new ContentValues(key);
        }
    }

    @Override
    public void onLocationChanged(Location arg0) {
        super.onLocationChanged(arg0);
        onSyncLocationChanged(arg0);
    }

    public void onSyncLocationChanged(Location arg0) {
        ContentValues values;
        synchronized (mLock) {
            if (mKey == null)
                values = new ContentValues();
            else
                values = new ContentValues(mKey);
        }

        values.put(LocationDBHelper.TIME, arg0.getTime());
        values.put(LocationDBHelper.LATITUDE, (float) arg0.getLatitude());
        values.put(LocationDBHelper.LONGITUDE, (float) arg0.getLongitude());
        if (arg0.hasAccuracy()) {
            values.put(LocationDBHelper.ACCURANCY, arg0.getAccuracy());
        }
        if (arg0.hasSpeed()) {
            values.put(LocationDBHelper.SPEED, arg0.getSpeed());
        }
        if (arg0.hasAltitude()) {
            values.put(LocationDBHelper.ALTITUDE, (float) arg0.getAltitude());
        }
        if (arg0.hasBearing()) {
            values.put(LocationDBHelper.BEARING, arg0.getBearing());
        }
        if(mDB != null) {
            locationDB.create(mDB, locationDB, values);
        }
    }

    @Override
    public void onProviderDisabled(String arg0) {
        super.onProviderDisabled(arg0);
    }

    @Override
    public void onProviderEnabled(String arg0) {
        super.onProviderEnabled(arg0);
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        super.onStatusChanged(arg0, arg1, arg2);
    }
}
