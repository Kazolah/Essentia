package com.essentia.workout.workout_pojos;

import android.location.Location;

import com.essentia.support.Scope;

/**
 * Created by kyawzinlatt94 on 2/18/15.
 * Acknowledgement niklas.weidemann, RunnerUp
 */
public interface WorkoutInfo {
    double get(Scope scope, Dimension d);

    double getDistance(Scope scope);

    double getTime(Scope scope);

    double getSpeed(Scope scope);

    double getPace(Scope scope);

    double getDuration(Scope scope, Dimension dimension);

    double getRemaining(Scope scope, Dimension dimension);

    double getHeartRate(Scope scope);

    double getHeartRateZone(Scope scope);

    int getSport();

    /* TODO make better/more elaborate state visible... */
    boolean isPaused();

    boolean isEnabled(Dimension dim, Scope scope);

    Location getLastKnownLocation();
}
