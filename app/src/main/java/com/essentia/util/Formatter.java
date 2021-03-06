package com.essentia.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import com.example.kyawzinlatt94.essentia.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kyawzinlatt94 on 2/24/15.
 */
public class Formatter implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static String formatLowerLimitHRZones(int percent, String zone){
        return percent + " % - "+zone;
    }
    public static String formatLimitHRZones(int percent, String zone){
        return percent + " % - "+zone;
    }


    Context context = null;
    Resources resources = null;
    SharedPreferences sharedPreferences = null;
    java.text.DateFormat dateFormat = null;
    java.text.DateFormat timeFormat = null;
    HRZones hrZones = null;

    boolean km = true;
    String base_unit = "km";
    double base_meters = km_meters;

    public final static double km_meters = 1000.0;
    public final static double mi_meters = 1609.34;
    public final static double FEETS_PER_METER = 3.2808;

    public static final int CUE = 1; // for text to speech
    public static final int CUE_SHORT = 2; // brief for tts
    public static final int CUE_LONG = 3; // long for tts
    public static final int TXT = 4; // same as TXT_SHORT
    public static final int TXT_SHORT = 5; // brief for printing
    public static final int TXT_LONG = 6; // long for printing

    public Formatter(Context ctx) {
        context = ctx;
        resources = ctx.getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        dateFormat = android.text.format.DateFormat.getDateFormat(ctx);
        timeFormat = android.text.format.DateFormat.getTimeFormat(ctx);

        setUnit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key != null && context.getString(R.string.pref_unit).contentEquals(key))
            setUnit();
    }

    private void setUnit() {
        km = getUseKilometers(context.getResources(), sharedPreferences, null);

        if (km) {
            base_unit = "km";
            base_meters = km_meters;
        } else {
            base_unit = "mi";
            base_meters = mi_meters;
        }
    }

    public static boolean getUseKilometers(Resources res, SharedPreferences prefs, SharedPreferences.Editor editor) {
        boolean _km = true;
        String unit = prefs.getString(res.getString(R.string.pref_unit), null);
        if (unit == null)
            _km = guessDefaultUnit(res, prefs, editor);
        else if (unit.contentEquals("km"))
            _km = true;
        else if (unit.contentEquals("mi"))
            _km = false;
        else
            _km = guessDefaultUnit(res, prefs, editor);

        return _km;
    }

    private static boolean guessDefaultUnit(Resources res, SharedPreferences prefs, SharedPreferences.Editor editor) {
        String countryCode = Locale.getDefault().getCountry();
        System.err.println("guessDefaultUnit: countryCode: " + countryCode);
        if (countryCode == null)
            return true; // km;
        String key = res.getString(R.string.pref_unit);
        if ("US".contentEquals(countryCode) ||
                "GB".contentEquals(countryCode)) {
            if (editor != null)
                editor.putString(key, "mi");
            return false;
        }
        else {
            if (editor != null)
                editor.putString(key, "km");
        }
        return true;
    }

    public double getUnitMeters() {
        return this.base_meters;
    }

    public static double getUnitMeters(Resources res, SharedPreferences prefs) {
        if (getUseKilometers(res, prefs, null))
            return km_meters;
        else
            return mi_meters;
    }

    public String getUnitString() {
        return this.base_unit;
    }


    public String formatElapsedTime(int target, long seconds) {
        switch (target) {
            case CUE:
            case CUE_SHORT:
                return cueElapsedTime(seconds, false);
            case CUE_LONG:
                return cueElapsedTime(seconds, true);
            case TXT:
            case TXT_SHORT:
                return DateUtils.formatElapsedTime(seconds);
            case TXT_LONG:
                return txtElapsedTime(seconds);
        }
        return "";
    }

    private String cueElapsedTime(long seconds, boolean includeDimension) {
        long hours = 0;
        long minutes = 0;
        if (seconds >= 3600) {
            hours = seconds / 3600;
            seconds -= hours * 3600;
        }
        if (seconds >= 60) {
            minutes = seconds / 60;
            seconds -= minutes * 60;
        }
        StringBuilder s = new StringBuilder();
        if (hours > 0) {
            includeDimension = true;
            s.append(hours)
                    .append(" ")
                    .append(resources.getQuantityString(R.plurals.cue_hour, (int) hours));
        }
        if (minutes > 0) {
            if (hours > 0)
                s.append(" ");
            includeDimension = true;
            s.append(minutes)
                    .append(" ")
                    .append(resources.getQuantityString(R.plurals.cue_minute, (int)minutes));
        }
        if (seconds > 0) {
            if (hours > 0 || minutes > 0)
                s.append(" ");

            if (includeDimension) {
                s.append(seconds)
                        .append(" ")
                        .append(resources.getQuantityString(R.plurals.cue_second, (int)seconds));
            } else {
                s.append(seconds);
            }
        }
        return s.toString();
    }

    private String txtElapsedTime(long seconds) {
        long hours = 0;
        long minutes = 0;
        if (seconds >= 3600) {
            hours = seconds / 3600;
            seconds -= hours * 3600;
        }
        if (seconds >= 60) {
            minutes = seconds / 60;
            seconds -= minutes * 60;
        }
        StringBuilder s = new StringBuilder();
        if (hours > 0) {
            s.append(hours).append(" ").append(resources.getString(R.string.metrics_elapsed_h));
        }
        if (minutes > 0) {
            if (hours > 0)
                s.append(" ");
            if (hours > 0 || seconds > 0)
                s.append(minutes).append(" ").append(resources.getString(R.string.metrics_elapsed_m));
            else
                s.append(minutes).append(" ").append(resources.getString(R.string.metrics_elapsed_min));
        }
        if (seconds > 0) {
            if (hours > 0 || minutes > 0)
                s.append(" ");
            s.append(seconds).append(" ").append(resources.getString(R.string.metrics_elapsed_s));
        }
        return s.toString();
    }

    /**
     * Format heart rate
     *
     * @param target
     * @param heart_rate
     * @return
     */
    public String formatHeartRate(int target, double heart_rate) {
        switch (target) {
            case CUE:
            case CUE_SHORT:
            case CUE_LONG:
                return Integer.toString((int) Math.round(heart_rate)) + " "
                        + resources.getQuantityString(R.plurals.cue_bpm, (int)heart_rate);
            case TXT:
            case TXT_SHORT:
            case TXT_LONG:
                return Integer.toString((int) Math.round(heart_rate));
        }
        return "";
    }

    private String formatHeartRateZone(int target, double hrZone) {
        switch (target) {
            case TXT:
            case TXT_SHORT:
                return Integer.toString((int) Math.round(hrZone));
            case TXT_LONG:
                return Double.toString(Math.round(10.0 * hrZone) / 10.0);
            case CUE_SHORT:
                return resources.getString(R.string.heartrate_zone) + " "
                        + Integer.toString((int) Math.floor(hrZone));
            case CUE:
            case CUE_LONG:
                return resources.getString(R.string.heartrate_zone) + " "
                        + Double.toString(Math.floor(10.0 * hrZone) / 10.0);
        }
        return "";
    }

    /**
     * Format pace
     *
     * @param target
     * @param seconds_per_meter
     * @return
     */
    public String formatPace(int target, double seconds_per_meter) {
        switch (target) {
            case CUE:
            case CUE_SHORT:
            case CUE_LONG:
                return cuePace(seconds_per_meter);
            case TXT:
            case TXT_SHORT:
                return txtPace(seconds_per_meter, false);
            case TXT_LONG:
                return txtPace(seconds_per_meter, true);
        }
        return "";
    }

    /**
     * @param
     * @return string suitable for printing according to settings
     */
    private String txtPace(double seconds_per_meter, boolean includeUnit) {
        long val = Math.round(base_meters * seconds_per_meter);
        String str = DateUtils.formatElapsedTime(val);
        if (includeUnit == false)
            return str;
        else {
            int res = km ? R.string.metrics_distance_km : R.string.metrics_distance_mi;
            return str + "/" + resources.getString(res);
        }
    }

    private String cuePace(double seconds_per_meter) {
        long seconds_per_unit = Math.round(base_meters * seconds_per_meter);
        long hours_per_unit = 0;
        long minutes_per_unit = 0;
        if (seconds_per_unit >= 3600) {
            hours_per_unit = seconds_per_unit / 3600;
            seconds_per_unit -= hours_per_unit * 3600;
        }
        if (seconds_per_unit >= 60) {
            minutes_per_unit = seconds_per_unit / 60;
            seconds_per_unit -= minutes_per_unit * 60;
        }
        StringBuilder s = new StringBuilder();
        if (hours_per_unit > 0) {
            s.append(hours_per_unit)
                    .append(" ")
                    .append(resources.getQuantityString(R.plurals.cue_hour, (int)hours_per_unit));
        }
        if (minutes_per_unit > 0) {
            if (hours_per_unit > 0)
                s.append(" ");
            s.append(minutes_per_unit)
                    .append(" ")
                    .append(resources.getQuantityString(R.plurals.cue_minute, (int)minutes_per_unit));
        }
        if (seconds_per_unit > 0) {
            if (hours_per_unit > 0 || minutes_per_unit > 0)
                s.append(" ");
            s.append(seconds_per_unit)
                    .append(" ")
                    .append(resources.getQuantityString(R.plurals.cue_second, (int)seconds_per_unit));
        }
        s.append(" ").append(resources.getString(km ? R.string.cue_perkilometer : R.string.cue_permile));
        return s.toString();
    }

    /**
     * Format Speed
     *
     * @param target
     * @param seconds_per_meter
     * @return
     */
    public String formatSpeed(int target, double seconds_per_meter) {
        switch (target) {
            case CUE:
            case CUE_SHORT:
            case CUE_LONG:
                return cueSpeed(seconds_per_meter);
            case TXT:
            case TXT_SHORT:
                return txtSpeed(seconds_per_meter, false);
            case TXT_LONG:
                return txtSpeed(seconds_per_meter, true);
        }
        return "";
    }

    /**
     * @param
     * @return string suitable for printing according to settings
     */
    private String txtSpeed(double seconds_per_meter, boolean includeUnit) {
        double meter_per_seconds = 1/seconds_per_meter;
        double distance_per_seconds = meter_per_seconds / base_meters;
        double distance_per_hour = distance_per_seconds * 3600;
        String str = String.format("%.1f", distance_per_hour);
        if (includeUnit == false)
            return str;
        else {
            int res = km ? R.string.metrics_distance_km : R.string.metrics_distance_mi;
            return str + resources.getString(res) + "/h";
        }
    }

    private String cueSpeed(double seconds_per_meter) {
        long seconds_per_unit = Math.round(base_meters * seconds_per_meter);
        long hours_per_unit = 0;
        long minutes_per_unit = 0;
        if (seconds_per_unit >= 3600) {
            hours_per_unit = seconds_per_unit / 3600;
            seconds_per_unit -= hours_per_unit * 3600;
        }
        if (seconds_per_unit >= 60) {
            minutes_per_unit = seconds_per_unit / 60;
            seconds_per_unit -= minutes_per_unit * 60;
        }
        StringBuilder s = new StringBuilder();
        if (hours_per_unit > 0) {
            s.append(hours_per_unit)
                    .append(" ")
                    .append(resources.getQuantityString(R.plurals.cue_hour, (int)hours_per_unit));
        }
        if (minutes_per_unit > 0) {
            if (hours_per_unit > 0)
                s.append(" ");
            s.append(minutes_per_unit)
                    .append(" ")
                    .append(resources.getQuantityString(R.plurals.cue_minute, (int)minutes_per_unit));
        }
        if (seconds_per_unit > 0) {
            if (hours_per_unit > 0 || minutes_per_unit > 0)
                s.append(" ");
            s.append(seconds_per_unit)
                    .append(" ")
                    .append(resources.getQuantityString(R.plurals.cue_second, (int)seconds_per_unit));
        }
        s.append(" ").append(resources.getString(km ? R.string.cue_perkilometer : R.string.cue_permile) + "/h");
        return s.toString();
    }

    /**
     * @param target
     * @param seconds_since_epoch
     * @return
     */
    public String formatDateTime(int target, long seconds_since_epoch) {
        // ignore target
        StringBuilder s = new StringBuilder();
        s.append(dateFormat.format(seconds_since_epoch * 1000)); // takes
        // milliseconds
        // as argument
        s.append(" ");
        s.append(timeFormat.format(seconds_since_epoch * 1000));
        return s.toString();
    }

    /**
     * @param target
     * @param meters
     * @return
     */
    public String formatDistance(int target, long meters) {
        switch (target) {
            case CUE:
            case CUE_LONG:
            case CUE_SHORT:
                return cueDistance(meters, false);
            case TXT:
            case TXT_SHORT:
                return cueDistance(meters, true);
            case TXT_LONG:
                return Long.toString(meters) + " m";
        }
        return null;
    }

    private String cueDistance(long meters, boolean txt) {
        double base_val = km_meters; // 1km
        double decimals = 2;
        if (!km) {
            base_val = mi_meters;
        }

        StringBuilder s = new StringBuilder();
        if (meters >= base_val) {
            double base = ((double) meters) / base_val;
            double val = round(base, decimals);
            if (txt) {
                s.append(val).append(" ")
                        .append(resources.getString(km ? R.string.metrics_distance_km : R.string.metrics_distance_mi));
            } else {
                s.append(val).append(" ")
                        .append(resources.getQuantityString(km ? R.plurals.cue_kilometer : R.plurals.cue_mile, (int)val));
            }
        } else {
            s.append(meters);
            s.append(" ").append(txt ? "m" : resources.getQuantityString(R.plurals.cue_meter, (int)meters));
        }
        return s.toString();
    }

    public String formatRemainingTime(int target, double value) {
        return formatElapsedTime(target, Math.round(value));
    }

    public String formatRemainingDistance(int target, double value) {
        return formatDistance(target, Math.round(value));
    }

    public String formatName(String first, String last) {
        if (first != null && last != null)
            return first + " " + last;
        else if (first == null && last != null)
            return last;
        else if (first != null && last == null)
            return first;
        return "";
    }

    public String formatTime(int target, long seconds_since_epoch) {
        return timeFormat.format(seconds_since_epoch * 1000);
    }

    public static double round(double base, double decimals) {
        double exp = Math.pow(10, decimals);
        return Math.round(base * exp) / exp;
    }

    public static double getUnitMeters(Context mContext) {
        return getUnitMeters(mContext.getResources(),
                PreferenceManager.getDefaultSharedPreferences(mContext));
    }

    public static String getTodayDate(){
        Date myDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(myDate);
    }
    public static String parseMsIntoTime(String time){
        String duration = "";
        int updatedTime = Integer.valueOf(time);
        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        int hours = mins / 60;
        secs = secs % 60;
        mins = mins % 60;
        duration += String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs);
        return duration;
    }
    public static String parseMsIntoTimeWithUnit(String time){
        String duration = "";
        int updatedTime = Integer.valueOf(time);
        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        int hours = mins / 60;
        secs = secs % 60;
        mins = mins % 60;
        if(hours>0)
            duration = String.format("%02d", hours) + " h " ;
        if(mins>0)
            duration += String.format("%02d", mins) + " m ";
        duration += String.format("%02d", secs) +" s";
        return duration;
    }
    public static String parseDate(String date){
        String formattedDate = "";
        SimpleDateFormat pFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat nFormat = new SimpleDateFormat("MMMM dd, yyyy");
        try{
            formattedDate = nFormat.format(pFormat.parse(date));
        }catch(ParseException e){
            e.printStackTrace();
        }
        return formattedDate;
    }

}
