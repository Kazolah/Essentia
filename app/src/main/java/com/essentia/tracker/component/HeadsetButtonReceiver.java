package com.essentia.tracker.component;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

import com.essentia.support.Constants;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public class HeadsetButtonReceiver extends BroadcastReceiver {

    public static void registerHeadsetListener(Context context) {
        registerHeadsetListener(context, HeadsetButtonReceiver.class);
    }

    protected static void registerHeadsetListener(Context context,
                                                  Class<? extends BroadcastReceiver> class_) {
        ComponentName mMediaReceiverCompName = new ComponentName(
                context.getPackageName(), class_.getName());
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.registerMediaButtonEventReceiver(mMediaReceiverCompName);
    }

    public static void unregisterHeadsetListener(Context context) {
        unregisterHeadsetListener(context, HeadsetButtonReceiver.class);
    }

    protected static void unregisterHeadsetListener(Context context,
                                                    Class<? extends BroadcastReceiver> class_) {
        ComponentName mMediaReceiverCompName = new ComponentName(
                context.getPackageName(), class_.getName());
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.unregisterMediaButtonEventReceiver(mMediaReceiverCompName);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent) intent
                    .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.ACTION_DOWN == event.getAction()) {
                Intent startBroadcastIntent = new Intent();
                startBroadcastIntent.setAction(Constants.Intents.PAUSE_RESUME);
                context.sendBroadcast(startBroadcastIntent);
            }
        }
    }

    public static boolean getAllowStartStopFromHeadsetKey(Context ctx) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        return pref.getBoolean(ctx.getString(R.string.pref_keystartstop_active), true);
    }
}