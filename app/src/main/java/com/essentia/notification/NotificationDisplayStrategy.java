package com.essentia.notification;

import android.app.Notification;

/**
 * Created by kyawzinlatt94 on 2/22/15.
 */
public interface NotificationDisplayStrategy {
    void notify(int notificationId, Notification notification);

    void cancel(int notificationId);
}
