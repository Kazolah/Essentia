package com.essentia.main;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.essentia.hrm.HRProvider;
import com.essentia.left_drawer.NavigationListItems;
import com.essentia.metrics.Calorie;
import com.essentia.metrics.Distance;
import com.essentia.metrics.Duration;
import com.essentia.metrics.HeartRate;
import com.essentia.metrics.Metrics;
import com.essentia.metrics.Pace;
import com.essentia.metrics.Speed;
import com.essentia.tracker.GpsStatus;
import com.essentia.tracker.component.TrackerHRM;
import com.example.kyawzinlatt94.essentia.R;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 *
 * This class is fragment to be inflated by MainActivity, contains UI elements
 */
public class MainFragment extends Fragment{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TrackerHRM trackerHRM;
    private HRProvider hrProvider;
    private String[] metricsList;
    public HashMap<String, Metrics> metrics;

    //UI references
    private ActivitySetUp mainActivity;
    private ListView setupListView;
    private MainFragmentCustomAdapter adapter;

    private NavigationListItems selectedActivity;
    private NavigationListItems selectedType;

    private Button btnStart;
    private Button btnForceStart;

    private ImageView ivHRM;
    private ImageView ivGPS;

    private TextView tvHRMConStatus;
    private TextView tvGPSConStatus;



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

        btnForceStart = (Button) rootView.findViewById(R.id.btnForceStart);
        btnForceStart.setOnClickListener(((MainActivity)mainActivity).getBtnStartClickListener());

        ivHRM = (ImageView) rootView.findViewById(R.id.ivHRMStatus);
        ivGPS = (ImageView) rootView.findViewById(R.id.ivGPSStatus);
        tvGPSConStatus = (TextView) rootView.findViewById(R.id.txtGPSConnection);
        tvHRMConStatus = (TextView) rootView.findViewById(R.id.txtHRMConnection);
        ((MainActivity)mainActivity).onGpsTrackerBound();

        //Check HRM is connected
        trackerHRM = new TrackerHRM();
        hrProvider = trackerHRM.getHrProvider();
        if(hrProvider!=null) {
            ivHRM.setImageResource(R.drawable.ic_heart_rate);
            tvHRMConStatus.setText(getString(R.string.hrm_connection_on));
        }
        return rootView;
    }

    final Target viewTarget = new Target() {
        @Override
        public Point getPoint() {
            return new ViewTarget(getActivity().findViewById(R.id.workout_setup_listview)).getPoint();
        }
    };

    final Target viewTarget1 = new Target() {
        @Override
        public Point getPoint() {
            return new ViewTarget(getActionBarView()).getPoint();
        }
    };
    public View getActionBarView() {
        Window window = getActivity().getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
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

    public void updateHRMIcon(){
        //Heart Rate Connection Icon Update
        if(hrProvider!=null) {
            ivHRM.setImageResource(R.drawable.ic_heart_rate);
            tvHRMConStatus.setText(getString(R.string.hrm_connection_on));
        }else{
            ivHRM.setImageResource(R.drawable.ic_heart_rate_na);
            tvHRMConStatus.setText(getString(R.string.hrm_connection_off));
        }
    }
    public void updateView(GpsStatus mGpsStatus){
        //GPS icon update and button update
        if(mGpsStatus.isEnabled() == false){
            btnStart.setBackgroundColor(getResources().getColor(R.color.orange_button));
            btnStart.setEnabled(true);
            btnStart.setText("Enable GPS");
            btnForceStart.setVisibility(View.GONE);
        }else if(mGpsStatus.isLogging() == false){
            btnForceStart.setVisibility(View.GONE);
            btnStart.setBackgroundColor(getResources().getColor(R.color.orange_button));
            btnStart.setEnabled(true);
            btnStart.setText("Start GPS");
        }else if(mGpsStatus.isFixed() == false){
            btnForceStart.setVisibility(View.VISIBLE);
            btnStart.setEnabled(false);
            btnStart.setBackgroundColor(getResources().getColor(R.color.gray));
            btnStart.setText("Waiting for GPS");
            ((MainActivity)mainActivity).displayGPSSearchingState();
        }else{
            btnForceStart.setVisibility(View.GONE);
            ivGPS.setImageResource(R.drawable.ic_gps_connected);
            tvGPSConStatus.setText(getString(R.string.gps_connection_on));
            btnStart.setBackgroundColor(getResources().getColor(R.color.orange_button));
            btnStart.setEnabled(true);
            btnStart.setText("START");
            ((MainActivity)mainActivity).displayGPSBoundState();
        }
    }
    public Button getStartButton(){
        return btnStart;
    }
    public void setBtnStartListener(View.OnClickListener l){
        btnStart.setOnClickListener(l);
    }


}
