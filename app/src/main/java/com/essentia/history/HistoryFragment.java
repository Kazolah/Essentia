package com.essentia.history;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.essentia.dbHelpers.ActivityDBHelper;
import com.essentia.main.MainActivity;
import com.essentia.support.WorkoutActivity;
import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class HistoryFragment extends Fragment{
    private ListView lsHistory;
    private ArrayList<WorkoutActivity> list;
    private MainActivity historyActivity;
    private HistoryListCustomAdapter adapter;
    private ActivityDBHelper activityDBHelper;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up list items for list view
        context = historyActivity.getApplicationContext();
        activityDBHelper = new ActivityDBHelper(context);
        list = activityDBHelper.queryAllActivity();
        adapter = new HistoryListCustomAdapter(getActivity(), R.layout.custom_history_row_layout,list);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        lsHistory = (ListView) rootView.findViewById(R.id.history_list_view);
        lsHistory.setAdapter(adapter);
        lsHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             selectItem(position);
            }
        });

        return rootView;
    }

    private void selectItem(int position){
       String txtId = list.get(position).getId();
       long id = Long.parseLong(txtId);
//       historyActivity.loadActivity(id);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        historyActivity = (MainActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        historyActivity = null;
    }
}
