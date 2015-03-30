package com.essentia.dbHelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.essentia.orm.BaseDBHelper;
import com.essentia.orm.Fields;
import com.essentia.orm.Types;
import com.essentia.util.Formatter;
import com.essentia.util.Route;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kyawzinlatt94 on 3/5/15.
 */
public class LocationDBHelper extends BaseDBHelper{
    public static final String TABLE = "location";
    public static final String ACTIVITY_ID = "activity_id";
    public static final String TYPE = "type";
    public static final String TIME = "time";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ACCURANCY = "accurancy";
    public static final String ALTITUDE = "altitude";
    public static final String SPEED = "speed";
    public static final String BEARING = "bearing";


    public static final int TYPE_START = 1;
    public static final int TYPE_END = 2;
    public static final int TYPE_GPS = 3;
    public static final int TYPE_PAUSE = 4;
    public static final int TYPE_RESUME = 5;
    public static final int TYPE_DISCARD = 6;

    /**
     * Instantiates a new base db helper.
     *
     * @param context the context
     */
    public LocationDBHelper(Context context) {
        super(context);
        this.name = TABLE;
        columns.add(new Fields(TYPE, TYPE, Types.text()));
        columns.add(new Fields(TIME, TIME, Types.text()));
        columns.add(new Fields(LATITUDE, LATITUDE, Types.text()));
        columns.add(new Fields(LONGITUDE, LONGITUDE, Types.text()));
        columns.add(new Fields(ACCURANCY, ACCURANCY, Types.text()));
        columns.add(new Fields(ALTITUDE, ALTITUDE, Types.text()));
        columns.add(new Fields(SPEED, SPEED, Types.text()));
        columns.add(new Fields(BEARING, BEARING, Types.text()));
        columns.add(new Fields(ACTIVITY_ID, ACTIVITY_ID, Types.text()));
    }

    public Route loadRoute(Context context, long mID){
        Formatter formatter = new Formatter(context);
        final String[] from = new String[] {LATITUDE, LONGITUDE, TYPE, TIME };
        SQLiteDatabase mDB = getWritableDatabase();
        int cnt = 0;
        Route route = new Route();
        Cursor c = mDB.query(TABLE, from, "activity_id == " + mID,
                null, null, null, "id", null);
        if (c.moveToFirst()) {
            route = new Route();
            double acc_distance = 0;
            double tot_distance = 0;
            int cnt_distance = 0;
            LatLng lastLocation = null;
            long lastTime = 0;
            do {
                cnt++;
                LatLng point = new LatLng(c.getDouble(0), c.getDouble(1));
                route.path.add(point);
                route.bounds.include(point);
                int type = c.getInt(2);
                long time = c.getLong(3);
                MarkerOptions markerOptions;
                switch (type) {
                    case TYPE_START:
                    case TYPE_END:
                    case TYPE_PAUSE:
                    case TYPE_RESUME:
                        if (type == TYPE_PAUSE)
                        {
                            lastTime = 0;
                        }
                        else if (type == TYPE_RESUME)
                        {
                            lastTime = time;
                        }
                        markerOptions = new MarkerOptions();
                        markerOptions.position((lastLocation = point));
                        markerOptions.title(type == TYPE_START ? "Start" :
                                type == TYPE_END ? "Stop" :
                                        type == TYPE_PAUSE ? "Pause" :
                                                type == TYPE_RESUME ? "Resume"
                                                        : "<Unknown>");
                        markerOptions.snippet(null);
                        markerOptions.draggable(false);
                        route.markers.add(markerOptions);
                        break;
                    case TYPE_GPS:
                        float res[] = {
                                0
                        };
                        Location.distanceBetween(lastLocation.latitude,
                                lastLocation.longitude, point.latitude, point.longitude,
                                res);
                        acc_distance += res[0];
                        tot_distance += res[0];

                        lastTime = time;

                        if (acc_distance >= formatter.getUnitMeters()) {
                            cnt_distance++;
                            acc_distance = 0;
                            markerOptions = new MarkerOptions();
                            markerOptions.position(point);
                            markerOptions.title("" + cnt_distance + " " + formatter.getUnitString());
                            markerOptions.snippet(null);
                            markerOptions.draggable(false);
                            route.markers.add(markerOptions);
                        }
                        lastLocation = point;
                        break;
                }
            } while (c.moveToNext());
            System.err.println("Finished loading " + cnt + " points");
        }
        c.close();
        mDB.close();
        return route;
    }
}
