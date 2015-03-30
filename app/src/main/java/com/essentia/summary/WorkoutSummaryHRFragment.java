package com.essentia.summary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.essentia.dbHelpers.ActivityHRDetailDBHelper;
import com.essentia.dbHelpers.ActivityHRDetailDBHelper.HRDetailQuery;
import com.essentia.support.WorkoutActivity;
import com.essentia.util.Formatter;
import com.essentia.util.HRZones;
import com.example.kyawzinlatt94.essentia.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 2/8/15.
 */
public class WorkoutSummaryHRFragment extends Fragment{

    private PieChart pieChart;
    private LineChart lineChart;

    private ArrayList<Entry> entryList;
    private ArrayList<String> xVals;

    private float zone1, zone2, zone3, zone4, zone5;
    private Entry z1, z2, z3, z4, z5;
    private ArrayList<Entry> valZones;
    private ArrayList<String> vals;

    private WorkoutSummaryActivity summaryActivity;
    private WorkoutActivity workoutActivity;
    private ActivityHRDetailDBHelper hrDetailDBHelper;
    private ArrayList<HRDetailQuery> hrQueriesList;

    private ImageView ivInfoIcon;
    private TextView tvZone;
    private TextView tvZoneLevel;
    private TextView tvZoneDurationDetail;

    private String selectedZone = "";
    private String zoneBenefits = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hrDetailDBHelper = new ActivityHRDetailDBHelper(getActivity());
        hrQueriesList = summaryActivity.getHRQueriesList();
        workoutActivity = summaryActivity.getWorkoutActivity();
        loadLineChartData();
        loadPieChartData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_summary_hr, container, false);
        lineChart = (LineChart) rootView.findViewById(R.id.hrSummaryLineChart);
        populateChart(lineChart);

        pieChart = (PieChart) rootView.findViewById(R.id.hrSummaryPieChart);
        populateHRZonePieChart(pieChart);

        ivInfoIcon = (ImageView) rootView.findViewById(R.id.fwsh_ivInfo);
        ivInfoIcon.setOnClickListener(ivImageClickListener);
        tvZone = (TextView) rootView.findViewById(R.id.fwsh_tvZone);
        tvZoneLevel = (TextView) rootView.findViewById(R.id.fwsh_tvZoneLevel);
        tvZoneDurationDetail = (TextView) rootView.findViewById(R.id.fwsh_zoneDurationDetail);
        return rootView;
    }

    public void loadPieChartData(){
        int totalDuration = Integer.valueOf(workoutActivity.getDuration());
        int zone1Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE1);
        int zone2Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE2);
        int zone3Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE3);
        int zone4Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE4);
        int zone5Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE5);
        zone1 = ((float)zone1Duration/(float)totalDuration) * 100;
        zone2 = ((float)zone2Duration/(float)totalDuration) * 100;
        zone3 = ((float)zone3Duration/(float)totalDuration) * 100;
        zone4 = ((float)zone4Duration/(float)totalDuration) * 100;
        zone5 = ((float)zone5Duration/(float)totalDuration) * 100;
    }
    public void animateCharts(){
        if(pieChart!=null && lineChart!=null){
            pieChart.animateY(1000);
            lineChart.animateXY(1500,1500);
        }
    }
    public void populateHRZonePieChart(PieChart chart){
        valZones = new ArrayList<Entry>();
        vals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<>();
        if(zone1 != 0) {
            z1 = new Entry(zone1, 0);
            valZones.add(z1);
            vals.add(HRZones.ZONE1_IN_TXT);
            colors.add(getResources().getColor(R.color.gray));
        }
        if(zone2 != 0) {
            z2 = new Entry(zone2, 1);
            valZones.add(z2);
            vals.add(HRZones.ZONE2_IN_TXT);
            colors.add(getResources().getColor(R.color.holo_blue_light));
        }
        if(zone3 != 0) {
            z3 = new Entry(zone3, 2);
            valZones.add(z3);
            vals.add(HRZones.ZONE3_IN_TXT);
            colors.add(getResources().getColor(R.color.holo_green_light));
        }
        if(zone4 != 0) {
            z4 = new Entry(zone4, 3);
            valZones.add(z4);
            vals.add(HRZones.ZONE4_IN_TXT);
            colors.add(getResources().getColor(R.color.orange_normal));
        }
        if(zone5 != 0) {
            z5 = new Entry(zone5, 4);
            valZones.add(z5);
            vals.add(HRZones.ZONE5_IN_TXT);
            colors.add(getResources().getColor(R.color.red));
        }

        PieDataSet zones = new PieDataSet(valZones, "");
        zones.setSliceSpace(3f);
        PieData data = new PieData(vals, zones);
        chart.setDescription("Heart Rate Zones");
        zones.setColors(colors);
        chart.setData(data);
        chart.setOnChartValueSelectedListener(pieChartSelectedListener);
    }
    private void loadLineChartData(){
        entryList = new ArrayList<Entry>();
        xVals = new ArrayList<String>();
        ArrayList<HRDetailQuery> hrDetailList = hrDetailDBHelper.queryHRDetail(summaryActivity.getActivityId());
        int i = 0;
        for(HRDetailQuery detailQuery: hrDetailList){
            xVals.add(Formatter.parseMsIntoTime(detailQuery.getTimeInMs()));
            Float hrValue = Float.valueOf(detailQuery.getHrValue());
            Entry entry = new Entry(hrValue, i);
            entryList.add(entry);
            i++;
        }
    }
    public void populateChart(LineChart chart){
        LineDataSet setHR = new LineDataSet(entryList, "Heart Rate Chart");
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setHR);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.setDescription("Heart Rate");
    }

    private final View.OnClickListener ivImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_heart_rate));
            if(selectedZone.equals("")){
                alertDialog.setTitle("Zone Unavailable");
                alertDialog.setMessage("Select zone on Pie Chart to view its benefits");
            }else{
                alertDialog.setTitle(selectedZone);
                alertDialog.setMessage(zoneBenefits);
            }
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                }
            });
            alertDialog.show();
        }
    };
    private OnChartValueSelectedListener pieChartSelectedListener = new OnChartValueSelectedListener() {

        @Override
        public void onValueSelected(Entry e, int dataSetIndex) {
             for(int i=0;i<valZones.size();i++){
                 if(e == valZones.get(i)){
                     selectedZone = vals.get(i);
                     updateZoneDurationBlock();
                     break;
                 }
             }
        }

        @Override
        public void onNothingSelected() {
            tvZone.setText("HR Zones");
            tvZoneDurationDetail.setText("");
            tvZoneLevel.setText("");
            selectedZone = "";
        }
    };
    public void updateZoneDurationBlock(){
        tvZone.setText(selectedZone);
        switch (selectedZone){
            case HRZones.ZONE1_IN_TXT:
                tvZoneDurationDetail.setText(workoutActivity.getZone1Info());
                tvZoneLevel.setText(HRZones.ZONE1_LEVEL);
                zoneBenefits = HRZones.ZONE1_ADV;
                break;
            case HRZones.ZONE2_IN_TXT:
                tvZoneDurationDetail.setText(workoutActivity.getZone2Info());
                tvZoneLevel.setText(HRZones.ZONE2_LEVEL);
                zoneBenefits = HRZones.ZONE2_ADV;
                break;
            case HRZones.ZONE3_IN_TXT:
                tvZoneDurationDetail.setText(workoutActivity.getZone3Info());
                tvZoneLevel.setText(HRZones.ZONE3_LEVEL);
                zoneBenefits = HRZones.ZONE3_ADV;
                break;
            case HRZones.ZONE4_IN_TXT:
                tvZoneDurationDetail.setText(workoutActivity.getZone4Info());
                tvZoneLevel.setText(HRZones.ZONE4_LEVEL);
                zoneBenefits = HRZones.ZONE4_ADV;
                break;
            case HRZones.ZONE5_IN_TXT:
                tvZoneDurationDetail.setText(workoutActivity.getZone5Info());
                tvZoneLevel.setText(HRZones.ZONE5_LEVEL);
                zoneBenefits = HRZones.ZONE5_ADV;
                break;
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        summaryActivity = (WorkoutSummaryActivity) activity;
    }
}
