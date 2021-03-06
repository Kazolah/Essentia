package com.essentia.support;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/6/15.
 */

public enum Scope {

    WORKOUT(1, R.string.cue_workout),
    STEP(2, R.string.cue_activity),
    LAP(3, R.string.cue_lap),
    CURRENT(4, R.string.cue_current);

    int value = 0;
    int cueId = 0;

    private Scope(int val, int cueId) {
        this.value = val;
        this.cueId = cueId;
    }

    /**
     * @return the scopeValue
     */
    public int getValue() {
        return value;
    }

    public boolean equal(Scope what) {
        if (what == null || what.value != this.value)
            return false;
        return true;
    }

    public int getCueId() {
        return cueId;
    }
}
