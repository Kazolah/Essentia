package com.essentia.workout;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewSwitcher;

import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

import java.util.Locale;

/**
 * Created by kyawzinlatt94 on 3/1/15.
 */
public class WorkoutFragment extends Fragment{
    private Workout workout;
    private Location l = null;

    private WorkoutActivity workoutActivity;
    private boolean isStarted = false;
    SectionsPagerAdapter mSectionsPagerAdapter;

    //UI component References
    private Button btnStart;
    private Button btnPause;
    private Button btnEnd;
    private ViewSwitcher switcher;
    private ViewPager mViewPager;
    public WorkoutMetricsFragment metricsFragment = WorkoutMetricsFragment.newInstance();
    private WorkoutHRFragment hrFragment = WorkoutHRFragment.newInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        switcher = (ViewSwitcher) rootView.findViewById(R.id.workout_btns_view_switcher);
        btnStart = (Button) rootView.findViewById(R.id.btnSessionStart);
        btnStart.setOnClickListener(btnStartClickListener);

        btnPause = (Button) rootView.findViewById(R.id.btnSessionPause);
        btnPause.setOnClickListener(btnPauseClickListener);

        btnEnd = (Button) rootView.findViewById(R.id.btnSessionEnd);
        btnEnd.setOnClickListener(btnEndClickListener);
        if(isStarted){
            switcher.showNext();
        }
        return rootView;
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = new Fragment();
            switch(position){
                case 0:
                    fragment = metricsFragment;
                    break;
                case 1:
                    fragment = hrFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_workout).toUpperCase(l);
                case 1:
                    return getString(R.string.title_fragment_hr).toUpperCase(l);
            }
            return null;
        }
    }

    public void updateView(){
        setPauseButtonEnabled(!workout.isPaused());
        switch(mViewPager.getCurrentItem()){
            case 0:
                metricsFragment.updateView(workout);
                break;
            case 1:
                if(hrFragment.isViewCreated())
                    hrFragment.updateView(workout);
                break;
        }
    }

    private void setPauseButtonEnabled(boolean enabled){
        if(enabled){
            btnPause.setText("PAUSE");
            btnPause.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
        }else {
            btnPause.setText("RESUME");
            btnPause.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
        }
    }

    // Button Listeners
    public final View.OnClickListener btnStartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switcher.showNext();
            workoutActivity.bindGpsTracker();
            isStarted = true;
        }
    };

    public final View.OnClickListener btnPauseClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(workout.isPaused()){
                workout.onResume(workout);
            }else{
                workout.onPause(workout);
            }
            setPauseButtonEnabled(!workout.isPaused());
        }
    };


    public final View.OnClickListener btnEndClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            workoutActivity.endActivity();
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        workoutActivity = (WorkoutActivity) activity;
    }

    public WorkoutMetricsFragment getMetricsFragment(){
        return metricsFragment;
    }
    public void setWorkout(Workout workout){
        this.workout = workout;
    }

}
