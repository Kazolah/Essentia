package com.essentia.main;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.essentia.metrics.Calorie;
import com.essentia.metrics.Distance;
import com.essentia.metrics.Duration;
import com.essentia.metrics.HeartRate;
import com.essentia.metrics.Metrics;
import com.essentia.metrics.Pace;
import com.essentia.metrics.Speed;
import com.example.kyawzinlatt94.essentia.R;

import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class MetricsFragment extends Fragment {
    public static final int CAPACITY_METRICS = 5;
    public String errorMsg;

    //Default Metrics selection
    public int totalMetrics = 4;
    public HashMap<String, Metrics> metrics;
    private MainActivity mainActivity;

    //UI components references
    private View rootView;
    private ImageView ivCalorie;
    private ImageView ivHeartRate;
    private ImageView ivDistance;
    private ImageView ivPace;
    private ImageView ivSpeed;
    private ImageView ivDuration;

    private TextView tvCalorie;
    private TextView tvHeartRate;
    private TextView tvDistance;
    private TextView tvPace;
    private TextView tvSpeed;
    private TextView tvDuration;

    //The below lists' components serial must be matched
    private ImageView[] ivList;
    private TextView[] tvList;
    private String[] metricsNameList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);
        metrics = (HashMap<String, Metrics>) bundle.getSerializable("Metrics");
        bundle.remove("Metrics");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_metrics_setup, container, false);

        ivCalorie = (ImageView)rootView.findViewById(R.id.fms_iv_calorie);
        ivHeartRate = (ImageView)rootView.findViewById(R.id.fms_iv_heart_rate);
        ivDistance = (ImageView)rootView.findViewById(R.id.fms_iv_distance);
        ivPace = (ImageView)rootView.findViewById(R.id.fms_iv_pace);
        ivSpeed = (ImageView)rootView.findViewById(R.id.fms_iv_speed);
        ivDuration = (ImageView)rootView.findViewById(R.id.fms_iv_duration);

        tvCalorie = (TextView)rootView.findViewById(R.id.fms_tv_calorie);
        tvHeartRate = (TextView)rootView.findViewById(R.id.fms_tv_heart_rate);
        tvDistance = (TextView)rootView.findViewById(R.id.fms_tv_distance);
        tvPace = (TextView)rootView.findViewById(R.id.fms_tv_pace);
        tvSpeed = (TextView)rootView.findViewById(R.id.fms_tv_speed);
        tvDuration = (TextView)rootView.findViewById(R.id.fms_tv_duration);

        ivList = new ImageView[]{ivCalorie, ivDistance, ivDuration, ivHeartRate, ivPace, ivSpeed};
        tvList = new TextView[]{tvCalorie, tvDistance, tvDuration, tvHeartRate, tvPace, tvSpeed};
        metricsNameList = new String[]{Calorie.NAME, Distance.NAME, Duration.NAME, HeartRate.NAME,
                Pace.NAME, Speed.NAME};

        //Set Selection for the metrics
        setSelectedMetrics();

        //OnClickListeners for the metrics images
        ivCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvCalorie.getText().toString().equals(getString(R.string.calorie_off))){
                    ivCalorie.setImageResource(R.drawable.calorie_on);
                    tvCalorie.setText(getString(R.string.calorie_on));
                }
                else{
                    ivCalorie.setImageResource(R.drawable.calorie_off);
                    tvCalorie.setText(getString(R.string.calorie_off));
                }
            }
        });
        ivHeartRate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(tvHeartRate.getText().toString().equals(getString(R.string.heart_rate_off))){
                    ivHeartRate.setImageResource(R.drawable.metrics_heart_rate_on);
                    tvHeartRate.setText(getString(R.string.heart_rate_on));
                    addMetrics();
                }
                else{
                    ivHeartRate.setImageResource(R.drawable.metrics_heart_rate_off);
                    tvHeartRate.setText(getString(R.string.heart_rate_off));
                    removeMetrics();
                }
            }
        });
        ivDistance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(tvDistance.getText().toString().equals(getString(R.string.distance_off))){
                    ivDistance.setImageResource(R.drawable.distance_on);
                    tvDistance.setText(getString(R.string.distance_on));
                    addMetrics();
                }
                else{
                    ivDistance.setImageResource(R.drawable.distance_off);
                    tvDistance.setText(getString(R.string.distance_off));
                    removeMetrics();
                }
            }
        });
        ivDuration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(tvDuration.getText().toString().equals(getString(R.string.duration_off))){
                    ivDuration.setImageResource(R.drawable.duration_on);
                    tvDuration.setText(getString(R.string.duration_on));
                    addMetrics();
                }
                else{
                    ivDuration.setImageResource(R.drawable.duration);
                    tvDuration.setText(getString(R.string.duration_off));
                    removeMetrics();
                }
            }
        });
        ivPace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(tvPace.getText().toString().equals(getString(R.string.pace_off))){
                    ivPace.setImageResource(R.drawable.pace_on);
                    tvPace.setText(getString(R.string.pace_on));
                    addMetrics();
                }
                else{
                    ivPace.setImageResource(R.drawable.pace_off);
                    tvPace.setText(getString(R.string.pace_off));
                    removeMetrics();
                }
            }
        });
        ivSpeed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(tvSpeed.getText().toString().equals(getString(R.string.speed_off))){
                    ivSpeed.setImageResource(R.drawable.speed_on);
                    tvSpeed.setText(getString(R.string.speed_on));
                    addMetrics();
                }
                else{
                    ivSpeed.setImageResource(R.drawable.speed_off);
                    tvSpeed.setText(getString(R.string.speed_off));
                    removeMetrics();
                }
            }
        });

        return rootView;
    }

    public static MetricsFragment newInstance() {
        MetricsFragment fragment = new MetricsFragment();
        return fragment;
    }

    //Create menu with Done button
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_metrics_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_metrics_selection:
                if(!checkCapacity()){
                    showErrorMsg();
                    return false;
                }
                //Total Metrics must be more than 2
                if(totalMetrics<=2){
                    errorMsg = "Select at least 3 metrics.";
                    showErrorMsg();
                    return false;
                }else {
                    updateSelectedMetrics();
                    mainActivity.metricsCallback(metrics);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getString(R.string.title_workout));
        try {
            mainActivity = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ActivitySetUp.");
        }
    }

    /**
     * Update the selection of the metrics according to
     * the list received from the main fragment
     */
    public void updateSelectedMetrics(){
        for(int i=0;i<metricsNameList.length;i++){
            Metrics temp = metrics.get(metricsNameList[i]);
            String textView = tvList[i].getText().toString();
            String metricsTextOn = getString(temp.getDisplayTextOn());
            if(textView.equals(metricsTextOn)){
                metrics.get(metricsNameList[i]).setIsDisplayed(true);
            }else {
                metrics.get(metricsNameList[i]).setIsDisplayed(false);
            }
        }
    }

    /**
     * Set selection for the metrics
     * according to info from bundle
     */
    public void setSelectedMetrics(){
        for(int i=0;i<metricsNameList.length;i++){
            Metrics temp = metrics.get(metricsNameList[i]);
            if(temp.getIsDisplayed()){
                tvList[i].setText(getString(temp.getDisplayTextOn()));
                ivList[i].setImageResource(temp.getDrawableOn());
            }
            else{
                tvList[i].setText(getString(temp.getDisplayTextOff()));
                ivList[i].setImageResource(temp.getDrawableOff());
            }
        }
    }

    /**
     * Check total selected metrics is under limit
     * @return boolean
     */
    public boolean checkCapacity(){
        if(totalMetrics > CAPACITY_METRICS){
            errorMsg = "Maximum 5 metrics on selection.";
            return false;
        }else
            return true;
    }
    public void showErrorMsg(){
        Toast.makeText(getActivity(),errorMsg, Toast.LENGTH_LONG).show();
    }
    public void addMetrics(){
        totalMetrics++;
    }
    public void removeMetrics(){
        totalMetrics--;
    }
}
