package com.essentia.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/4/15.
 */
public class WorkoutMetricsFragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_3metrics, container, false);
        return rootView;
    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WorkoutMetricsFragment newInstance() {
        WorkoutMetricsFragment fragment = new WorkoutMetricsFragment();
        return fragment;
    }
}
