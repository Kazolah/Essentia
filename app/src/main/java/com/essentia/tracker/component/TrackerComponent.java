package com.essentia.tracker.component;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/15/15.
 */
public interface TrackerComponent {
    public static final String KEY_CONTEXT = "KEY_CONTEXT";

    public enum ResultCode{
        RESULT_OK,
        RESULT_UNKNOWN,
        RESULT_NOT_SUPPORTED,
        RESULT_NOT_ENABLED,
        RESULT_ERROR,
        RESULT_ERROR_FATAL,
        RESULT_PENDING
    }

    public interface Callback{
        void run(TrackerComponent component, ResultCode resultCode);
    }

    public String getName();

    /**
     * Called by Tracker during initialization
     */
    public ResultCode onInit(Callback callback, Context context);

    /**
     * Called by Tracker when connecting
     */
    public ResultCode onConnecting(Callback callback, Context context);

    boolean isConnected();

    public void onConnected();

    public void onStart();

    public void onPause();

    public void onResume();

    public void onComplete(boolean discarded);

    /**
     * Called by Tracker before component populate bindValues with objects
     * that will then be passed to workout
     */
    public void onBind(HashMap<String, Object> bindValues);

    /**
     * Called by Tracker when shutting down
     */
    public ResultCode onEnd(Callback callback, Context context);

}
