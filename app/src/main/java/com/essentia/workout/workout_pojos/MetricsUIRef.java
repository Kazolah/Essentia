package com.essentia.workout.workout_pojos;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kyawzinlatt94 on 2/21/15.
 */
public class MetricsUIRef {
    private LinearLayout ll;
    private ImageView iv;
    private TextView tvDesc;
    private TextView tvValue;
    private TextView tvUnit;
    private String description;
    private String value;
    private String unit;
    private int icon;
    public MetricsUIRef(){

    }
    public MetricsUIRef(View view, int llId, int ivId, int descId, int valueId, int unitId){
        this.ll = (LinearLayout) view.findViewById(llId);
        this.iv = (ImageView) view.findViewById(ivId);
        this.tvDesc = (TextView) view.findViewById(descId);
        this.tvValue = (TextView) view.findViewById(valueId);
        this.tvUnit = (TextView) view.findViewById(unitId);
    }
    public void setResource(int ivId, String desc, String value, String unit){
        setImageView(ivId);
        setDescription(desc);
        setValue(value);
        setUnit(unit);
    }
    public void setOnClick(View.OnClickListener listener){
        ll.setOnClickListener(listener);
    }
    public void setImageView(int ivId){
        iv.setImageResource(ivId);
        icon = ivId;
    }
    public void setDescription(String text){
        tvDesc.setText(text);
        description = text;
    }
    public void setValue(String text){
        tvValue.setText(text);
        value = text;
    }
    public void setUnit(String text){
        tvUnit.setText(text);
        unit = text;
    }

    public String getDescription(){
        return description;
    }
    public String getValue(){
        return value;
    }
    public String getUnit(){
        return unit;
    }
    public int getIcon(){
        return icon;
    }

    public LinearLayout getLayout(){
        return ll;
    }

    public int getLayoutId(){
        return ll.getId();
    }
}
