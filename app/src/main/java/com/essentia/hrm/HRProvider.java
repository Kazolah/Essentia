package com.essentia.hrm;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by kyawzinlatt94 on 2/15/15.
 */
public interface HRProvider {

    public interface HRClient {
        public void onOpenResult(boolean ok);

        public void onScanResult(HRDeviceReference device);

        public void onConnectResult(boolean connectOK);

        public void onDisconnectResult(boolean disconnectOK);

        public void onCloseResult(boolean closeOK);

        public void log(HRProvider src, String msg);
    }

    public abstract String getName(); // For display

    public abstract String getProviderName(); // For internal usage

    public abstract boolean isEnabled();

    public abstract boolean startEnableIntent(Activity activity, int requestCode);

    public abstract void open(Handler handler, HRClient hrClient);

    public abstract void close();

    public abstract boolean isBondingDevice();

    public abstract boolean isScanning();

    public abstract boolean isConnected();

    public abstract boolean isConnecting();

    public abstract void startScan();

    public abstract void stopScan();

    public abstract void connect(HRDeviceReference ref);

    public abstract void disconnect();

    public abstract int getHRValue();

    public abstract long getHRValueTimestamp();

    public abstract int getBatteryLevel();
}
