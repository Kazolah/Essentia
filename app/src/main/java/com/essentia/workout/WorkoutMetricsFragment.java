package com.essentia.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essentia.metrics.Metrics;
import com.essentia.support.Scope;
import com.essentia.workout.workout_pojos.MetricsUIRef;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/4/15.
 */
public class WorkoutMetricsFragment extends Fragment{
    private TextView mTextViewList[];
    private ArrayList<Metrics> metricsList;
    private int totalMetricsCount;
    private String activity;
    private String type;
    private HashMap<String, MetricsUIRef> mBundle;
    //UI Component References
    private MetricsUIRef mRef1, mRef2, mRef3, mRef4, mRef5;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getActivity().getIntent();
        Bundle bundles = i.getExtras();
        metricsList = (ArrayList<Metrics>)bundles.getSerializable("Metrics");
        activity = (String) bundles.getSerializable("Activity");
        type = (String) bundles.getSerializable("Type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        bindUIComponents(rootView);
        setResources();
        return rootView;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WorkoutMetricsFragment newInstance() {
        WorkoutMetricsFragment fragment = new WorkoutMetricsFragment();
        return fragment;
    }
    public void updateView(Workout workout){
        double distance = workout.getDistance(Scope.WORKOUT);
        String duration = workout.getDuration();
        ((MetricsUIRef) mBundle.get("Duration")).setValue(duration);

    }
    private int getSelectedMetricsNo(ArrayList<Metrics> metricsList){
        int count = 0;
        for(Metrics m: metricsList){
            if(m.getIsDisplayed()){
                count++;
            }
        }
        return count;
    }

    /**
     * Return the layout according to the number of selected metrics
     */
    private int getLayout(){
        int layout;
        totalMetricsCount = getSelectedMetricsNo(metricsList);
        switch (totalMetricsCount){
            case 3:
                layout = R.layout.fragment_workout_3metrics;
                break;
            case 4:
                layout = R.layout.fragment_workout_4metrics;
                break;
            case 5:
                layout = R.layout.fragment_workout_5metrics;
                break;
            default:
                layout = R.layout.fragment_workout_3metrics;
                break;
        }
        return layout;
    }

    private void bindUIComponents(View view){
        switch (totalMetricsCount){
            case 3:
                mRef1 = new MetricsUIRef(view, R.id.fw3m_iv1, R.id.fw3m_desc1,
                        R.id.fw3m_val1, R.id.fw3m_unit1);
                mRef2 = new MetricsUIRef(view, R.id.fw3m_iv2, R.id.fw3m_desc2,
                        R.id.fw3m_val2, R.id.fw3m_unit2);
                mRef3 = new MetricsUIRef(view, R.id.fw3m_iv3, R.id.fw3m_desc3,
                        R.id.fw3m_val3, R.id.fw3m_unit3);
                break;
            case 4:
                mRef1 = new MetricsUIRef(view, R.id.fw4m_iv1, R.id.fw4m_desc1,
                        R.id.fw4m_val1, R.id.fw4m_unit1);
                mRef2 = new MetricsUIRef(view, R.id.fw4m_iv2, R.id.fw4m_desc2,
                        R.id.fw4m_val2, R.id.fw4m_unit2);
                mRef3 = new MetricsUIRef(view, R.id.fw4m_iv3, R.id.fw4m_desc3,
                        R.id.fw4m_val3, R.id.fw4m_unit3);
                mRef4 = new MetricsUIRef(view, R.id.fw4m_iv4, R.id.fw4m_desc4,
                        R.id.fw4m_val4, R.id.fw4m_unit4);
                break;
            case 5:
                mRef1 = new MetricsUIRef(view, R.id.fw5m_iv1, R.id.fw5m_desc1,
                        R.id.fw5m_val1, R.id.fw5m_unit1);
                mRef2 = new MetricsUIRef(view, R.id.fw5m_iv2, R.id.fw5m_desc2,
                        R.id.fw5m_val2, R.id.fw5m_unit2);
                mRef3 = new MetricsUIRef(view, R.id.fw5m_iv3, R.id.fw5m_desc3,
                        R.id.fw5m_val3, R.id.fw5m_unit3);
                mRef4 = new MetricsUIRef(view, R.id.fw5m_iv4, R.id.fw5m_desc4,
                        R.id.fw5m_val4, R.id.fw5m_unit4);
                mRef5 = new MetricsUIRef(view, R.id.fw5m_iv5, R.id.fw5m_desc5,
                        R.id.fw5m_val5, R.id.fw5m_unit5);
            break;
        }
    }

    private void setResources(){
        int count = 1;
        for(Metrics metrics: metricsList){
            if(metrics.getIsDisplayed()) {
                switch (count) {
                    case 1:
                        mRef1.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mBundle.put(metrics.getDescription(), mRef1);
                        break;
                    case 2:
                        mRef2.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mBundle.put(metrics.getDescription(), mRef2);
                        break;
                    case 3:
                        mRef3.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mBundle.put(metrics.getDescription(), mRef3);
                        break;
                    case 4:
                        mRef4.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mBundle.put(metrics.getDescription(), mRef4);
                        break;
                    case 5:
                        mRef5.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mBundle.put(metrics.getDescription(), mRef5);
                        break;
                }
                count++;
            }
        }

    }
}
