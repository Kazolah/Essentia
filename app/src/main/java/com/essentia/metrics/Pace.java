package com.essentia.metrics;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/12/15.
 */
public class Pace extends Metrics{
    public static final String NAME="Pace";
    private boolean isDisplayed;
    private String value;

    public Pace(){

    }
    public Pace(boolean isDisplayed, String value){
        this.isDisplayed = isDisplayed;
        this.value = value;
    }
    @Override
    public int getDrawable(){
        return isDisplayed?R.drawable.ic_pace:0;
    }

    @Override
    public int getDrawableOn() {
        return R.drawable.pace_on;
    }

    @Override
    public int getDrawableOff() {
        return R.drawable.pace_off;
    }

    @Override
    public int getDisplayTextOn() {
        return R.string.pace_on;
    }

    @Override
    public int getDisplayTextOff() {
        return R.string.pace_off;
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
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getUnit() {
        return "min/km";
    }

    @Override
    public String getDescription() {
        return "Pace";
    }
}
