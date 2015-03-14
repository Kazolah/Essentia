package com.essentia.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.essentia.left_drawer.NavigationListItems;
import com.essentia.metrics.Calorie;
import com.essentia.metrics.Distance;
import com.essentia.metrics.Duration;
import com.essentia.metrics.HeartRate;
import com.essentia.metrics.Metrics;
import com.essentia.metrics.Pace;
import com.essentia.metrics.Speed;
import com.essentia.tracker.GpsStatus;
import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class MainFragment extends Fragment{

    private static final String ARG_SECTION_NUMBER = "section_number";
    public HashMap<String, Metrics> metrics;
    private ActivitySetUp mainActivity;
    private ListView setupListView;
    private MainFragmentCustomAdapter adapter;
    private String[] metricsList;
    private NavigationListItems selectedActivity;
    private NavigationListItems selectedType;
    private Button btnStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        metricsList = new String[]{
            Calorie.NAME, Distance.NAME, Duration.NAME,
                HeartRate.NAME,Pace.NAME, Speed.NAME
        };
        selectedActivity = new NavigationListItems(R.drawable.running, getString(R.string.running));
        selectedType = new NavigationListItems(R.drawable.basic_workout, getString(R.string.basic_workout));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Get the icons
        ArrayList<Integer> selectedMetrics = getSelectedMetrics();

        //Prepare list items for the list view
        MainFragmentListItems listItems[] = new MainFragmentListItems[]{
                new MainFragmentListItems(
                        getString(R.string.list_prompt_activity), selectedActivity.title,
                        selectedActivity.icon, 0,0,0,0),
                new MainFragmentListItems(
                        getString(R.string.list_prompt_metrics), "", selectedMetrics.get(0), selectedMetrics.get(1),
                        selectedMetrics.get(2),selectedMetrics.get(3),selectedMetrics.get(4)),
                new MainFragmentListItems(
                        getString(R.string.list_prompt_type), selectedType.title, selectedType.icon,0,0,0,0),
        };
        adapter = new MainFragmentCustomAdapter(
                getActivity(), R.layout.custom_main_fragment_row_layout,listItems);

        setupListView = (ListView)rootView.findViewById(R.id.workout_setup_listview);
        setupListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        setupListView.setAdapter(adapter);
        btnStart = (Button) rootView.findViewById(R.id.btnMainStart);
        btnStart.setOnClickListener(((MainActivity)mainActivity).getBtnStartClickListener());
        ((MainActivity)mainActivity).onGpsTrackerBound();
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
    public void setSelectedMetrics(HashMap<String, Metrics> metrics){
        this.metrics = metrics;
    }

    public void setSelectedActivity(NavigationListItems activity){
        this.selectedActivity = activity;
    }
    public NavigationListItems getSelectedActivity(){
        return this.selectedActivity;
    }
    public void setSelectedType(NavigationListItems type){
        this.selectedType = type;
    }
    public NavigationListItems getSelectedType(){
        return this.selectedType;
    }

    public ArrayList<Integer> getSelectedMetrics(){
        //Check the metrics from intent,
        // otherwise, instantiate new metrics object with default selected metrics
        if(metrics==null) {
            metrics = new HashMap<String, Metrics>();
            metrics.put(Calorie.NAME, new Calorie(true, ""));
            metrics.put(Duration.NAME, new Duration(true, ""));
            metrics.put(HeartRate.NAME, new HeartRate(true, ""));
            metrics.put(Distance.NAME, new Distance(true, ""));
            metrics.put(Pace.NAME, new Pace());
            metrics.put(Speed.NAME, new Speed());
        }
        ArrayList<Integer> selectedMetrics = new ArrayList<Integer>();
        for(int i=0;i<metricsList.length;i++){
            if(metrics.get(metricsList[i]).getIsDisplayed()){
                selectedMetrics.add(metrics.get(metricsList[i]).getDrawable());
            }
        }
        //add 0 to fill up 5 slots for icons
        while(selectedMetrics.size()<5){
            selectedMetrics.add(0);
        }
        return selectedMetrics;
    }
    public ArrayList<Metrics> getMetricsList(){
        ArrayList<Metrics> metricsList = new ArrayList<Metrics>();
        metricsList.addAll(metrics.values());
        return metricsList;
    }

    public HashMap<String, Metrics> getMetrics(){
        return metrics;
    }

    public void updateView(GpsStatus mGpsStatus){
//        if(mGpsStatus.isEnabled() == false){
//            btnStart.setBackgroundColor(getResources().getColor(R.color.orange_button));
//            btnStart.setEnabled(true);
//            btnStart.setText("Enable GPS");
//        }else if(mGpsStatus.isLogging() == false){
//            btnStart.setBackgroundColor(getResources().getColor(R.color.orange_button));
//            btnStart.setEnabled(true);
//            btnStart.setText("Start GPS");
//        }else if(mGpsStatus.isFixed() == false){
//            btnStart.setEnabled(false);
//            btnStart.setBackgroundColor(getResources().getColor(R.color.holo_gray_light));
//            btnStart.setText("Waiting for GPS");
//            ((MainActivity)mainActivity).displayGPSSearchingState();
//        }else{
            btnStart.setBackgroundColor(getResources().getColor(R.color.orange_button));
            btnStart.setEnabled(true);
            btnStart.setText("START");
            ((MainActivity)mainActivity).displayGPSBoundState();
//        }
    }
    public Button getStartButton(){
        return btnStart;
    }
    public void setBtnStartListener(View.OnClickListener l){
        btnStart.setOnClickListener(l);
    }
}
