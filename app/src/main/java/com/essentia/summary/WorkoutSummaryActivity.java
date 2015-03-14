package com.essentia.summary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.essentia.dbHelpers.ActivityDBHelper;
import com.essentia.dbHelpers.ActivityHRDetailDBHelper;
import com.essentia.dbHelpers.ActivityHRDetailDBHelper.HRDetailQuery;
import com.essentia.dbHelpers.DBBuilder;
import com.essentia.dbHelpers.LocationDBHelper;
import com.essentia.support.WorkoutActivity;
import com.essentia.util.Route;
import com.example.kyawzinlatt94.essentia.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kyawzinlatt94 on 3/6/15.
 */
public class WorkoutSummaryActivity extends Activity{
    private LocationDBHelper locationDBHelper;
    private AsyncTask<String, String, Route> loadRouteTask;
    private Context context;
    private long mID;

    private ActivityHRDetailDBHelper hrDetailDBHelper;
    private ActivityDBHelper activityDBHelper;
    private WorkoutActivity workoutActivity;
    private ArrayList<HRDetailQuery> hrQueriesList;

    private WorkoutSummaryDataFragment dataFragment;
    private WorkoutSummaryHRFragment hrFragment;
    private WorkoutSummaryMapFragment mapFragment;

    private String DB_PATH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);

        DBBuilder dbBuilder = new DBBuilder(this);
        dbBuilder.buildDBs();

        activityDBHelper = new ActivityDBHelper(this);
        locationDBHelper = new LocationDBHelper(this);
        hrDetailDBHelper = new ActivityHRDetailDBHelper(this);
        context = getApplicationContext();
        Intent intent = getIntent();
        mID = intent.getLongExtra("ID",1);

        //query and load the info for the activity
        workoutActivity = activityDBHelper.queryActivity(String.valueOf(mID));

        //Get the hrDetail data with activity id
        hrQueriesList = hrDetailDBHelper.queryHRDetail(String.valueOf(mID));

        dataFragment = new WorkoutSummaryDataFragment();
        hrFragment = new WorkoutSummaryHRFragment();
        mapFragment = new WorkoutSummaryMapFragment();

        //To Check the database, Later to be removed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DB_PATH = getApplicationContext().getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
        }
        else {
            DB_PATH = getApplicationContext().getFilesDir().getPath() + getApplicationContext().getPackageName() + "/databases/";
        }
        try {
            writeToSD();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void loadRoute(){
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

    private void writeToSD() throws IOException {
        File sd = Environment.getExternalStorageDirectory();

        if (sd.canWrite()) {
            String currentDBPath = "EssentiaSQLite";
            String backupDBPath = "backupname.db";
            File currentDB = new File(DB_PATH, currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        }
    }
}

