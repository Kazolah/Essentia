package com.essentia.statistics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.essentia.dbHelpers.ActivityDBHelper;
import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class StatisticsFragment extends Fragment{
    private TextView tvCalorie;
    private TextView tvAvgHeartRate;
    private TextView tvDistance;
    private ListView lsStatistics;
    private ArrayList<StatisticListItem> list;
    private StatisticsActivity statisticsActivity;
    private StatisticsCustomAdapter adapter;
    private ActivityDBHelper activityDBHelper;
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up list items for list view
        context = statisticsActivity.getApplicationContext();
        activityDBHelper = new ActivityDBHelper(context);
        list = activityDBHelper.getStatisticsItems();
        adapter = new StatisticsCustomAdapter(getActivity(), R.layout.custom_statistics_row_layout,list);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        lsStatistics = (ListView)rootView.findViewById(R.id.statistics_list_view);
        lsStatistics.setAdapter(adapter);

        tvCalorie = (TextView) rootView.findViewById(R.id.fs_calorie);
        tvCalorie.setText(activityDBHelper.getTotalCalorie());

        tvAvgHeartRate = (TextView) rootView.findViewById(R.id.fs_hr);
        tvAvgHeartRate.setText(activityDBHelper.getTotalAvgHR());

        tvDistance = (TextView) rootView.findViewById(R.id.fs_distance);
        tvDistance.setText(activityDBHelper.getTotalDistance());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        statisticsActivity = (StatisticsActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        statisticsActivity = null;
    }
}
