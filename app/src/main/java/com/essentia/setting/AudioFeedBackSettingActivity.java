package com.essentia.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.speech.tts.TextToSpeech;

import com.example.kyawzinlatt94.essentia.R;

import java.util.Locale;

/**
 * Created by kyawzinlatt94 on 3/16/15.
 */
public class AudioFeedBackSettingActivity extends PreferenceActivity {
    TextToSpeech tts;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.audio_feedback_settings);
        setContentView(R.layout.settings_wrapper);
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        {
            Preference btn = (Preference) findPreference("test_cueinfo");
            btn.setOnPreferenceClickListener(onTestAudioClick);
        }
    }
    final Preference.OnPreferenceClickListener onTestAudioClick = new Preference.OnPreferenceClickListener(){

        @Override
        public boolean onPreferenceClick(Preference arg0) {
            tts.speak(getString(R.string.test_audio_speech).toString(),TextToSpeech.QUEUE_ADD, null);
            return true;
        }
    };
}
