package com.essentia.workout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.essentia.summary.WorkoutSummaryActivity;
import com.essentia.tracker.Tracker;
import com.essentia.util.HRZones;
import com.essentia.workout.workout_pojos.MetricsUIRef;
import com.essentia.workout.workout_pojos.TickListener;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

import java.util.Timer;
import java.util.TimerTask;

public class WorkoutActivity extends FragmentActivity implements TickListener {

    private Tracker mTracker = null;
    private Workout workout;
    private Location l = null;
    private Timer timer;
    private MetricsUIRef selectedMetrics;
    //Variables
    private boolean isStarted = false;
    private String sport;
    private CharSequence mTitle;

    private final Handler handler = new Handler();
    private HRZones hrZones;
    //UI component References
    private WorkoutFragment workoutFragment;
    private WorkoutMetricsListFragment workoutMetricsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Intent i = getIntent();
        Bundle bundles = i.getExtras();
        sport = (String) bundles.getSerializable("Sport");

        mTitle = getTitle();
        workoutFragment = new WorkoutFragment();
        workoutMetricsListFragment = new WorkoutMetricsListFragment();
        inflateWorkoutFragment("");

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
        unbindGpsTracker();
        stopTimer();
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
        },0,100);
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
            workoutFragment.updateView();

//            if (mTracker != null) {
//                Location l2 = mTracker.getLastKnownLocation();
//                if (!l2.equals(l)) {
//                    l = l2;
//                }
//            }
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


    public void bindGpsTracker(){
        boolean status = getApplicationContext().bindService(new Intent(this, Tracker.class),
                mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void onGpsTrackerBound() {
        workout = new Workout();
        workout.setSport(sport);

        /* Initiate the Tracker */
        mTracker.start(workout);
        mTracker.setup();
        workout = mTracker.getWorkout();
        workoutFragment.setWorkout(this.workout);
//        try {
//            writeToSD();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        mTracker.onStartTimer();
        startTimer();
    }

    void unbindGpsTracker() {
        if (mIsBound) {
            // Detach our existing connection.
            getApplicationContext().unbindService(mConnection);
            mIsBound = false;
        }
    }

    public void inflateWorkoutFragment(String newSelectedMetrics){
        if(!newSelectedMetrics.equals("")) {
            workoutFragment.setSelectedMetrics(newSelectedMetrics);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.workout_container, workoutFragment)
                .addToBackStack("workout_fragment")
                .commit();
//        Fragment fragment = fragmentManager.findFragmentByTag("list_fragment");
//        if(fragment != null)
//            fragmentManager.beginTransaction().remove(fragment).commit();
    }

    public void inflateMetricsListFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.workout_container, workoutMetricsListFragment)
                .addToBackStack("list_fragment")
                .commit();
    }

    public MetricsUIRef getSelectedMetrics(){
        return selectedMetrics;
    }

    public Workout getWorkout(){
        return this.workout;
    }

    public long getTotalDurationLong(){
        if(mTracker!=null)
            return mTracker.getDurationLong();
        else
            return 0;
    }

    public HRZones getHRZones(){
        hrZones = mTracker.getHRZones();
        return hrZones;
    }

    public void endActivity(){
        if(timer!=null){
            workout.onStop(workout);
            stopTimer();
            mTracker.stopForeground(true); //Remove Notification
            Intent intent = new Intent(WorkoutActivity.this, WorkoutSummaryActivity.class);
            intent.putExtra("ID",mTracker.getActivityId());
            finish();
            startActivity(intent);
        }
    }

}
