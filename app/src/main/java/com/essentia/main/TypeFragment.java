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
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class TypeFragment extends Fragment{
    private ListCustomAdapter adapter;
    private ListView mListView;
    private MainActivity mainActivity;
    private NavigationListItems selectedType;
    private NavigationListItems mListItems[];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationListItems listItems[] = new NavigationListItems[]{
                new NavigationListItems(R.drawable.basic_workout, getString(R.string.basic_workout)),
                new NavigationListItems(R.drawable.target_chasing, getString(R.string.target_heart_rate)),
        };
        mListItems = listItems;
        adapter = new ListCustomAdapter(getActivity(), R.layout.custom_drawer_row_layout,listItems);
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
        mainActivity.typeCallBack(mListItems[position]);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getString(R.string.title_activity));
        mainActivity = (MainActivity) activity;
    }
    private void setSelectedType(NavigationListItems type){
        this.selectedType = type;
    }
}
