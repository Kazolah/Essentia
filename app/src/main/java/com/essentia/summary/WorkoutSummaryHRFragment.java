package com.essentia.summary;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.essentia.dbHelpers.ActivityHRDetailDBHelper;
import com.essentia.dbHelpers.ActivityHRDetailDBHelper.HRDetailQuery;
import com.essentia.support.WorkoutActivity;
import com.essentia.util.Formatter;
import com.essentia.util.HRZones;
import com.essentia.util.WidgetUtil;
import com.example.kyawzinlatt94.essentia.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 2/8/15.
 */
public class WorkoutSummaryHRFragment extends Fragment{
    private TabSpec tabSpec;
    private PieChart pieChart;
    private LineChart lineChart;

    private WorkoutActivity workoutActivity;
    private ActivityHRDetailDBHelper hrDetailDBHelper;
    private ArrayList<HRDetailQuery> hrQueriesList;
    WorkoutSummaryActivity summaryActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hrDetailDBHelper = new ActivityHRDetailDBHelper(getActivity());
        hrQueriesList = summaryActivity.getHRQueriesList();
        workoutActivity = summaryActivity.getWorkoutActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_summary_data, container, false);
        TabHost th = (TabHost) rootView.findViewById(R.id.tabHost);
        th.setup();
        tabSpec = th.newTabSpec("HR Data");
        tabSpec.setIndicator(WidgetUtil.createHoloTabIndicator(getActivity(),"HR Chart"));
        tabSpec.setContent(R.id.tabChart);
        th.findViewById(R.id.tabChart);

        tabSpec = th.newTabSpec("HR Zones");
        tabSpec.setIndicator(WidgetUtil.createHoloTabIndicator(getActivity(),"HR Zones"));
        tabSpec.setContent(R.id.tabChart2);
        th.findViewById(R.id.tabChart2);
        th.getTabWidget().setBackgroundColor(Color.WHITE);
        return rootView;
    }

    public void populateHRZonePieChart(PieChart chart){
        ArrayList<Entry> valZones = new ArrayList<Entry>();
        int totalDuration = Integer.valueOf(workoutActivity.getDuration());
        int zone1Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE1);
        int zone2Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE2);
        int zone3Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE3);
        int zone4Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE4);
        int zone5Duration = hrDetailDBHelper.queryZonePercentage(workoutActivity.getId(), HRZones.ZONE5);
        float zone1 = zone1Duration/totalDuration;
        float zone2 = zone2Duration/totalDuration;
        float zone3 = zone3Duration/totalDuration;
        float zone4 = zone4Duration/totalDuration;
        float zone5 = zone5Duration/totalDuration;

        Entry z1 = new Entry(zone1, 0);
        Entry z2 = new Entry(zone2, 0);
        Entry z3 = new Entry(zone3, 0);
        Entry z4 = new Entry(zone4, 0);
        Entry z5 = new Entry(zone5, 0);
        valZones.add(z1);
        valZones.add(z2);
        valZones.add(z3);
        valZones.add(z4);
        valZones.add(z5);

        PieDataSet zones = new PieDataSet(valZones, "Heart Rate Zone");
        ArrayList<String> vals = new ArrayList<String>();
        PieData data = new PieData(vals, zones);
        chart.setData(data);
        chart.animateXY(3000, 3000);
    }

    public void populateChart(LineChart chart){
        ArrayList<Entry> entryList = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<HRDetailQuery> hrDetailList = hrDetailDBHelper.queryHRDetail(summaryActivity.getActivityId());
        int i = 0;
        for(HRDetailQuery detailQuery: hrDetailList){
            xVals.add(Formatter.parseMsIntoTime(detailQuery.getTimeInMs()));
            Float hrValue = Float.valueOf(detailQuery.getHrValue());
            Entry entry = new Entry(hrValue, i);
            entryList.add(entry);
            i++;
        }
        LineDataSet setHR = new LineDataSet(entryList, "HR");

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setHR);

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.animateXY(3000, 3000);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        summaryActivity = (WorkoutSummaryActivity) activity;
    }
}
