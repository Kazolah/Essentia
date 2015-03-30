package com.essentia.tracker.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;

import com.essentia.tracker.Tracker;
import com.essentia.workout.feedback.RUTextToSpeech;
import com.example.kyawzinlatt94.essentia.R;

import java.util.Locale;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public class TrackerTTS{
    private TextToSpeech tts;
    private Context context;
    private RUTextToSpeech ruTTS;
    private Tracker tracker;
    private Vibrator vibrator;
    private int hrValue;
    private boolean pace, speed, duration, distance, calorie, heartRateZone, heartRate;
    public TrackerTTS(Context context, Tracker tracker){
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        this.context = context;
        this.tracker = tracker;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        pace = prefs.getBoolean(context.getString(R.string.cueinfo_total_pace), false);
        speed = prefs.getBoolean(context.getString(R.string.cueinfo_total_speed), false);
        duration = prefs.getBoolean(context.getString(R.string.cueinfo_total_time), false);
        distance = prefs.getBoolean(context.getString(R.string.cueinfo_total_distance), false);
        calorie = prefs.getBoolean(context.getString(R.string.cueinfo_total_calorie), false);
        heartRateZone = prefs.getBoolean(context.getString(R.string.cueinfo_total_hrz), false);
        heartRate = prefs.getBoolean(context.getString(R.string.cueinfo_total_hr), false);
    }
    public void emitIntervalFeedback(){
        String speech = "";
        hrValue = tracker.getCurrentHRValue();

        //Set false to heart rate related text to speech
        if(hrValue == -1){
            heartRate = false;
            heartRateZone = false;
        }
        if(duration){
            speech += getDuration();
        }
        if(distance){
            speech += getDistanceTTS();
        }
        if(heartRate){
            speech += getHrTTS(hrValue);
        }
        if(heartRateZone){
            speech += getHrzTTS();
        }
        if(calorie){
            speech += getCalorieTTS();
        }
        if(speed){
            speech += getSpeedTTS();
        }
        if(pace){
            speech += getPaceTTS();
        }
        tts.speak(speech, TextToSpeech.QUEUE_ADD, null);
    }
    public void emitHeartRateZoneShifted(boolean shiftUp){
        String textToSpeech = "";
        vibrator.vibrate(1000);
        if(shiftUp){
            textToSpeech = context.getResources().getText(R.string.hrz_shift_up).toString();
        }else{
            textToSpeech = context.getResources().getText(R.string.hrz_shift_down).toString();
        }
        tts.speak(textToSpeech, TextToSpeech.QUEUE_ADD, null);

    }
    RUTextToSpeech getTTS(SharedPreferences prefs) {
        final String mute = prefs.getString(context.getString(R.string.pref_mute), "no");
        ruTTS = new RUTextToSpeech(tts, mute, context);
        return ruTTS;
    }

    public String getCalorieTTS(){
        String speech = "Calorie Burned ";
        speech += tracker.getCalorieBurned();
        speech += " kilo calorie";
        return speech;
    }
    public String getDistanceTTS(){
        String speech = "Current Distance ";
        double distance = tracker.getDistance()/1000;
        if(distance>1)
            speech += String.format("%02d",distance) + " km";
        else
            speech += String.valueOf(distance) + " meter";
        return speech;
    }
    public String getPaceTTS(){
        String speech = "Current Pace";
        double pace = tracker.getCurrentPace();
        speech += String.valueOf(pace) + " minutes per kilometer";
        return speech;
    }
    public String getSpeedTTS(){
        String speech = "Current Speech ";
        double speed = tracker.getCurrentSpeed() * 3.6;
        speech += speed + " kmh";
        return speech;
    }
    public String getHrTTS(int hrValue){
        String speech = "Current Heart Rate ";
        double heartRate = hrValue;
        speech += heartRate + " beat per minute";
        return speech;
    }
    public String getHrzTTS(){
        String speech = "Heart Rate Zone ";
        String heartRateZone = tracker.getCurrentZoneLevel(hrValue);
        speech += heartRateZone;
        return speech;
    }
    public String getDuration(){
        String speech = "Current duration ";
        long duration = tracker.getDurationLong();

        int secs = (int) (duration / 1000);
        int mins = secs / 60;
        int hours = mins / 60;
        secs = secs % 60;
        mins = mins % 60;

        if(hours>0) {
            String hh = String.valueOf(hours) + " hours ";
            speech += hh;
        }
        if(mins>0){
            String mm = String.valueOf(mins) + " minutes ";
            speech += mm;
        }
        String ss = String.valueOf(secs) + " seconds ";
        speech += ss;
        return speech;
    }
}
