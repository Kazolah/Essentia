package com.essentia.workout.workout_pojos;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kyawzinlatt94 on 2/21/15.
 */
public class MetricsUIRef {
    private ImageView iv;
    private TextView desc;
    private TextView value;
    private TextView unit;

    public MetricsUIRef(View view, int ivId, int descId, int valueId, int unitId){
        this.iv = (ImageView) view.findViewById(ivId);
        this.desc = (TextView) view.findViewById(descId);
        this.value = (TextView) view.findViewById(valueId);
        this.unit = (TextView) view.findViewById(unitId);
    }
    public void setResource(int ivId, String desc, String value, String unit){
        setImageView(ivId);
        setDescription(desc);
        setValue(value);
        setUnit(unit);
    }
    public void setImageView(int ivId){
        iv.setImageResource(ivId);
    }
    public void setDescription(String text){
        desc.setText(text);
    }
    public void setValue(String text){
        value.setText(text);
    }
    public void setUnit(String text){
        unit.setText(text);
    }
}
