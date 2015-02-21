package com.essentia.metrics;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/12/15.
 */
public class Speed extends Metrics{
    public static final String NAME="Speed";
    private boolean isDisplayed = false;
    private String value;

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
        return "kmh";
    }

    @Override
    public String getDescription() {
        return "Speed";
    }

    @Override
    public int getDrawable(){
        return isDisplayed?R.drawable.ic_speed:0;
    }

    @Override
    public int getDrawableOn() {
        return R.drawable.speed_on;
    }

    @Override
    public int getDrawableOff() {
        return R.drawable.speed_off;
    }

    @Override
    public int getDisplayTextOn() {
        return R.string.speed_on;
    }

    @Override
    public int getDisplayTextOff() {
        return R.string.speed_off;
    }
}
