package com.essentia.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kyawzinlatt94.essentia.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 2/7/15.
 */
public class WorkoutHRChartFragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_hr_chart, container, false);
        LineChart chart = (LineChart) rootView.findViewById(R.id.hr_chart);
        populateChart(chart);
        return rootView;

    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WorkoutHRChartFragment newInstance() {
        WorkoutHRChartFragment fragment = new WorkoutHRChartFragment();
        return fragment;
    }
    public void populateChart(LineChart chart){
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0);
        valsComp1.add(c1e1);
        Entry c1e2 = new Entry(50.000f, 1);
        valsComp1.add(c1e2);
        Entry c1e3 = new Entry(150.000f, 2);
        valsComp1.add(c1e2);

        Entry c2e1 = new Entry(120.000f, 0);
        valsComp2.add(c2e1);
        Entry c2e2 = new Entry(110.000f, 1);
        valsComp2.add(c2e2);
        Entry c2e3 = new Entry(150.000f, 2);
        valsComp2.add(c2e2);

        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company1");
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Company2");

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.Q"); xVals.add("2.Q"); xVals.add("3.Q"); xVals.add("4.Q");

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.animateXY(3000, 3000);
    }
}
