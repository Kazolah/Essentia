package com.essentia.plans;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.essentia.main.MainActivity;
import com.example.kyawzinlatt94.essentia.R;

import java.util.Calendar;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class PlansFragment extends Fragment{
    private MainActivity mainActivity;
    private Context context;
    private Button btnSavePlan;
    private ImageView ivMon, ivTue, ivWed, ivThurs, ivFri, ivSat, ivSun;
    private TimePicker timePicker;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private Boolean onMon, onTue, onWed, onThurs, onFri, onSat, onSun;
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = mainActivity.getApplicationContext();
        sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plans, container, false);
        btnSavePlan = (Button) rootView.findViewById(R.id.btnPlansSave);
        btnSavePlan.setOnClickListener(btnSavePlanListener);
        ivMon = (ImageView) rootView.findViewById(R.id.iv_MON);
        onMon = sharedPrefs.getBoolean(getString(R.string.pref_monday),false);
        if(onMon){
            ivMon.setImageResource(R.drawable.ic_monday_on);
        }else{
            ivMon.setImageResource(R.drawable.ic_monday_off);
        }
        ivMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onMon){
                    ivMon.setImageResource(R.drawable.ic_monday_on);
                    onMon = true;
                }else{
                    ivMon.setImageResource(R.drawable.ic_monday_off);
                    onMon = false;
                }
            }
        });
        ivTue = (ImageView) rootView.findViewById(R.id.iv_TUE);
        onTue = sharedPrefs.getBoolean(getString(R.string.pref_tuesday),false);
        if(onTue){
            ivTue.setImageResource(R.drawable.ic_tuesday_on);
        }else{
            ivTue.setImageResource(R.drawable.ic_tuesday_off);
        }
        ivTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onTue){
                    ivTue.setImageResource(R.drawable.ic_tuesday_on);
                    onTue = true;
                }else{
                    ivTue.setImageResource(R.drawable.ic_tuesday_off);
                    onTue = false;
                }
            }
        });

        ivWed = (ImageView) rootView.findViewById(R.id.iv_WED);
        onWed = sharedPrefs.getBoolean(getString(R.string.pref_wednesday),false);
        if(onWed){
            ivWed.setImageResource(R.drawable.ic_wednesday_on);
        }else{
            ivWed.setImageResource(R.drawable.ic_wednesday_off);
        }
        ivWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onWed){
                    ivWed.setImageResource(R.drawable.ic_wednesday_on);
                    onWed = true;
                }else{
                    ivWed.setImageResource(R.drawable.ic_wednesday_off);
                    onWed = false;
                }
            }
        });

        ivThurs = (ImageView) rootView.findViewById(R.id.iv_THURS);
        onThurs = sharedPrefs.getBoolean(getString(R.string.pref_thursday),false);
        if(onThurs){
            ivThurs.setImageResource(R.drawable.ic_thrusday_on);
        }else{
            ivThurs.setImageResource(R.drawable.ic_thrusday_off);
        }
        ivThurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onThurs){
                    ivThurs.setImageResource(R.drawable.ic_thrusday_on);
                    onThurs = true;
                }else{
                    ivThurs.setImageResource(R.drawable.ic_thrusday_off);
                    onThurs = false;
                }
            }
        });

        ivFri = (ImageView) rootView.findViewById(R.id.iv_Fri);
        onFri = sharedPrefs.getBoolean(getString(R.string.pref_friday),false);
        if(onFri){
            ivFri.setImageResource(R.drawable.ic_friday_on);
        }else{
            ivFri.setImageResource(R.drawable.ic_friday_off);
        }
        ivFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onFri){
                    ivFri.setImageResource(R.drawable.ic_friday_on);
                    onFri = true;
                }else{
                    ivFri.setImageResource(R.drawable.ic_friday_off);
                    onFri = false;
                }
            }
        });

        ivSat = (ImageView) rootView.findViewById(R.id.iv_Sat);
        onSat = sharedPrefs.getBoolean(getString(R.string.pref_sat),false);
        if(onSat){
            ivSat.setImageResource(R.drawable.ic_sat_on);
        }else{
            ivSat.setImageResource(R.drawable.ic_sat_off);
        }
        ivSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onSat){
                    ivSat.setImageResource(R.drawable.ic_sat_on);
                    onSat = true;
                }else{
                    ivSat.setImageResource(R.drawable.ic_sat_off);
                    onSat = false;
                }
            }
        });

        ivSun = (ImageView) rootView.findViewById(R.id.iv_SUN);
        onSun = sharedPrefs.getBoolean(getString(R.string.pref_sun),false);
        if(onSun){
            ivSun.setImageResource(R.drawable.ic_sun_on);
        }else{
            ivSun.setImageResource(R.drawable.ic_sun_off);
        }

        ivSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!onSun){
                    ivSun.setImageResource(R.drawable.ic_sun_on);
                    onSun = true;
                }else{
                    ivSun.setImageResource(R.drawable.ic_sun_off);
                    onSun = false;
                }
            }
        });

        timePicker = (TimePicker) rootView.findViewById(R.id.timePicker1);
        int currentHour = sharedPrefs.getInt(getString(R.string.pref_hour), 0);
        int currentMin = sharedPrefs.getInt(getString(R.string.pref_min), 0);
        timePicker.setCurrentHour(currentHour);
        timePicker.setCurrentMinute(currentMin);
        return rootView;
    }

    private View.OnClickListener btnSavePlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editor.putBoolean(getString(R.string.pref_monday), onMon);
            editor.putBoolean(getString(R.string.pref_tuesday), onTue);
            editor.putBoolean(getString(R.string.pref_wednesday), onWed);
            editor.putBoolean(getString(R.string.pref_thursday), onThurs);
            editor.putBoolean(getString(R.string.pref_friday), onFri);
            editor.putBoolean(getString(R.string.pref_sat), onSat);
            editor.putBoolean(getString(R.string.pref_sun), onSun);
            editor.putInt(getString(R.string.pref_hour), timePicker.getCurrentHour());
            editor.putInt(getString(R.string.pref_min), timePicker.getCurrentMinute());
            editor.commit();

            Intent intent = new Intent(getActivity(), NotifyReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.cancel(pendingIntent);

            Calendar calendar = Calendar.getInstance();

            if(onMon || onTue || onWed || onThurs || onFri || onSat || onSun){
                boolean am = true;
                if(timePicker.getCurrentHour()/12>0)
                    am = false;
                int hour = timePicker.getCurrentHour()%12;
                if(!am && hour==0) hour = 12;
                calendar.set(Calendar.HOUR,hour);
                calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                if(am)
                    calendar.set(Calendar.AM_PM, Calendar.AM);
                else
                    calendar.set(Calendar.AM_PM, Calendar.PM);
            }

            if(onMon){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
                        PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }
            if(onTue){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
                        PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }
            if(onWed){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
                        PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }
            if(onThurs){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
                        PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }
            if(onFri){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
                        PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));}
            if(onSat){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
                        PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }
            if(onSun){
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000,
                        PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }

            Toast.makeText(context,"Workout Plan Successfully Saved", Toast.LENGTH_LONG).show();
        }
    };
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}
