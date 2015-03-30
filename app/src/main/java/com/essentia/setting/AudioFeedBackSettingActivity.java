package com.essentia.setting;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 3/16/15.
 */
public class AudioFeedBackSettingActivity extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.audio_feedback_settings);
        setContentView(R.layout.settings_wrapper);
    }
}
