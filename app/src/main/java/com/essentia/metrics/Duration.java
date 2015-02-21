package com.essentia.metrics;

import com.example.kyawzinlatt94.essentia.R;

import java.io.Serializable;

/**
 * Created by kyawzinlatt94 on 2/11/15.
 */
public class Duration extends Metrics implements Serializable{
    public static final String NAME="Duration";
    private String value;
    private boolean isDisplayed;

    public Duration(boolean isDisplayed, String value){
        this.isDisplayed = isDisplayed;
        this.value = value;
    }
    @Override
    public int getDrawable(){
        return isDisplayed?R.drawable.ic_duration:0;
    }

    @Override
    public int getDrawableOn() {
        return R.drawable.duration_on;
    }

    @Override
    public int getDrawableOff() {
        return R.drawable.duration;
    }

    @Override
    public int getDisplayTextOn() {
        return R.string.duration_on;
    }

    @Override
    public int getDisplayTextOff() {
        return R.string.duration_off;
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
}
