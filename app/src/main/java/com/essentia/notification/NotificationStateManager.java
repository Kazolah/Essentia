package com.essentia.notification;

import android.app.Notification;

/**
 * Created by kyawzinlatt94 on 2/22/15.
 */
public class NotificationStateManager { private static final int NOTIFICATION_ID = 1;
    private final NotificationDisplayStrategy strategy;

    public NotificationStateManager(NotificationDisplayStrategy strategy) {
        this.strategy = strategy;
    }

    public void displayNotificationState(NotificationState state) {
        if (state == null) throw new IllegalArgumentException("state is null");

        Notification notification = state.createNotification();
        strategy.notify(NOTIFICATION_ID, notification);
    }

    public void cancelNotification() {
        strategy.cancel(NOTIFICATION_ID);
    }
}
