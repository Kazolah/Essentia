package com.essentia.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.essentia.main.MainActivity;
import com.essentia.support.Constants;
import com.essentia.tracker.GpsInformation;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/22/15.
 */
public class GpsSearchingState implements NotificationState {
    private final Context context;
    private final GpsInformation gpsInformation;
    private final NotificationCompat.Builder builder;

    public GpsSearchingState(Context context, GpsInformation gpsInformation) {
        this.context = context;
        this.gpsInformation = gpsInformation;

        builder = new NotificationCompat.Builder(context);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra(Constants.Intents.FROM_NOTIFICATION, true);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pi);
        builder.setContentTitle(context.getString(R.string.searching_for_gps));
        builder.setSmallIcon(R.drawable.workout_icon);
        builder.setOnlyAlertOnce(true);
        builder.setLocalOnly(true);
    }

    @Override
    public Notification createNotification() {
        builder.setContentText(String.format("%s: %d/%d%s",
                context.getString(R.string.gps_satellites),
                gpsInformation.getSatellitesFixed(), gpsInformation.getSatellitesAvailable(),
                gpsInformation.getGpsAccuracy()));

        return builder.build();
    }
}