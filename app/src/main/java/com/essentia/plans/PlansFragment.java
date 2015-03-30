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

import com.example.kyawzinlatt94.essentia.R;

import java.util.Calendar;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class PlansFragment extends Fragment{
    private PlansActivity plansActivity;
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
        context = plansActivity.getApplicationContext();
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
        if(sharedPrefs.getBoolean(getString(R.string.pref_monday),false)){
            ivMon.setImageResource(R.drawable.ic_monday_on);
            onMon = true;
        }else{
            ivMon.setImageResource(R.drawable.ic_monday_off);
            onMon = false;
        }
        ivMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivMon.getDrawable() == getResources().getDrawable(R.drawable.ic_monday_off)){
                    ivMon.setImageResource(R.drawable.ic_monday_on);
                    onMon = true;
                }else{
                    ivMon.setImageResource(R.drawable.ic_monday_off);
                    onMon = false;
                }
            }
        });
        ivTue = (ImageView) rootView.findViewById(R.id.iv_TUE);
        if(sharedPrefs.getBoolean(getString(R.string.pref_tuesday),false)){
            ivTue.setImageResource(R.drawable.ic_tuesday_on);
            onTue = true;
        }else{
            ivTue.setImageResource(R.drawable.ic_tuesday_off);
            onTue = false;
        }
        ivTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivTue.getDrawable() == getResources().getDrawable(R.drawable.ic_tuesday_off)){
                    ivTue.setImageResource(R.drawable.ic_tuesday_on);
                    onTue = true;
                }else{
                    ivTue.setImageResource(R.drawable.ic_tuesday_off);
                    onTue = false;
                }
            }
        });

        ivWed = (ImageView) rootView.findViewById(R.id.iv_WED);
        if(sharedPrefs.getBoolean(getString(R.string.pref_wednesday),false)){
            ivWed.setImageResource(R.drawable.ic_wednesday_on);
            onWed = true;
        }else{
            ivWed.setImageResource(R.drawable.ic_wednesday_off);
            onWed = false;
        }
        ivWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivWed.getDrawable() == getResources().getDrawable(R.drawable.ic_wednesday_off)){
                    ivWed.setImageResource(R.drawable.ic_wednesday_on);
                    onWed = true;
                }else{
                    ivWed.setImageResource(R.drawable.ic_wednesday_off);
                    onWed = false;
                }
            }
        });

        ivThurs = (ImageView) rootView.findViewById(R.id.iv_THURS);
        if(sharedPrefs.getBoolean(getString(R.string.pref_thursday),false)){
            ivThurs.setImageResource(R.drawable.ic_thrusday_on);
            onThurs = true;
        }else{
            ivThurs.setImageResource(R.drawable.ic_thrusday_off);
            onThurs = false;
        }
        ivThurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivThurs.getDrawable() == getResources().getDrawable(R.drawable.ic_thrusday_off)){
                    ivThurs.setImageResource(R.drawable.ic_thrusday_on);
                    onThurs = true;
                }else{
                    ivThurs.setImageResource(R.drawable.ic_thrusday_off);
                    onThurs = false;
                }
            }
        });

        ivFri = (ImageView) rootView.findViewById(R.id.iv_Fri);
        if(sharedPrefs.getBoolean(getString(R.string.pref_friday),false)){
            ivFri.setImageResource(R.drawable.ic_friday_on);
            onFri = true;
        }else{
            ivFri.setImageResource(R.drawable.ic_friday_off);
            onFri = false;
        }
        ivFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivFri.getDrawable() == getResources().getDrawable(R.drawable.ic_friday_off)){
                    ivFri.setImageResource(R.drawable.ic_friday_on);
                    onFri = true;
                }else{
                    ivFri.setImageResource(R.drawable.ic_friday_off);
                    onFri = false;
                }
            }
        });

        ivSat = (ImageView) rootView.findViewById(R.id.iv_Sat);
        if(sharedPrefs.getBoolean(getString(R.string.pref_sat),false)){
            ivSat.setImageResource(R.drawable.ic_sat_on);
            onSat = true;
        }else{
            ivSat.setImageResource(R.drawable.ic_sat_off);
            onSat = false;
        }
        ivSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivSat.getDrawable() == getResources().getDrawable(R.drawable.ic_sat_off)){
                    ivSat.setImageResource(R.drawable.ic_sat_on);
                    onSat = true;
                }else{
                    ivSat.setImageResource(R.drawable.ic_sat_off);
                    onSat = false;
                }
            }
        });

        ivSun = (ImageView) rootView.findViewById(R.id.iv_SUN);
        if(sharedPrefs.getBoolean(getString(R.string.pref_sun),false)){
            ivSun.setImageResource(R.drawable.ic_sun_on);
            onSun = true;
        }else{
            ivSun.setImageResource(R.drawable.ic_sun_off);
            onSun = false;
        }

        ivSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivSun.getDrawable() == getResources().getDrawable(R.drawable.ic_sun_off)){
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

            Calendar calendar = Calendar.getInstance();
            if(onMon || onTue || onWed || onThurs || onFri || onSat || onSun){
                calendar.set(Calendar.HOUR,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
            }

            if(onMon){calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);}
            if(onTue){calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);}
            if(onWed){calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);}
            if(onThurs){calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);}
            if(onFri){calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);}
            if(onSat){calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);}
            if(onSun){calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);}

            alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), NotifyService.class);
            pendingIntent = PendingIntent.getService(context, 0, intent, 0);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24 , pendingIntent);

            Toast.makeText(context,"Workout Plan Successfully Saved", Toast.LENGTH_LONG);
        }
    };
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        plansActivity = (PlansActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        plansActivity = null;
    }
}
