package com.essentia.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.essentia.main.MainActivity;
import com.essentia.support.Constants;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/22/15.
 */
public class GpsBoundState implements NotificationState {
    private final Notification notification;

    public GpsBoundState(Context context) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra(Constants.Intents.FROM_NOTIFICATION, true);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

        builder.setContentIntent(pi);
        builder.setContentTitle(context.getString(R.string.activity_ready));
        builder.setContentText(context.getString(R.string.ready_to_start_running));
        builder.setSmallIcon(R.drawable.workout_icon);
        builder.setOnlyAlertOnce(true);
        builder.setLocalOnly(true);

        notification = builder.build();
    }

    @Override
    public Notification createNotification() {
        return notification;
    }
}