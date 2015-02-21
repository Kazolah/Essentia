package com.essentia.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.essentia.account.UserProfileActivity;
import com.essentia.history.HistoryActivity;
import com.essentia.left_drawer.NavigationDrawerFragment;
import com.essentia.left_drawer.NavigationListItems;
import com.essentia.metrics.Metrics;
import com.essentia.plans.PlansActivity;
import com.essentia.setting.SettingActivity;
import com.essentia.statistics.StatisticsActivity;
import com.essentia.tracker.GpsInformation;
import com.essentia.tracker.GpsStatus;
import com.essentia.tracker.Tracker;
import com.essentia.workout.WorkoutActivity;
import com.essentia.workout.workout_pojos.TickListener;
import com.example.kyawzinlatt94.essentia.R;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainFragment.ActivitySetUp,
        TickListener, GpsInformation {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Context context = this;
    private GpsStatus mGpsStatus;
    private Tracker mTracker;
    boolean skipStopGps = false;
//    private GpsSearchingState pgsSearchingState;

    //Reference for drawer list
    private static final int USER_PROFILE = 0;
    private static final int MAIN = 1;
    private static final int HISTORY = 2;
    private static final int STATISTICS = 3;
    private static final int PLANS = 4;

    //Reference for activity setup list
    private static final int ACTIVITY = 0;
    private static final int TYPE = 2;
    private static final int METRICS = 1;

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.main_drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            case USER_PROFILE:
                finish();
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case MAIN:
                mainFragment = new MainFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mainFragment)
                        .commit();
                    break;
            case HISTORY:
                finish();
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case STATISTICS:
                finish();
                startActivity(new Intent(this, StatisticsActivity.class));
                break;
            case PLANS:
                finish();
                startActivity(new Intent(this, PlansActivity.class));
                break;
        }
    }

    @Override
    public void onActivitySetUpSelected(int position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundles = new Bundle();
        switch(position){
            case ACTIVITY:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ActivityFragment())
                        .addToBackStack("Activity")
                        .commit();
                break;
            case TYPE:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new TypeFragment())
                        .addToBackStack("Type")
                        .commit();
                break;
            case METRICS:
                MetricsFragment metricsFragment = new MetricsFragment();
                bundles.putSerializable("Metrics", mainFragment.getMetrics());
                metricsFragment.setArguments(bundles);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, metricsFragment)
                        .addToBackStack(getString(R.string.list_prompt_metrics))
                        .commit();
                break;
        }
    }
    public void onSectionAttached(String title) {
        mTitle = title;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Start Workout session
     */
    public void startWorkoutSession(View v){
        Intent i = new Intent(this, WorkoutActivity.class);
        Bundle bundles = new Bundle();
        bundles.putSerializable("Metrics",mainFragment.getMetrics());
        bundles.putSerializable("Activity",mainFragment.getSelectedActivity().title);
        bundles.putSerializable("Type",mainFragment.getSelectedType().title);
        i.putExtras(bundles);
        startActivity(i);
    }
    public void metricsCallback(HashMap<String, Metrics> metrics){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(metrics!=null) {
            mainFragment.setSelectedMetrics(metrics);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commit();
    }
    public void workoutCallback(NavigationListItems workout){
         FragmentManager fragmentManager = getSupportFragmentManager();
        if(workout!=null){
            mainFragment.setSelectedActivity(workout);
        }
         fragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commit();
    }

    public void typeCallBack(NavigationListItems type){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (type!=null){
            mainFragment.setSelectedType(type);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commit();
    }
    @Override
    public void onTick() {
        updateView();
    }

    @Override
    public String getGpsAccuracy() {
        if (mTracker != null) {
            Location l = mTracker.getLastKnownLocation();

            if (l != null && l.getAccuracy() > 0) {
                return String.format(", %s m", l.getAccuracy());
            }
        }

        return "";
    }

    @Override
    public int getSatellitesAvailable() {
        return mGpsStatus.getSatellitesAvailable();
    }

    @Override
    public int getSatellitesFixed() {
        return mGpsStatus.getSatellitesFixed();
    }

    void bindGpsTracker(){
        // Establish a connection with the service. We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        getApplicationContext().bindService(new Intent(this, Tracker.class),
                mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    private boolean mIsBound = false;
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service. Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mTracker = ((Tracker.LocalBinder) service).getService();
            // Tell the user about this for our demo.
            MainActivity.this.onGpsTrackerBound();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mTracker = null;
        }
    };

    void onGpsTrackerBound() {
        startGps();
        updateView();
    }
    private void startGps(){
        if (mGpsStatus != null && !mGpsStatus.isLogging())
            mGpsStatus.start(this);

        if (mTracker != null) {
            mTracker.connect();
        }
    }
    private void stopGps(){
        System.err.println("StartActivity.stopGps() skipStop: " + this.skipStopGps);
        if (skipStopGps == true)
            return;

        if (mGpsStatus != null)
            mGpsStatus.stop(this);

        if (mTracker != null)
            mTracker.reset();
    }
    private void updateView(){

    }

}
