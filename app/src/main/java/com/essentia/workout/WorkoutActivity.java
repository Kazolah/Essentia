package com.essentia.workout;

import java.util.Locale;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.essentia.account.UserProfileActivity;
import com.essentia.history.HistoryActivity;
import com.essentia.left_drawer.NavigationDrawerFragment;
import com.essentia.main.MainActivity;
import com.essentia.main.MainFragment;
import com.essentia.plans.PlansActivity;
import com.essentia.statistics.StatisticsActivity;
import com.essentia.support.AppScope;
import com.example.kyawzinlatt94.essentia.R;

public class WorkoutActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    //Reference for drawer list
    private static final int USER_PROFILE = 0;
    private static final int MAIN = 1;
    private static final int HISTORY = 2;
    private static final int STATISTICS = 3;
    private static final int PLANS = 4;

    private boolean isClicked = false;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.workout_navigation_drawer);
        mTitle = getTitle();
        //Set up the drawer
        mNavigationDrawerFragment.setUp(
                R.id.workout_navigation_drawer,
                (DrawerLayout) findViewById(R.id.workout_drawer_layout));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            case USER_PROFILE:
                finishSession(UserProfileActivity.class);
                break;
            case MAIN:
                if(isClicked) {
                    finishSession(MainActivity.class);
                }else{
                    isClicked = true;
                }
                break;
            case HISTORY:
                finishSession(HistoryActivity.class);
                break;
            case STATISTICS:
                finishSession(StatisticsActivity.class);
                break;
            case PLANS:
               finishSession(PlansActivity.class);
                break;
        }
    }
    private void finishSession( Class<?> cls){
        if(!AppScope.isStarted){
            finish();
        }
        startActivity(new Intent(this, cls));
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
            getMenuInflater().inflate(R.menu.menu_workout, menu);
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
}
