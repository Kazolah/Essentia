package com.essentia.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * Created by kyawzinlatt94 on 3/16/15.
 */
@TargetApi(Build.VERSION_CODES.FROYO)
public class TextPreference extends EditTextPreference{
    public TextPreference(Context context) {
        super(context);
    }

    public TextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
                                     Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        super.setSummary(super.getPersistedString(""));
    }

    @Override
    protected void onDialogClosed(boolean ok) {
        super.onDialogClosed(ok);
        if (ok) {
            super.setSummary(super.getPersistedString(""));
        }
    }
}
