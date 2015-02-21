package com.essentia.workout.workout_pojos;

import com.essentia.support.Constants.DIMENSION;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */
public enum Dimension {
     TIME(DIMENSION.TIME, R.string.time),
    DISTANCE(DIMENSION.DISTANCE, R.string.distance),
    SPEED(DIMENSION.SPEED, R.string.speed),
    PACE(DIMENSION.PACE, R.string.pace),
    HR(DIMENSION.HR, R.string.heart_rate),
    HRZ(DIMENSION.HRZ, R.string.heart_rate_zone);

    public static final boolean SPEED_CUE_ENABLED = false;

    int value = 0;
    int textId = 0;

    private Dimension(int val, int textId) {
        this.value = val;
        this.textId = textId;
    }
    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    public int getTextId() {
        return textId;
    }

    public boolean equal(Dimension what) {
        if (what == null || what.value != this.value)
            return false;
        return true;
    }

    public static Dimension valueOf(int val) {
        switch(val) {
            case -1:
                return null;
            case DIMENSION.TIME:
                return TIME;
            case DIMENSION.DISTANCE:
                return DISTANCE;
            case DIMENSION.SPEED:
                return SPEED;
            case DIMENSION.PACE:
                return PACE;
            case DIMENSION.HR:
                return HR;
            case DIMENSION.HRZ:
                return HRZ;
            default:
                return null;
        }
    }
}
