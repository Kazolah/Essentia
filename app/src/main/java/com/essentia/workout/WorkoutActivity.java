package com.essentia.workout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ViewSwitcher;

import com.essentia.tracker.Tracker;
import com.essentia.workout.workout_pojos.TickListener;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WorkoutActivity extends ActionBarActivity implements TickListener {

    private Tracker mTracker = null;
    private Workout workout;
    private Location l = null;
    private Timer timer;

    //Variables
    private boolean isStarted = false;

    SectionsPagerAdapter mSectionsPagerAdapter;
    private CharSequence mTitle;

    private final Handler handler = new Handler();

    //UI component References
    private Button btnStart;
    private Button btnPause;
    private Button btnEnd;
    private ViewSwitcher switcher;
    private ViewPager mViewPager;
    private WorkoutMetricsFragment metricsFragment;
    private WorkoutHRFragment hrFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mTitle = getTitle();

        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        metricsFragment = (WorkoutMetricsFragment) mSectionsPagerAdapter.getItem(0);
        hrFragment = (WorkoutHRFragment) mSectionsPagerAdapter.getItem(1);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        switcher = (ViewSwitcher) findViewById(R.id.workout_btns_view_switcher);

        btnStart = (Button) findViewById(R.id.btnSessionStart);
        btnStart.setOnClickListener(btnStartClickListener);

        btnPause = (Button) findViewById(R.id.btnSessionPause);
        btnPause.setOnClickListener(btnPauseClickListener);

        btnEnd = (Button) findViewById(R.id.btnSessionEnd);
        btnEnd.setOnClickListener(btnEndClickListener);

        bindGpsTracker();

    }

    private void finishSession( Class<?> cls){
        startActivity(new Intent(this, cls));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workout, menu);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unbindGpsTracker();
//        stopTimer();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void startTimer(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                WorkoutActivity.this.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        WorkoutActivity.this.onTick();
                    }
                });
            }
        },0,500);
    }
    void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    @Override
    public void onTick() {
        if (workout != null) {
            workout.onTick();
            updateView();

            if (mTracker != null) {
                Location l2 = mTracker.getLastKnownLocation();
                if (!l2.equals(l)) {
                    l = l2;
                }
            }
        }
    }

    private void updateView(){
        setPauseButtonEnabled(!workout.isPaused());
        metricsFragment.updateView(workout);
        hrFragment.updateView(workout);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
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
                    fragment = WorkoutMetricsFragment.newInstance();
                    break;
                case 1:
                    fragment = WorkoutHRFragment.newInstance();
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

    private boolean mIsBound = false;

    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service. Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            if (mTracker == null) {
                mTracker = ((Tracker.LocalBinder) service).getService();
                // Tell the user about this for our demo.
                WorkoutActivity.this.onGpsTrackerBound();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mIsBound = false;
            mTracker = null;
        }
    };


    private void bindGpsTracker(){
        getApplicationContext().bindService(new Intent(this, Tracker.class),
                mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void onGpsTrackerBound() {
        workout = mTracker.getWorkout();
        startTimer();
    }

    void unbindGpsTracker() {
        if (mIsBound) {
            // Detach our existing connection.
            getApplicationContext().unbindService(mConnection);
            mIsBound = false;
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
        }
    };

    public final View.OnClickListener btnPauseClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };


    public final View.OnClickListener btnEndClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };
}
