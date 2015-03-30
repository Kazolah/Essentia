package com.essentia.main;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.essentia.dbHelpers.UserDBHelper;
import com.essentia.history.HistoryFragment;
import com.essentia.left_drawer.NavigationDrawerFragment;
import com.essentia.left_drawer.NavigationListItems;
import com.essentia.metrics.Metrics;
import com.essentia.notification.GpsBoundState;
import com.essentia.notification.GpsSearchingState;
import com.essentia.notification.NotificationManagerDisplayStrategy;
import com.essentia.notification.NotificationStateManager;
import com.essentia.plans.PlansActivity;
import com.essentia.setting.ProfileSettingActivity;
import com.essentia.setting.SettingActivity;
import com.essentia.statistics.StatisticsActivity;
import com.essentia.summary.WorkoutSummaryActivity;
import com.essentia.support.ApplicationContext;
import com.essentia.tracker.GpsInformation;
import com.essentia.tracker.GpsStatus;
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
    private GpsStatus mGpsStatus;
    private GpsSearchingState gpsSearchingState;
    private GpsBoundState gpsBoundState;
    private NotificationStateManager notificationStateManager;
    boolean skipStopGps = false;
//    private GpsSearchingState pgsSearchingState;

    boolean doubleBackToExitPressedOnce = false;
    //Reference for activity setup list
    private static final int ACTIVITY = 0;
    private static final int TYPE = 2;
    private static final int METRICS = 1;

    private MainFragment mainFragment;
    private UserDBHelper userDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainFragment = new MainFragment();
        mGpsStatus = new GpsStatus(this);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationStateManager = new NotificationStateManager(new NotificationManagerDisplayStrategy(notificationManager));
        gpsSearchingState = new GpsSearchingState(this, this);
        gpsBoundState = new GpsBoundState(this);

        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(ApplicationContext.userObject==null){
            userDBHelper = new UserDBHelper(this);
            ApplicationContext.userObject = userDBHelper.getUserObject();
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.main_drawer_layout));
    }
    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onResume(){
        super.onResume();
        onGpsTrackerBound();
    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onStop(){
        super.onStop();
    }
    @Override
    public void onDestroy(){
        stopGps();
        notificationStateManager.cancelNotification();
        mGpsStatus = null;
        super.onDestroy();
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            case NavigationDrawerFragment.USER_PROFILE:
                startActivity(new Intent(this, ProfileSettingActivity.class));
                break;
            case NavigationDrawerFragment.MAIN:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, mainFragment)
                        .commit();
                    break;
            case NavigationDrawerFragment.HISTORY:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new HistoryFragment())
                        .addToBackStack("history")
                        .commit();
                break;
            case NavigationDrawerFragment.STATISTICS:
                finish();
                startActivity(new Intent(this, StatisticsActivity.class));
                break;
            case NavigationDrawerFragment.PLANS:
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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
        bundles.putSerializable("Metrics",mainFragment.getMetricsList());
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

    void onGpsTrackerBound() {
        startGps();
        updateView();
    }
    void updateView(){
        if (mainFragment!=null){
            mainFragment.updateView(mGpsStatus);
        }
    }

    private void startGps(){
        if (mGpsStatus != null && !mGpsStatus.isLogging())
            mGpsStatus.start(this);
    }

    private void stopGps(){
        System.err.println("StartActivity.stopGps() skipStop: " + this.skipStopGps);
        if (skipStopGps == true)
            return;
        mGpsStatus.stop(this);
    }

    public View.OnClickListener getBtnStartClickListener(){
        return btnStartClickListener;
    }
    final View.OnClickListener btnStartClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if(mGpsStatus.isEnabled() == false){
               startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
           }else{
               startGps();
               skipStopGps = true;
               mGpsStatus.stop(MainActivity.this);
               Intent i = new Intent(MainActivity.this, WorkoutActivity.class);
               Bundle bundles = new Bundle();
               bundles.putSerializable("Metrics", mainFragment.getMetricsList());
               bundles.putSerializable("Activity",mainFragment.getSelectedActivity().title);
               bundles.putSerializable("Type",mainFragment.getSelectedType().title);
               i.putExtras(bundles);
               startActivity(i);
               MainActivity.this.startActivityForResult(i,112);
               notificationStateManager.cancelNotification();
               return;
           }
            updateView();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 112) {
            skipStopGps = false;
            onGpsTrackerBound();
        } else {
            updateView();
        }
    }
    public void displayGPSBoundState(){
        notificationStateManager.displayNotificationState(gpsBoundState);
    }
    public void displayGPSSearchingState(){
        notificationStateManager.displayNotificationState(gpsSearchingState);
    }

    /**
     * Load Activity for selected item from History
     * @param id
     */
    public void loadActivity(long id){
        Intent i = new Intent(this, WorkoutSummaryActivity.class);
        i.putExtra("ID",id);
        startActivity(i);
    }
}
