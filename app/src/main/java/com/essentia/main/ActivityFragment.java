package com.essentia.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.essentia.left_drawer.NavigationListItems;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class ActivityFragment extends Fragment{

    private ActivityFragmentCustomAdapter adapter;
    private ListView mListView;
    private String activity;
    private MainActivity mainActivity;
    private NavigationListItems mListItems[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationListItems listItems[] = new NavigationListItems[]{
                new NavigationListItems(R.drawable.cycling, getString(R.string.cycling)),
                new NavigationListItems(R.drawable.running, getString(R.string.running)),
                new NavigationListItems(R.drawable.running_treadmill, getString(R.string.running_treadmill)),
                new NavigationListItems(R.drawable.walking, getString(R.string.walking)),
                new NavigationListItems(R.drawable.walking_treadmill, getString(R.string.walking_treadmill)),
                new NavigationListItems(R.drawable.other, getString(R.string.other_activities)),
                new NavigationListItems(R.drawable.other_indoor, getString(R.string.other_activities_indoor)),
        };
        mListItems = listItems;
        adapter = new ActivityFragmentCustomAdapter(getActivity(), R.layout.custom_drawer_row_layout,listItems);
    }

    public static ActivityFragment newInstance() {
        ActivityFragment fragment = new ActivityFragment();
        return fragment;
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
        mainActivity.workoutCallback(mListItems[position]);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getString(R.string.title_activity));
            mainActivity = (MainActivity) activity;
    }
    private void setSelectedActivity(String activity){
        this.activity = activity;
    }
}
