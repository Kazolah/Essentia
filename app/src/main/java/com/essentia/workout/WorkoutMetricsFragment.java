package com.essentia.workout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essentia.metrics.Metrics;
import com.essentia.workout.workout_pojos.MetricsUIRef;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/4/15.
 */
public class WorkoutMetricsFragment extends Fragment{
    private TextView mTextViewList[];
    private WorkoutActivity workoutActivity;
    private ArrayList<Metrics> metricsList;
    private int totalMetricsCount = 0;
    private String activity;
    private String type;
    private boolean isCreated = false;
    private String listSelectedItem;
    private int selectedLayoutIndex = 0;
    private String txtPreviousSelectedMetrics;
    public HashMap<String, MetricsUIRef> mBundle;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;

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

        listSelectedItem = "";
        metricsRefls = new ArrayList<MetricsUIRef>();
        sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
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
    public void displayTutorial(){
        boolean mainTutorial = sharedPrefs.getBoolean(getString(R.string.pref_tutorial_metrics),true);
        if(mainTutorial) {
            new ShowcaseView.Builder(getActivity())
                    .setStyle(R.style.ThemeOverlay_AppCompat_Dark_ActionBar)
                    .setTarget(viewTarget)
                    .setContentTitle("Metrics Replacement")
                    .setContentText("Select to replace with another metrics")
                    .hideOnTouchOutside()
                    .build();

        }
        editor.putBoolean(getString(R.string.pref_tutorial_metrics), false);
        editor.commit();
    }
    final Target viewTarget = new Target() {
        @Override
        public Point getPoint() {
            return new ViewTarget(getActivity().findViewById(mRef1.getValueId())).getPoint();
        }
    };

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
        if(totalMetricsCount==0) {
            totalMetricsCount = getSelectedMetricsNo(metricsList);
        }
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

    /**
     * Bind ui components according to the metrics count on layout
     * @param view
     */
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

    /**
     * Populate data to UI references
     */
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

    private View.OnClickListener layoutOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == mRef1.getLayoutId()){
                txtPreviousSelectedMetrics = mRef1.getDescription();
                selectedLayoutIndex = 1;
            }else if(v.getId() == mRef2.getLayoutId()){
                txtPreviousSelectedMetrics = mRef2.getDescription();
                selectedLayoutIndex = 2;
            }else if(v.getId() == mRef3.getLayoutId()){
                txtPreviousSelectedMetrics = mRef3.getDescription();
                selectedLayoutIndex = 3;
            }else if(v.getId() == mRef4.getLayoutId()){
                txtPreviousSelectedMetrics = mRef4.getDescription();
                selectedLayoutIndex = 4;
            }else if(v.getId() == mRef5.getLayoutId()){
                txtPreviousSelectedMetrics = mRef5.getDescription();
                selectedLayoutIndex = 5;
            }
            listSelectedItem ="";
            workoutActivity.inflateMetricsListFragment();
        }
    };

    /**
     * Set the selected from list metrics visible
     * @param description
     */
    public void setNewSelectedMetrics(String description){
        this.listSelectedItem = description;
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
        //Previously selected UI Ref
        MetricsUIRef uiRef = getSelectedUIRef();
        if(listSelectedItem.equals("") || uiRef==null || txtPreviousSelectedMetrics.equals(listSelectedItem))
            return;
        if(swapPositionIfPossible(uiRef))
            return;
        setPreviousMetricsInvisible(txtPreviousSelectedMetrics);
        setCurrentMetricsVisible(this.listSelectedItem);
        deploySelectedMetrics(uiRef);
    }

    /**
     * Swap position with the previously selected UI Ref
     * @param uiRef Previously selected UI Ref
     * @return
     */
    public boolean swapPositionIfPossible(MetricsUIRef uiRef){
        boolean possible = false;

        for(MetricsUIRef metricsRef:metricsRefls) {
            if(listSelectedItem.equals(metricsRef.getDescription())) {
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
    public void setCurrentMetricsVisible(String description){
        for(Metrics metrics:metricsList){
            if(metrics.getDescription().equals(description)){
                metrics.setIsDisplayed(true);
            }
        }
    }
    public void deploySelectedMetrics(MetricsUIRef uiRef){
        for (Metrics metrics:metricsList){
            if(metrics.getDescription().equals(listSelectedItem)){
                metrics.setIsDisplayed(true);
                mBundle.remove(uiRef.getDescription());
                uiRef.setResource(metrics.getDrawable(), metrics.getDescription(),
                        metrics.getValue(), metrics.getUnit());
                mBundle.put(metrics.getDescription(), uiRef);
                break;
            }
        }
    }
    public MetricsUIRef getSelectedUIRef(){
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
