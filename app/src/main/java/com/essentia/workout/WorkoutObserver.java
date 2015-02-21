package com.essentia.workout;

import com.essentia.workout.workout_pojos.WorkoutInfo;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 */
public interface WorkoutObserver {
    public void workoutEvent(WorkoutInfo workoutInfo, int type);
}
