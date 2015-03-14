package com.essentia.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 3/13/15.
 */
public class WidgetUtil {

    public static void setEditable(EditText editText, boolean onoff) {
        if (onoff == true) {
            editText.setClickable(onoff);
            editText.setFocusable(onoff);
            editText.setFocusableInTouchMode(onoff);
        } else {
            editText.setClickable(onoff);
            editText.setFocusable(onoff);
        }
    }

    public static View createHoloTabIndicator(Context ctx, String title) {
        Resources res = ctx.getResources(); // Resource object to get Drawables
        TextView txtTab = new TextView(ctx);
        txtTab.setText(title);
        txtTab.setTextColor(Color.WHITE);
        txtTab.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL);
        Drawable drawable = res.getDrawable(R.drawable.tab_indicator_holo);
        WidgetUtil.setBackground(txtTab, drawable); // R.drawable.tab_indicator_holo);

        int h = (25 * drawable.getIntrinsicHeight()) / 10;
        txtTab.setPadding(0, h, 0, h);
        // txtTab.setHeight(1 + 10 * drawable.getIntrinsicHeight());
        // txtTab.setLineSpacing(1 + 5 * drawable.getIntrinsicHeight(), 1);
        return txtTab;
    }

    @SuppressWarnings("deprecation")
    public static void setBackground(View v, Drawable d) {
        if (Build.VERSION.SDK_INT < 16) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    public static void addLegacyOverflowButton(Window window) {
        if (window.peekDecorView() == null) {
            return;
        }

        try {
            window.addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
        } catch (NoSuchFieldException e) {
            // Ignore since this field won't exist in most versions of Android
        } catch (IllegalAccessException e) {
        }
    }
}
