package com.essentia.metrics;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/12/15.
 */
public class Calorie extends Metrics{
    public static final String NAME="Calorie";
    private String value;
    private boolean isDisplayed;

    public Calorie(){

    }
    public Calorie(boolean isDisplayed, String value){
            this.isDisplayed = isDisplayed;
            this.value = value;
    }
    @Override
    public int getDrawable(){
        return isDisplayed?R.drawable.ic_calorie:0;
    }

    @Override
    public int getDrawableOn(){
        return R.drawable.calorie_on;
    }
    @Override
    public int getDrawableOff(){
        return R.drawable.calorie_off;
    }

    @Override
    public int getDisplayTextOn() {
        return R.string.calorie_on;
    }

    @Override
    public int getDisplayTextOff() {
        return R.string.calorie_off;
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
        return "kcal";
    }

    @Override
    public String getDescription() {
        return "Calorie";
    }
}
