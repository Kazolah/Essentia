package com.essentia.summary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by kyawzinlatt94 on 3/15/15.
 */
public class CustomMapFragment extends SupportMapFragment{

    public CustomMapFragment() {
        super();

    }

    public static CustomMapFragment newInstance() {
        CustomMapFragment fragment = new CustomMapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        View v = super.onCreateView(arg0, arg1, arg2);
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof OnMapReadyListener) {
            ((OnMapReadyListener) fragment).onMapReady();
        }
        return v;
    }



    /**
     * Listener interface to tell when the map is ready
     */
    public static interface OnMapReadyListener {

        void onMapReady();
    }
}
