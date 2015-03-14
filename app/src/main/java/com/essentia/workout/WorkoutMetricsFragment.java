package com.essentia.workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essentia.metrics.Metrics;
import com.essentia.support.Scope;
import com.essentia.util.Formatter;
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
    private WorkoutActivity workoutActivity;
    private ArrayList<Metrics> metricsList;
    private int totalMetricsCount;
    private String activity;
    private String type;
    private boolean isCreated = false;
    private String descSelected;
    private Formatter formatter;

    private int selectedLayoutIndex;
    private String txtSelectedMetrics;
    public HashMap<String, MetricsUIRef> mBundle;


    //UI Component References
    private MetricsUIRef mRef1, mRef2, mRef3, mRef4, mRef5;
    private ArrayList<MetricsUIRef> metricsRefls;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getActivity().getIntent();
        Bundle bundles = i.getExtras();
        mBundle = new HashMap<String, MetricsUIRef>();
        metricsList = (ArrayList<Metrics>)bundles.getSerializable("Metrics");
        activity = (String) bundles.getSerializable("Activity");
        type = (String) bundles.getSerializable("Type");
        descSelected = "";
        metricsRefls = new ArrayList<MetricsUIRef>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        bindUIComponents(rootView);
        setResources();
        populateMetricsRefList();
        populateMetrics();
        return rootView;
    }
    private void populateMetricsRefList(){
        metricsRefls.clear();
        metricsRefls.add(mRef1);
        metricsRefls.add(mRef2);
        metricsRefls.add(mRef3);
        if(totalMetricsCount==4)
            metricsRefls.add(mRef4);
        if(totalMetricsCount==5)
            metricsRefls.add(mRef5);
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
        updateMetricsValue(((MetricsUIRef) mBundle.get("Duration")), workout.getMetrics("Duration"));
        updateMetricsValue(((MetricsUIRef) mBundle.get("Heart Rate")), workout.getMetrics("Heart Rate"));
        updateMetricsValue(((MetricsUIRef) mBundle.get("Pace")), workout.getMetrics("Pace"));
        updateMetricsValue(((MetricsUIRef) mBundle.get("Speed")), workout.getMetrics("Speed"));
        updateMetricsValue(((MetricsUIRef) mBundle.get("Distance")), workout.getMetrics("Distance"));
        updateMetricsValue(((MetricsUIRef) mBundle.get("Calorie")), workout.getMetrics("Calorie"));
    }
    private void updateMetricsValue(MetricsUIRef metrics, String value){
        if(metrics != null){
            metrics.setValue(value);
        }
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
                mRef1 = new MetricsUIRef(view, R.id.fw3m_ll1, R.id.fw3m_iv1, R.id.fw3m_desc1,
                        R.id.fw3m_val1, R.id.fw3m_unit1);
                mRef2 = new MetricsUIRef(view, R.id.fw3m_ll2, R.id.fw3m_iv2, R.id.fw3m_desc2,
                        R.id.fw3m_val2, R.id.fw3m_unit2);
                mRef3 = new MetricsUIRef(view, R.id.fw3m_ll3, R.id.fw3m_iv3, R.id.fw3m_desc3,
                        R.id.fw3m_val3, R.id.fw3m_unit3);
                break;
            case 4:
                mRef1 = new MetricsUIRef(view, R.id.fw4m_ll1, R.id.fw4m_iv1, R.id.fw4m_desc1,
                        R.id.fw4m_val1, R.id.fw4m_unit1);
                mRef2 = new MetricsUIRef(view, R.id.fw4m_ll2, R.id.fw4m_iv2, R.id.fw4m_desc2,
                        R.id.fw4m_val2, R.id.fw4m_unit2);
                mRef3 = new MetricsUIRef(view, R.id.fw4m_ll3, R.id.fw4m_iv3, R.id.fw4m_desc3,
                        R.id.fw4m_val3, R.id.fw4m_unit3);
                mRef4 = new MetricsUIRef(view, R.id.fw4m_ll4, R.id.fw4m_iv4, R.id.fw4m_desc4,
                        R.id.fw4m_val4, R.id.fw4m_unit4);
                break;
            case 5:
                mRef1 = new MetricsUIRef(view, R.id.fw5m_ll1, R.id.fw5m_iv1, R.id.fw5m_desc1,
                        R.id.fw5m_val1, R.id.fw5m_unit1);
                mRef2 = new MetricsUIRef(view, R.id.fw5m_ll2, R.id.fw5m_iv2, R.id.fw5m_desc2,
                        R.id.fw5m_val2, R.id.fw5m_unit2);
                mRef3 = new MetricsUIRef(view, R.id.fw5m_ll3, R.id.fw5m_iv3, R.id.fw5m_desc3,
                        R.id.fw5m_val3, R.id.fw5m_unit3);
                mRef4 = new MetricsUIRef(view, R.id.fw5m_ll4, R.id.fw5m_iv4, R.id.fw5m_desc4,
                        R.id.fw5m_val4, R.id.fw5m_unit4);
                mRef5 = new MetricsUIRef(view, R.id.fw5m_ll5, R.id.fw5m_iv5, R.id.fw5m_desc5,
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
                        mRef1.setOnClick(layoutOnClick);
                        mBundle.put(metrics.getDescription(), mRef1);
                        break;
                    case 2:
                        mRef2.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mRef2.setOnClick(layoutOnClick);
                        mBundle.put(metrics.getDescription(), mRef2);
                        break;
                    case 3:
                        mRef3.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mRef3.setOnClick(layoutOnClick);
                        mBundle.put(metrics.getDescription(), mRef3);
                        break;
                    case 4:
                        mRef4.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mRef4.setOnClick(layoutOnClick);
                        mBundle.put(metrics.getDescription(), mRef4);
                        break;
                    case 5:
                        mRef5.setResource(metrics.getDrawable(),metrics.getDescription(),
                                metrics.getValue(), metrics.getUnit());
                        mRef5.setOnClick(layoutOnClick);
                        mBundle.put(metrics.getDescription(), mRef5);
                        break;
                }
                count++;
            }
        }
    }

    final View.OnClickListener layoutOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == mRef1.getLayoutId()){
                txtSelectedMetrics = mRef1.getDescription();
                selectedLayoutIndex = 1;
            }else if(v.getId() == mRef2.getLayoutId()){
                txtSelectedMetrics = mRef2.getDescription();
                selectedLayoutIndex = 2;
            }else if(v.getId() == mRef3.getLayoutId()){
                txtSelectedMetrics = mRef3.getDescription();
                selectedLayoutIndex = 3;
            }else if(v.getId() == mRef4.getLayoutId()){
                txtSelectedMetrics = mRef4.getDescription();
                selectedLayoutIndex = 4;
            }else if(v.getId() == mRef5.getLayoutId()){
                txtSelectedMetrics = mRef5.getDescription();
                selectedLayoutIndex = 5;
            }
            descSelected ="";
            workoutActivity.inflateMetricsListFragment();
        }
    };

    public void setNewSelectedMetrics(MetricsUIRef metricsUIRef, String description){
        this.descSelected = description;
    }
    public void setRefResource(MetricsUIRef currentRef, MetricsUIRef previousRef){
        currentRef.setResource(previousRef.getIcon(), previousRef.getDescription(),
                                previousRef.getValue(), previousRef.getUnit());
        mBundle.remove(previousRef.getDescription());
        mBundle.put(previousRef.getDescription(), currentRef);
    }

    /**
     * Update with new selected metrics
     */
    public void populateMetrics(){
        MetricsUIRef uiRef = getSelectedLayout();
        if(descSelected.equals("") || uiRef==null || txtSelectedMetrics.equals(descSelected))
            return;
        if(swapPositionIfPossible(uiRef))
            return;
        setPreviousMetricsInvisible(uiRef.getDescription());
        deploySelectedMetrics(uiRef);
    }

    public boolean swapPositionIfPossible(MetricsUIRef uiRef){
        boolean possible = false;
        for(MetricsUIRef metricsRef:metricsRefls) {
            if(descSelected.equals(metricsRef.getDescription())) {
                String desc = metricsRef.getDescription();
                String unit = metricsRef.getUnit();
                int icon = metricsRef.getIcon();
                mBundle.remove(metricsRef.getDescription());
                mBundle.remove(uiRef.getDescription());
                metricsRef.setResource(uiRef.getIcon(),uiRef.getDescription(),"",uiRef.getUnit());
                uiRef.setResource(icon, desc,"",unit);
                mBundle.put(metricsRef.getDescription(), metricsRef);
                mBundle.put(uiRef.getDescription(), uiRef);
                possible = true;
            }
        }
        return possible;
    }
    public void setPreviousMetricsInvisible(String description){
        for(Metrics metrics:metricsList){
            if(metrics.getDescription().equals(description)){
                metrics.setIsDisplayed(false);
            }
        }
    }
    public void deploySelectedMetrics(MetricsUIRef uiRef){
        for (Metrics metrics:metricsList){
            if(metrics.getDescription().equals(descSelected)){
                metrics.setIsDisplayed(true);
                mBundle.remove(uiRef.getDescription());
                uiRef.setResource(metrics.getDrawable(), metrics.getDescription(),
                        metrics.getValue(), metrics.getUnit());
                mBundle.put(metrics.getDescription(), uiRef);
                break;
            }
        }
    }
    public MetricsUIRef getSelectedLayout(){
        switch (selectedLayoutIndex){
            case 1: return mRef1;
            case 2: return mRef2;
            case 3: return mRef3;
            case 4: return mRef4;
            case 5: return mRef5;
            default: return null;
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        workoutActivity = (WorkoutActivity) activity;
    }
    public ArrayList<Metrics> getMetricsList(){
        return metricsList;
    }
}
