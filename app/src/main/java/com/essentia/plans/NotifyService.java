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
    private NotificationManager mNM;
    private NotificationCompat.Builder builder;
    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }
    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId){
        super.onStart(intent, startId);
        mNM = (NotificationManager)this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent i = new Intent(this.getApplicationContext(), MainActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(Constants.Intents.FROM_NOTIFICATION, true);
        PendingIntent pi = PendingIntent.getActivity(this.getApplicationContext(), 0, i, 0);

        builder = new NotificationCompat.Builder(this.getApplicationContext());
        builder.setContentIntent(pi);
        builder.setContentTitle("Reminder");
        builder.setSmallIcon(R.drawable.workout_icon);
        builder.setOnlyAlertOnce(true);
        builder.setLocalOnly(true);
        builder.setContentText("Don't forget to workout");
        mNM.notify(0, builder.build());
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
