package com.essentia.hrm;

import android.os.Handler;
import android.os.Looper;

import java.util.UUID;

/**
 * Created by kyawzinlatt94 on 2/15/15.
 * This property is referred from Runner Up OpenSource application.
 */
public abstract class BtHRBase implements HRProvider{
    static final UUID HRP_SERVICE = UUID
            .fromString("0000180D-0000-1000-8000-00805f9b34fb");
    static final UUID BATTERY_SERVICE = UUID
            .fromString("0000180f-0000-1000-8000-00805f9b34fb");
    static final UUID FIRMWARE_REVISON_UUID = UUID
            .fromString("00002a26-0000-1000-8000-00805f9b34fb");
    static final UUID DIS_UUID = UUID
            .fromString("0000180a-0000-1000-8000-00805f9b34fb");
    static final UUID HEART_RATE_MEASUREMENT_CHARAC = UUID
            .fromString("00002A37-0000-1000-8000-00805f9b34fb");
    static final UUID BATTERY_LEVEL_CHARAC = UUID
            .fromString("00002A19-0000-1000-8000-00805f9b34fb");
    static final UUID CCC = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");

    protected HRProvider.HRClient hrClient;
    protected Handler hrClientHandler;

    protected void log (final String msg) {
        if (hrClient != null) {
            if(Looper.myLooper() == Looper.getMainLooper()) {
                hrClient.log(this, msg);
            } else {
                hrClientHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (hrClient != null)
                            hrClient.log(BtHRBase.this, msg);
                    }
                });
            }
        }
        else
            System.err.println(msg);
    }
}
