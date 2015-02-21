package com.essentia.metrics;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/11/15.
 */
public class HeartRate extends Metrics{
    public static final String NAME="HeartRate";
    private String value;
    private boolean isDisplayed;

    public HeartRate(){

    }
    public HeartRate(boolean isDisplayed, String value){
        this.isDisplayed = isDisplayed;
        this.value = value;
    }
    @Override
    public int getDrawable(){
        return isDisplayed?R.drawable.ic_heart_rate:0;
    }

    @Override
    public int getDrawableOn(){
        return R.drawable.metrics_heart_rate_on;
    }

    @Override
    public int getDrawableOff(){
        return R.drawable.metrics_heart_rate_off;
    }

    @Override
    public int getDisplayTextOn() {
        return R.string.heart_rate_on;
    }

    @Override
    public int getDisplayTextOff() {
        return R.string.heart_rate_off;
    }

    @Override
    public boolean getIsDisplayed() {
        return isDisplayed;
    }

    @Override
    public void setIsDisplayed(boolean isDisplayed) {
        this.isDisplayed = isDisplayed;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getUnit() {
        return "bpm";
    }

    @Override
    public String getDescription() {
        return "Heart Rate";
    }
}
