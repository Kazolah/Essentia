package com.essentia.workout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essentia.util.Formatter;
import com.essentia.util.HRZones;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/7/15.
 */
@TargetApi(16)
public class WorkoutHRInfoFragment extends Fragment{
    private int currentHRValue;
    private int currentHRPercentage;
    private int currentHRZone;
    private WorkoutActivity workoutActivity;
    private int previousHRZone = -1;
    private TextView tvUpperHrLimit;
    private TextView tvLowerHrLimit;
    private TextView tvCurrentHrPercent;
    private TextView tvCurrentHrZone;
    private TextView tvUpperHrZone;
    private TextView tvLowerHrZone;
    private RelativeLayout hrZoneLayout;
    private Formatter formatter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formatter = new Formatter(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_hr_info, container, false);
        tvUpperHrLimit = (TextView)rootView.findViewById(R.id.fwhi_upper_limit);
        tvLowerHrLimit = (TextView)rootView.findViewById(R.id.fwhi_lower_limit);
        tvCurrentHrPercent = (TextView)rootView.findViewById(R.id.fwhi_current_percent);
        tvCurrentHrZone = (TextView)rootView.findViewById(R.id.fwhi_current_zone);
        tvUpperHrZone = (TextView)rootView.findViewById(R.id.fwhi_upper_zone);
        tvLowerHrZone = (TextView)rootView.findViewById(R.id.fwhi_lower_zone);
        hrZoneLayout = (RelativeLayout) rootView.findViewById(R.id.fwhi_rl_hr_zone);
        return rootView;

    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WorkoutHRInfoFragment newInstance() {
        WorkoutHRInfoFragment fragment = new WorkoutHRInfoFragment();
        return fragment;
    }

    public void updateView(Workout workout, int hrValue){
       HRZones hrZones = workout.getHRZones();
       this.currentHRValue = hrValue;
       this.currentHRPercentage = hrZones.getCurrentHRPercentage(currentHRValue);
       currentHRZone = hrZones.getCurrentHRZone(currentHRValue);
       if(currentHRZone != previousHRZone){
           //Trigger Speech

       }
//       workoutActivity.populateZoneDurationData(currentHRZone);
       previousHRZone = currentHRZone;

       Pair<Integer, Integer> hrValueBound = hrZones.getCurrentHRValueBound(currentHRValue, currentHRZone);
       Pair<Integer, Integer> hrPercentBound = hrZones.getCurrentHRPercentBound(currentHRZone);

       tvCurrentHrPercent.setText(currentHRPercentage+" %");
       tvLowerHrLimit.setText(hrValueBound.first+" ");
       tvUpperHrLimit.setText(hrValueBound.second+" ");
       tvCurrentHrZone.setText("Zone "+currentHRZone);

        switch(currentHRZone){
            case 0:
                hrZoneLayout.setBackground(getResources().getDrawable(R.drawable.bg_zone1));
                tvLowerHrZone.setVisibility(View.GONE);
                tvUpperHrZone.setVisibility(View.VISIBLE);
                break;
            case 1:
                hrZoneLayout.setBackground(getResources().getDrawable(R.drawable.bg_zone1));
                tvLowerHrZone.setVisibility(View.GONE);
                tvUpperHrZone.setVisibility(View.VISIBLE);
                break;
            case 2:
                hrZoneLayout.setBackground(getResources().getDrawable(R.drawable.bg_zone2));
                tvLowerHrZone.setVisibility(View.VISIBLE);
                tvUpperHrZone.setVisibility(View.VISIBLE);
                break;
            case 3:
                hrZoneLayout.setBackground(getResources().getDrawable(R.drawable.bg_zone3));
                tvLowerHrZone.setVisibility(View.VISIBLE);
                tvUpperHrZone.setVisibility(View.VISIBLE);
                break;
            case 4:
                hrZoneLayout.setBackground(getResources().getDrawable(R.drawable.bg_zone4));
                tvLowerHrZone.setVisibility(View.VISIBLE);
                tvUpperHrZone.setVisibility(View.VISIBLE);
                break;
            case 5:
                hrZoneLayout.setBackground(getResources().getDrawable(R.drawable.bg_zone5));
                tvLowerHrZone.setVisibility(View.VISIBLE);
                tvUpperHrZone.setVisibility(View.GONE);
                break;
        }
        tvLowerHrZone.setText(formatter.formatLowerLimitHRZones(hrPercentBound.first, currentHRZone));
        tvUpperHrZone.setText(formatter.formatUpperLimitHRZones(hrPercentBound.second, currentHRZone));
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        workoutActivity = (WorkoutActivity) activity;
    }
}

