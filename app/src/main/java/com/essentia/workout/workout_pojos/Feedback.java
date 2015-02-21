package com.essentia.workout.workout_pojos;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public abstract class Feedback {

    public void onInit(Workout s) {
    }

    public void onBind(Workout s, HashMap<String, Object> bindValues) {
    }

    public void onStart(Workout s) {
    }

    public void onEnd(Workout s) {
    }

    /**
     * compare feedback to another feedback so that same information isn't
     * emitted twice (or more) during one tick i.e different triggers can have
     * same feedback
     *
     * @param other
     */
    public abstract boolean equals(Feedback other);

    /**
     * Emit the feedback
     */
    public abstract void emit(Workout s, Context ctx);
}
