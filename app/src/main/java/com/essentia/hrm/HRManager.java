package com.essentia.hrm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyawzinlatt94 on 2/15/15.
 */
public class HRManager {
    public static HRProvider getHRProvider(Context ctx, String src) {
        HRProvider provider = getHRProviderImpl(ctx, src);
        if (provider != null) {
            return new RetryingHRProviderProxy(provider);
        }
        return provider;
    }

    private static HRProvider getHRProviderImpl(Context ctx, String src) {
        System.err.println("getHRProvider(" + src + ")");

        if (src.contentEquals(AndroidBLEHRProvider.NAME)) {
            if (!AndroidBLEHRProvider.checkLibrary(ctx))
                return null;
            return new AndroidBLEHRProvider(ctx);
        }
        if (src.contentEquals(Bt20Base.ZephyrHRM.NAME)) {
            if (!Bt20Base.checkLibrary(ctx))
                return null;
            return new Bt20Base.ZephyrHRM(ctx);
        }
        if (src.contentEquals(Bt20Base.PolarHRM.NAME)) {
            if (!Bt20Base.checkLibrary(ctx))
                return null;
            return new Bt20Base.PolarHRM(ctx);
        }

        if (src.contentEquals(Bt20Base.StHRMv1.NAME)) {
            if (!Bt20Base.checkLibrary(ctx))
                return null;
            return new Bt20Base.StHRMv1(ctx);
        }

        if (src.contentEquals(MockHRProvider.NAME)) {
            return new MockHRProvider(ctx);
        }

        return null;
    }

    public static List<HRProvider> getHRProviderList(Context ctx) {
        Resources res = ctx.getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean experimental = prefs
                .getBoolean(res.getString(R.string.pref_bt_experimental), false);
        boolean mock = prefs.getBoolean(res.getString(R.string.pref_bt_mock), false);

        if (experimental) {
            /* dummy if to remove warning on experimental */
        }

        List<HRProvider> providers = new ArrayList<HRProvider>();
//
        if (AndroidBLEHRProvider.checkLibrary(ctx)) {
            providers.add(new AndroidBLEHRProvider(ctx));
        }

        if (experimental && Bt20Base.checkLibrary(ctx)) {
            providers.add(new Bt20Base.StHRMv1(ctx));
        }

        return providers;
    }
}
