package com.essentia.summary;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essentia.support.WorkoutActivity;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 3/6/15.
 */
public class WorkoutSummaryDataFragment extends Fragment{
    private TextView tvDuration;
    private TextView tvCalorie;
    private TextView tvAvgPace;
    private TextView tvAvgSpeed;
    private TextView tvAvgHR;
    private TextView tvMaxHR;
    private WorkoutSummaryActivity summaryActivity;
    private WorkoutActivity workoutActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workoutActivity = summaryActivity.getWorkoutActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_summary_data, container, false);
        tvDuration = (TextView) rootView.findViewById(R.id.fwsd_tvDuration);
        tvCalorie = (TextView) rootView.findViewById(R.id.fwsd_tvDuration);
        tvAvgPace = (TextView) rootView.findViewById(R.id.fwsd_tvDuration);
        tvAvgSpeed = (TextView) rootView.findViewById(R.id.fwsd_tvAvgSpeed);
        tvAvgHR = (TextView) rootView.findViewById(R.id.fwsd_tvAvgHR);
        tvMaxHR = (TextView) rootView.findViewById(R.id.fwsd_tvMaxHR);

        tvDuration.setText(workoutActivity.getDuration());
        tvCalorie.setText(workoutActivity.getCalorie());
        tvAvgPace.setText(workoutActivity.getAvgPace());
        tvAvgSpeed.setText(workoutActivity.getAvgSpeed());
        tvAvgHR.setText(workoutActivity.getAvgHR());
        tvMaxHR.setText(workoutActivity.getMaxHR());
        return rootView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        summaryActivity = (WorkoutSummaryActivity) activity;
    }
}


