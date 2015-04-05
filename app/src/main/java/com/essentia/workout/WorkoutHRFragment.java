package com.essentia.workout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essentia.workout.workout_pojos.CustomViewPager;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

import java.util.Locale;

/**
 * Created by kyawzinlatt94 on 2/4/15.
 */
public class WorkoutHRFragment extends Fragment {

    private boolean isViewCreated = false;

    private WorkoutHRInfoFragment hrInfoFragment;
    private WorkoutHRPercentageFragment hrPercentageFragment;
    private WorkoutActivity workoutActivity;
    private TextView tvCurrentHR;
    private TextView tvAvgHR;
    private TextView tvMaxHR;
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    CustomViewPager mViewPager;
    Context context;
    private RelativeLayout tabInfo;
    private RelativeLayout tabChart;
    private boolean isInfo;
    private int currentHRValue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_hr, container, false);
        tvCurrentHR = (TextView) rootView.findViewById(R.id.fwh_current_hr);
        tvAvgHR = (TextView) rootView.findViewById(R.id.fwh_avg_hr);
        tvMaxHR = (TextView) rootView.findViewById(R.id.fwh_max_hr);
        context = getActivity();
        isInfo = true;
        tabInfo = (RelativeLayout) rootView.findViewById(R.id.fwh_tab_zones);
        tabInfo.setOnClickListener(tabInfoClickListener);
        tabChart = (RelativeLayout) rootView.findViewById(R.id.fwh_tab_chart);
        tabChart.setOnClickListener(tabChartClickListener);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) rootView.findViewById(R.id.pager_workout_hr);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);
        return rootView;
    }
    final View.OnClickListener tabInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabInfo.setBackgroundColor(getResources().getColor(R.color.holo_gray_dark));
            tabChart.setBackgroundColor(getResources().getColor(R.color.gray));
            mViewPager.setCurrentItem(0);
        }
    };
    final View.OnClickListener tabChartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tabChart.setBackgroundColor(getResources().getColor(R.color.holo_gray_dark));
            tabInfo.setBackgroundColor(getResources().getColor(R.color.gray));
            mViewPager.setCurrentItem(1);
        }
    };
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WorkoutHRFragment newInstance() {
        WorkoutHRFragment fragment = new WorkoutHRFragment();
        return fragment;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            isViewCreated = true;
        }else{
            setViewDismiss();
        }
    }
    public void updateView(Workout workout){
        currentHRValue = workout.getCurrentHRValueInt();

        //Return if hr value is -1, which indicates null
        if(currentHRValue==-1)
            return;

        String hrValue = (currentHRValue==-1)?"--":String.valueOf(currentHRValue);
        tvCurrentHR.setText(hrValue);
        tvMaxHR.setText(workout.getMetrics("HRMax"));
        tvAvgHR.setText(workout.getMetrics("HRAvg"));
        hrInfoFragment.updateView(workout, currentHRValue);
        hrPercentageFragment.updateView(workoutActivity.getHRZones());
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
                    hrInfoFragment = WorkoutHRInfoFragment.newInstance();
                     fragment = hrInfoFragment;
                    break;
                case 1:
                    hrPercentageFragment = WorkoutHRPercentageFragment.newInstance();
                    fragment = hrPercentageFragment;
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
                    return getString(R.string.title_fragment_hr_chart).toUpperCase(l);
                case 1:
                    return getString(R.string.title_fragment_hr_percentage).toUpperCase(l);
            }
            return null;
        }
    }
    public boolean isViewCreated(){
        return isViewCreated;
    }
    public void setViewDismiss(){
        isViewCreated = false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        workoutActivity = (WorkoutActivity) activity;
    }

}
