package com.essentia.summary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.essentia.dbHelpers.ActivityDBHelper;
import com.essentia.dbHelpers.ActivityHRDetailDBHelper;
import com.essentia.dbHelpers.ActivityHRDetailDBHelper.HRDetailQuery;
import com.essentia.dbHelpers.LocationDBHelper;
import com.essentia.dbHelpers.UserDBHelper;
import com.essentia.main.MainActivity;
import com.essentia.support.UserObject;
import com.essentia.support.WorkoutActivity;
import com.essentia.util.Route;
import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kyawzinlatt94 on 3/6/15.
 */
public class WorkoutSummaryActivity extends ActionBarActivity {
    private LocationDBHelper locationDBHelper;
    private AsyncTask<String, String, Route> loadRouteTask;
    private Context context;
    private long mID;

    private ActivityHRDetailDBHelper hrDetailDBHelper;
    private UserDBHelper userDBHelper;
    private ActivityDBHelper activityDBHelper;
    private WorkoutActivity workoutActivity;
    private ArrayList<HRDetailQuery> hrQueriesList;

    private WorkoutSummaryDataFragment dataFragment;
    private WorkoutSummaryHRFragment hrFragment;
    private WorkoutSummaryMapFragment mapFragment;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Button btnSaveActivity;
    private Button btnDiscardActivity;
    private boolean isDestoryed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#707070")));
        context = getApplicationContext();
        isDestoryed = false;
        dataFragment = new WorkoutSummaryDataFragment();
        hrFragment = new WorkoutSummaryHRFragment();
        mapFragment = new WorkoutSummaryMapFragment();
        btnSaveActivity = (Button)findViewById(R.id.btnSummarySave);
        btnSaveActivity.setOnClickListener(btnSaveClickListener);

        btnDiscardActivity = (Button)findViewById(R.id.btnSummaryDiscard);
        btnDiscardActivity.setOnClickListener(btnDiscardClickListener);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager_workout_summary);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==1){
                    hrFragment.displayTutorial();
                }
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {}

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {}
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(tabListener));
        }
        userDBHelper = new UserDBHelper(this);
        activityDBHelper = new ActivityDBHelper(this);
        locationDBHelper = new LocationDBHelper(this);
        hrDetailDBHelper = new ActivityHRDetailDBHelper(this);

        Intent intent = getIntent();
        mID = intent.getLongExtra("ID",1);

        workoutActivity = activityDBHelper.queryActivity(String.valueOf(mID)); //Query and load the info for the activity
        hrQueriesList = hrDetailDBHelper.queryHRDetail(String.valueOf(mID));   //Get the hrDetail data with activity id
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

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
                    fragment = dataFragment;
                    break;
                case 1:
                    fragment = hrFragment;
                    break;
                case 2:
                    fragment = mapFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_info).toUpperCase(l);
                case 1:
                    return getString(R.string.title_fragment_summary_hr).toUpperCase(l);
                case 2:
                    return getString(R.string.title_fragment_map).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * Load Route from database to populate in MapFragment
     */
    public void loadRoute(){
        try {
            loadRouteTask = new AsyncTask<String, String, Route>() {
                @Override
                protected Route doInBackground(String... params) {
                    Route route = locationDBHelper.loadRoute(context, mID);
                    return route;
                }

                @Override
                protected void onPostExecute(Route route) {
                    mapFragment.setRoute(route);
                }
            }.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<HRDetailQuery> getHRQueriesList(){
        return hrQueriesList;
    }
    public String getActivityId(){
        return String.valueOf(mID);
    }
    public WorkoutActivity getWorkoutActivity(){
        return workoutActivity;
    }

    public final View.OnClickListener btnSaveClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            updateAvgHR();
            Toast.makeText(context,"Activity Successfully Saved",Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, MainActivity.class);
            finish();
            startActivity(i);
        }
    };
    private void updateAvgHR(){
        UserObject userObject = userDBHelper.getUserObject();
        String userId = userObject.getUserId();
        userDBHelper.updateAvgHR(userId, activityDBHelper.getTotalAvgHR());
    }
    public final View.OnClickListener btnDiscardClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
           discardActivity();
        }
    };

    private void discardActivity(){
        boolean status = false;
        String where = LocationDBHelper.ACTIVITY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(mID)};
        String whereID = "id=?";
        if(locationDBHelper.delete(locationDBHelper, where, whereArgs) &&
                hrDetailDBHelper.delete(hrDetailDBHelper, where, whereArgs) &&
                activityDBHelper.delete(activityDBHelper, whereID, whereArgs)){
            updateAvgHR();
            status = true;
            isDestoryed = true;
        }
        if(status){
            Toast.makeText(context,"Activity Successfully Discarded",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"Problem deleting activity",Toast.LENGTH_LONG).show();
        }
        Intent i = new Intent(context, MainActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}

