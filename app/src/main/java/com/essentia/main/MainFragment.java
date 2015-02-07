package com.essentia.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.essentia.workout.WorkoutActivity;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class MainFragment extends Fragment{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ACTIVITY = "Activity";
    private static  final String TYPE = "Type";
    private static final String METRICS = "Metrics";

    private ActivitySetUp mainActivity;
    private ListView setupListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setupListView = (ListView)rootView.findViewById(R.id.workout_setup_listview);
        setupListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        setupListView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{ ACTIVITY, TYPE, METRICS }));
        return rootView;
    }
    private void selectItem(int position){
        if(mainActivity!=null){
            mainActivity.onActivitySetUpSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getString(R.string.title_workout));
        try {
        mainActivity = (ActivitySetUp) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ActivitySetUp.");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public static interface ActivitySetUp{
        void onActivitySetUpSelected(int position);
    }
}
