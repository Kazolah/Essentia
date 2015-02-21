package com.essentia.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/9/15.
 */
public class SettingFragment extends Fragment{
    private ListView setupListView;
    private SettingSetUp settingActivity;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up list items for list view
        ListItems listItems[] = new ListItems[]{
                new ListItems(getString(R.string.title_profile),
                        getString(R.string.description_profile)),
                new ListItems(getString(R.string.title_heart_rate_monitor),
                        getString(R.string.description_heart_rate_monitor)),
                new ListItems(getString(R.string.title_workout_preferences),
                        getString(R.string.description_workout_preferences)),
                new ListItems(getString(R.string.title_audio_preferences),
                        getString(R.string.description_audio_preferences)),
                new ListItems(getString(R.string.title_notification_settings),
                        getString(R.string.description_notification_settings)),
                new ListItems(getString(R.string.title_about),
                        getString(R.string.description_about))
        };
        list = populateList(listItems);
        adapter = new SimpleAdapter(getActivity(), list,
                R.layout.custom_setting_row_layout, new String[]{"Title","Description"},
                new int[]{R.id.txtCustomRowSettingTitle, R.id.txtCustomRowSettingDescription});
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        setupListView = (ListView)rootView.findViewById(R.id.setting_listview);
        setupListView.setAdapter(adapter);
        setupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        return rootView;
    }
    private void selectItem(int position){
        if (settingActivity != null) {
            settingActivity.onSettingSetUpSelected(position);
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        ((SettingActivity) activity).onSectionAttached(
//                getString(R.string.title_workout));
        try {
            settingActivity = (SettingSetUp) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement SettingFragment.SettingSetUp.");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        settingActivity = null;
    }
    private ArrayList<HashMap<String, Object>> populateList(ListItems[] listItems){
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for(ListItems listItem:listItems){
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("Title", listItem.title);
            temp.put("Description", listItem.description);
            list.add(temp);
        }
        return list;
    }

    public static interface SettingSetUp{
        void onSettingSetUpSelected(int position);
    }
    private class ListItems {
        public String title;
        public String description;
        public ListItems(String title, String description){
            this.title = title;
            this.description = description;
        }
    }

}
