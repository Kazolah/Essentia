package com.essentia.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Pair;

import com.example.kyawzinlatt94.essentia.R;

import java.util.Vector;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public class HRZones {

    int zones[] = null;
    final String key;
    final SharedPreferences prefs;

    public HRZones(Context ctx) {
        this(ctx.getResources(), PreferenceManager
                .getDefaultSharedPreferences(ctx));
    }

    public HRZones(Resources res, SharedPreferences p) {
        key = res.getString(R.string.pref_hrz_values);
        prefs = p;
        reload();
    }

    public void reload() {
        String str = prefs.getString(key, null);
        if (str != null) {
            zones = SafeParse.parseIntList(str);
        } else {
            zones = null;
        }
        if (zones != null) {
            System.err.print("loaded: (" + str + ")");
            for (int zone : zones) {
                System.err.print(" " + zone);
            }
            System.err.println("");
        }
    }

    public boolean isConfigured() {
        return zones != null;
    }

    public int getCount() {
        if (zones != null)
            return zones.length - 1;
        return 0;
    }

    public double getZone(double value) {
        if (zones != null) {
            int z = 0;
            for (z = 0; z < zones.length; z++) {
                if (zones[z] >= value)
                    break;
            }

            if (z == zones.length) {
                return z - 1;
            }
            double lo = (z == 0) ? 0 : zones[z - 1];
            double hi = zones[z];
            double add = (value - lo) / (hi - lo);
            System.err.println("value: " + value + ", z: " + z + ", lo: " + lo + ", hi: " + hi
                    + ", add: " + add);
            return z + add;
        }
        return 0;
    }

    public int getZoneInt(double value) {
        if (zones == null) {
            return 0;
        }

        int z = 0;
        for (z = 0; z < zones.length; z++) {
            if (zones[z] >= value)
                return z;
        }
        return z - 1;
    }

    public Pair<Integer, Integer> getHRValues(int zone) {
        if (zones != null && zone < zones.length) {
            if (zone == 0) {
                return new Pair<Integer, Integer>(0, zones[0]);
            } else {
                return new Pair<Integer, Integer>(zones[zone - 1], zones[zone]);
            }
        }
        return null;
    }

    public void save(Vector<Integer> vals) {
        zones = new int[vals.size()];
        for (int i = 0; i < zones.length; i++)
            zones[i] = vals.get(i);

        prefs.edit().putString(key, SafeParse.storeIntList(zones)).commit();
    }

    public void clear() {
        zones = null;
        prefs.edit().remove(key).commit();
    }

    /**
     * Find best matching HRZone give a min/max pair
     *
     * @param minValue
     * @param maxValue
     * @return
     */
    public int match(double minValue, double maxValue) {
        return (int)(getZone((minValue + maxValue) / 2) + 0.5);
    }
}