package com.essentia.notification;

import android.app.Notification;
import android.app.NotificationManager;

/**
 * Created by kyawzinlatt94 on 2/22/15.
 */
public class NotificationManagerDisplayStrategy implements NotificationDisplayStrategy {
    private final NotificationManager notificationManager;

    public NotificationManagerDisplayStrategy(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @Override
    public void notify(int notificationId, Notification notification) {
        notificationManager.notify(notificationId, notification);
    }

    @Override
    public void cancel(int notificationId) {
        notificationManager.cancel(notificationId);
    }
}
