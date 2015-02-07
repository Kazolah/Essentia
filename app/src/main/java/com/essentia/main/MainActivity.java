package com.essentia.main;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.essentia.account.UserProfileActivity;
import com.essentia.history.HistoryActivity;
import com.essentia.left_drawer.NavigationDrawerFragment;
import com.essentia.plans.PlansActivity;
import com.essentia.statistics.StatisticsActivity;
import com.essentia.workout.WorkoutActivity;
import com.example.kyawzinlatt94.essentia.R;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainFragment.ActivitySetUp{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private Context context = this;

    //Reference for drawer list
    private static final int USER_PROFILE = 0;
    private static final int MAIN = 1;
    private static final int HISTORY = 2;
    private static final int STATISTICS = 3;
    private static final int PLANS = 4;

    //Reference for activity setup list
    private static final int ACTIVITY = 0;
    private static final int TYPE = 1;
    private static final int METRICS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                fragmentManager.beginTransaction()
                        .addToBackStack("Main")
                        .replace(R.id.container, new MainFragment())
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
        switch(position){
            case ACTIVITY:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new ActivityFragment())
                        .addToBackStack(getString(R.string.list_prompt_activity))
                        .commit();
                break;
            case TYPE:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new TypeFragment())
                        .addToBackStack(getString(R.string.list_prompt_type))
                        .commit();
                break;
            case METRICS:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new MetricsFragment())
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
            getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void startWorkoutSession(View v){
        Intent i = new Intent(this, WorkoutActivity.class);
        startActivity(i);
    }
}
