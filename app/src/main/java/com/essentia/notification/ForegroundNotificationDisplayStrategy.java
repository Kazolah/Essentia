package com.essentia.notification;

import android.app.Notification;
import android.app.Service;

/**
 * Created by kyawzinlatt94 on 2/22/15.
 */
public class ForegroundNotificationDisplayStrategy implements NotificationDisplayStrategy {
    private final Service service;

    public ForegroundNotificationDisplayStrategy(Service service) {
        this.service = service;
    }

    @Override
    public void notify(int notificationId, Notification notification) {
        service.startForeground(notificationId, notification);
    }

    @Override
    public void cancel(int notificationId) {
        service.stopForeground(true);
    }
}
