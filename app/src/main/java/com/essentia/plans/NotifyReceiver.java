package com.essentia.plans;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.essentia.main.MainActivity;
import com.essentia.support.Constants;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 4/2/15.
 */
public class NotifyReceiver extends BroadcastReceiver {
    NotificationCompat.Builder builder;
    NotificationManager mNM;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        mNM = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra(Constants.Intents.FROM_NOTIFICATION, true);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

        builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pi);
        builder.setContentTitle("Reminder");
        builder.setSmallIcon(R.drawable.workout_icon);
        builder.setOnlyAlertOnce(true);
        builder.setLocalOnly(true);
        builder.setContentText("Don't forget to workout");
        builder.setAutoCancel(true);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        mNM.notify(0, builder.build());
    }
}
