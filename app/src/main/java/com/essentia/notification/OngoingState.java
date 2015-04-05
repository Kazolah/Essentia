package com.essentia.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.essentia.support.Constants;
import com.essentia.util.Formatter;
import com.essentia.workout.WorkoutActivity;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 3/4/15.
 */
public class OngoingState implements NotificationState {
    private final Formatter formatter;
    private final Workout workoutInfo;
    private final Context context;
    private final NotificationCompat.Builder builder;

    public OngoingState(Formatter formatter, Workout workoutInfo, Context context) {
        this.formatter = formatter;
        this.workoutInfo = workoutInfo;
        this.context = context;

        builder = new NotificationCompat.Builder(context);
        Intent i = new Intent(context, WorkoutActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra(Constants.Intents.FROM_NOTIFICATION, true);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

        builder.setTicker(context.getString(R.string.essentia_started));
        builder.setContentIntent(pi);
        builder.setContentTitle(context.getString(R.string.activity_ongoing));
        builder.setSmallIcon(R.drawable.workout_icon);
        builder.setOngoing(true);
        builder.setOnlyAlertOnce(true);
        builder.setLocalOnly(true);
    }

    @Override
    public Notification createNotification() {
        String distance = workoutInfo.getMetrics("Distance");
        String time = workoutInfo.getMetrics("Duration");
        String pace = workoutInfo.getMetrics("Pace");

        String content = String.format("%s: %s %s: %s %s: %s",
                context.getString(R.string.distance), distance,
                context.getString(R.string.time), time,
                context.getString(R.string.pace), pace);
        builder.setContentText(content);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(builder);
        bigTextStyle.setBigContentTitle(context.getString(R.string.activity_ongoing));
        bigTextStyle.bigText(String.format("%s: %s,\n%s: %s\n%s: %s",
                context.getString(R.string.distance), distance,
                context.getString(R.string.time), time,
                context.getString(R.string.pace), pace));
        builder.setStyle(bigTextStyle);

        return builder.build();
    }
}