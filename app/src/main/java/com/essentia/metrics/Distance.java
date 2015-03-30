package com.essentia.metrics;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/12/15.
 */
public class Distance extends Metrics{
    public static final String NAME="Distance";
    private boolean isDisplayed;
    private String value;

    public Distance(){

    }
    public Distance(boolean isDisplayed, String value){
       this.isDisplayed = isDisplayed;
       this.value = value;
    }
    @Override
    public int getDrawable(){
        return isDisplayed?R.drawable.ic_distance:0;
    }

    @Override
    public int getDrawableOn() {
        return R.drawable.distance_on;
    }

    @Override
    public int getDrawableOff() {
        return R.drawable.distance_off;
    }

    @Override
    public int getDisplayTextOn() {
        return R.string.distance_on;
    }

    @Override
    public int getDisplayTextOff() {
        return R.string.distance_off;
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
        return "";
    }

    @Override
    public String getDescription() {
        return "Distance";
    }
}
