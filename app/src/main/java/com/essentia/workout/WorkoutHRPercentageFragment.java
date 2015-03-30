package com.essentia.workout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.essentia.util.HRZones;
import com.example.kyawzinlatt94.essentia.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 2/7/15.
 */
public class WorkoutHRPercentageFragment extends Fragment{
    private BarChart hrBarChart;
    private BarEntry e1, e2, e3, e4, e5;
    private BarDataSet setZone1, setZone2, setZone3, setZone4, setZone5;
    private MyMarkerView mv;
    private HRZones hrZones;
    private WorkoutActivity workoutActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_hr_percentage, container, false);
        hrBarChart = (BarChart) rootView.findViewById(R.id.fwhp_hrzones_chart);
        hrBarChart.disableScroll();
        hrBarChart.setBackgroundColor(getResources().getColor(R.color.white));
        hrBarChart.setDescription("HR Zones");
        hrBarChart.setOnChartValueSelectedListener(new ChartValueSelectedListener());
        hrBarChart.setMarkerView(mv);
        return rootView;
    }
    public void updateView(HRZones hrZones){
        this.hrZones = hrZones;
        populateBarChart(hrZones);
//        updateBarChart(hrZones);
    }
    public void populateBarChart(HRZones hrZones){
        mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setOffsets(-mv.getMeasuredWidth()/2, -mv.getMeasuredHeight());
        ArrayList<BarEntry> zone1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> zone2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> zone3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> zone4 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> zone5 = new ArrayList<BarEntry>();

        e1 = new BarEntry((float)hrZones.getZonePercentage(HRZones.ZONE1, workoutActivity.getTotalDurationLong()), 0);
        zone1.add(e1);

        e2 = new BarEntry((float)hrZones.getZonePercentage(HRZones.ZONE2, workoutActivity.getTotalDurationLong()), 0);
//        e2 = new BarEntry(10.000f, 0);
        zone2.add(e2);

        e3 = new BarEntry((float)hrZones.getZonePercentage(HRZones.ZONE3, workoutActivity.getTotalDurationLong()), 0);
//        e3 = new BarEntry(35.000f, 0);
        zone3.add(e3);

        e4 = new BarEntry((float)hrZones.getZonePercentage(HRZones.ZONE4, workoutActivity.getTotalDurationLong()), 0);
//        e4 = new BarEntry(15.000f, 0);
        zone4.add(e4);

        e5 = new BarEntry((float)hrZones.getZonePercentage(HRZones.ZONE5, workoutActivity.getTotalDurationLong()), 0);
//        e5 = new BarEntry(20.000f, 0);
        zone5.add(e5);

        setZone1 = new BarDataSet(zone1, "Zone 1");
        setZone1.setColor(getResources().getColor(R.color.holo_gray_dark));

        setZone2 = new BarDataSet(zone2, "Zone 2");
        setZone2.setColor(getResources().getColor(R.color.holo_blue_light));

        setZone3 = new BarDataSet(zone3, "Zone 3");
        setZone3.setColor(getResources().getColor(R.color.holo_green_light));

        setZone4 = new BarDataSet(zone4, "Zone 4");
        setZone4.setColor(getResources().getColor(R.color.orange_normal));

        setZone5 = new BarDataSet(zone5, "Zone 5");
        setZone5.setColor(getResources().getColor(R.color.red));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setZone1);
        dataSets.add(setZone2);
        dataSets.add(setZone3);
        dataSets.add(setZone4);
        dataSets.add(setZone5);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Zones");

        BarData data = new BarData(xVals, dataSets);
        hrBarChart.setData(data);

    }
    public void updateBarChart(HRZones hrZones){
        ArrayList<BarEntry> zone1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> zone2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> zone3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> zone4 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> zone5 = new ArrayList<BarEntry>();

        setZone1.removeEntry(e1);
        e1 = new BarEntry((float)hrZones.getZonePercentage(HRZones.ZONE1, workoutActivity.getTotalDurationLong()), 0);
        setZone1.addEntry(e1);
    }
    public static WorkoutHRPercentageFragment newInstance(){
        return new WorkoutHRPercentageFragment();
    }
    public class ChartValueSelectedListener implements OnChartValueSelectedListener{

        @Override
        public void onValueSelected(Entry e, int dataSetIndex) {
            if(e.equals(e1)){
                mv.setMarkerResource(hrZones.getZone1DurationDetails());
            }
            if(e.equals(e2)){
                mv.setMarkerResource(hrZones.getZone2DurationDetails());
            }
            if(e.equals(e3)){
                mv.setMarkerResource(hrZones.getZone3DurationDetails());
            }
            if(e.equals(e4)){
                mv.setMarkerResource(hrZones.getZone4DurationDetails());
            }
            if(e.equals(e5)){
                mv.setMarkerResource(hrZones.getZone5DurationDetails());
            }
        }

        @Override
        public void onNothingSelected() {

        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        workoutActivity = (WorkoutActivity) activity;
    }

}

