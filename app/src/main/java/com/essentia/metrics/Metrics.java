package com.essentia.metrics;

import java.io.Serializable;

/**
 * Created by kyawzinlatt94 on 2/9/15.
 */
public abstract class Metrics implements Serializable{
    public abstract boolean getIsDisplayed();
    public abstract void setIsDisplayed(boolean isDisplayed);
    public abstract int getDrawable();
    public abstract int getDrawableOn();
    public abstract int getDrawableOff();
    public abstract int getDisplayTextOn();
    public abstract int getDisplayTextOff();
    public abstract String getValue();
    public abstract void setValue(String value);
    public abstract String getUnit();
    public abstract String getDescription();
}
