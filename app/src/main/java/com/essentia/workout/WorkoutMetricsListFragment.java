package com.essentia.workout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.essentia.left_drawer.NavigationListItems;
import com.essentia.main.ListCustomAdapter;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 3/1/15.
 */
public class WorkoutMetricsListFragment extends Fragment {
    private ListCustomAdapter adapter;
    private ListView mListView;
    private WorkoutActivity workoutActivity;
    private NavigationListItems mListItems[];
    private static String selectedMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationListItems listItems[] = new NavigationListItems[]{
                new NavigationListItems(R.drawable.ic_calorie, getString(R.string.calorie)),
                new NavigationListItems(R.drawable.ic_distance, getString(R.string.distance)),
                new NavigationListItems(R.drawable.ic_pace, getString(R.string.pace)),
                new NavigationListItems(R.drawable.ic_speed, getString(R.string.speed)),
                new NavigationListItems(R.drawable.ic_duration, getString(R.string.duration)),
                new NavigationListItems(R.drawable.ic_heart_rate, getString(R.string.heart_rate)),
        };
        mListItems = listItems;
        adapter = new ListCustomAdapter(getActivity(), R.layout.custom_drawer_row_layout, listItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activity_setup, container, false);
        mListView = (ListView)rootView.findViewById(R.id.lv_activity);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mListView.setAdapter(adapter);
        return rootView;
    }

    /**
     * Navigate back to the main fragment and pass the selected value
     */
    private void selectItem(int position){
        selectedMetrics = mListItems[position].title;

        workoutActivity.inflateWorkoutFragment(selectedMetrics);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        workoutActivity = (WorkoutActivity) activity;
    }

}
