package com.essentia.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.essentia.left_drawer.NavigationDrawerFragment;
import com.essentia.main.MainActivity;
import com.essentia.plans.PlansActivity;
import com.essentia.setting.ProfileSettingActivity;
import com.essentia.statistics.StatisticsActivity;
import com.essentia.summary.WorkoutSummaryActivity;
import com.example.kyawzinlatt94.essentia.R;

/**
 * This class is Activity for history, see HistoryFragment for details
 */
public class HistoryActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private HistoryFragment historyFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyFragment = new HistoryFragment();
        setContentView(R.layout.activity_history);

        //Replace history fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.history_container, historyFragment)
                .commit();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.history_navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setCurrentSelectedPosition(NavigationDrawerFragment.HISTORY);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.history_navigation_drawer,
                (DrawerLayout) findViewById(R.id.history_drawer_layout));
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
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
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            case NavigationDrawerFragment.USER_PROFILE:
                finish();
                startActivity(new Intent(this, ProfileSettingActivity.class));
                break;
            case NavigationDrawerFragment.MAIN:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case NavigationDrawerFragment.HISTORY:
                //Replace history fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.history_container, historyFragment)
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadActivity(long id){
        Intent i = new Intent(this, WorkoutSummaryActivity.class);
        i.putExtra("ID",id);
        startActivity(i);
    }
}
