package com.essentia.tracker.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;

import com.essentia.workout.feedback.RUTextToSpeech;
import com.essentia.workout.workout_pojos.Workout;
import com.example.kyawzinlatt94.essentia.R;

import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/20/15.
 */
public class TrackerTTS extends DefaultTrackerComponent {

    private TextToSpeech tts;
    private Context context;
    private RUTextToSpeech ruTTS;

    public static final String NAME = "TTS";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ResultCode onInit(final Callback callback, final Context context) {
        this.context = context;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    callback.run(TrackerTTS.this, ResultCode.RESULT_OK);
                }
                else {
                    callback.run(TrackerTTS.this, ResultCode.RESULT_ERROR);
                }
            }
        });
        return ResultCode.RESULT_PENDING;
    }

    @Override
    public void onBind(HashMap<String, Object> bindValues) {
        Context ctx = (Context) bindValues.get(KEY_CONTEXT);
        Boolean mute = (Boolean) bindValues.get(Workout.KEY_MUTE);
        bindValues.put(Workout.KEY_TTS, new RUTextToSpeech(tts, mute, ctx));
    }

    @Override
    public ResultCode onEnd(Callback callback, Context context) {
        if (tts != null) {
            tts.shutdown();
            tts = null;
        }
        return ResultCode.RESULT_OK;
    }

    RUTextToSpeech getTTS(SharedPreferences prefs) {
        final String mute = prefs.getString(context.getString(R.string.pref_mute), "no");
        ruTTS = new RUTextToSpeech(tts, mute, context);
        return ruTTS;
    }
}
