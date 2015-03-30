package com.essentia.plans;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.essentia.main.MainActivity;
import com.essentia.support.Constants;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 3/24/15.
 */
public class NotifyService extends Service{
    private NotificationCompat.Builder builder;
    @Override
    public void onCreate(){
        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra(Constants.Intents.FROM_NOTIFICATION, true);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pi);
        builder.setContentTitle("Reminder");
        builder.setSmallIcon(R.drawable.workout_icon);
        builder.setOnlyAlertOnce(true);
        builder.setLocalOnly(true);
        builder.setContentText("Don't forget to workout");
        mNM.notify(1, builder.build());
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
