package com.essentia.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.essentia.left_drawer.NavigationListItems;
import com.essentia.util.HRZones;
import com.example.kyawzinlatt94.essentia.R;

import java.io.Serializable;

/**
 * Created by kyawzinlatt94 on 4/4/15.
 *
 * This class contains UI elements for TargetZone, to be inflated in MainActivity
 */
public class TargetZoneFragment extends Fragment{
    private TargetListCustomAdapter adapter;
    private ListView mListView;
    private MainActivity mainActivity;
    private NavigationListItems selectedType;
    private TargetZoneListItems mListItems[];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TargetZoneListItems listItems[] = new TargetZoneListItems[]{
                new TargetZoneListItems(R.drawable.ic_max_zone, HRZones.ZONE5_DES, HRZones.ZONE5_ADV),
                new TargetZoneListItems(R.drawable.ic_vh_zone, HRZones.ZONE4_DES, HRZones.ZONE4_ADV),
                new TargetZoneListItems(R.drawable.ic_m_zone, HRZones.ZONE3_DES, HRZones.ZONE3_ADV),
                new TargetZoneListItems(R.drawable.ic_light_zone, HRZones.ZONE2_DES, HRZones.ZONE2_ADV),
                new TargetZoneListItems(R.drawable.ic_vl_zone, HRZones.ZONE1_DES, HRZones.ZONE1_ADV),
        };
        mListItems = listItems;
        adapter = new TargetListCustomAdapter(getActivity(), R.layout.custom_target_row_layout,listItems);
    }

    public static TypeFragment newInstance() {
        TypeFragment fragment = new TypeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activity_setup, container, false);
        mListView = (ListView)rootView.findViewById(R.id.lv_activity);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mListView.setAdapter(adapter);
        return rootView;
    }

    /**
     * Navigate back to the main fragment and pass the selected value
     */
    private void selectItem(int position){
        mainActivity.targetFragmentCallBack(mListItems[position]);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getString(R.string.title_activity));
        mainActivity = (MainActivity) activity;
    }

    public class TargetZoneListItems implements Serializable {
        public int icon;
        public String title;
        public String benefits;

        public TargetZoneListItems(){
            super();
        }

        public TargetZoneListItems(int icon, String title, String benefits){
            super();
            this.icon = icon;
            this.title = title;
            this.benefits = benefits;
        }
    }

}

