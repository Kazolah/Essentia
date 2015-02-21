package com.essentia.tracker;

/**
 * Created by kyawzinlatt94 on 2/21/15.
 */
public interface GpsInformation {
    String getGpsAccuracy();
    int getSatellitesAvailable();
    int getSatellitesFixed();
}
