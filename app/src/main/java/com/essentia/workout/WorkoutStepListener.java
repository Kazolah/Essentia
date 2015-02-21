package com.essentia.workout;

import com.essentia.workout.workout_pojos.Step;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */
public interface WorkoutStepListener {
    public void onStepChanged(Step oldStep, Step newStep);
}
