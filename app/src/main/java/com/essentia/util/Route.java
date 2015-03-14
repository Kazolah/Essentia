package com.essentia.util;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 3/8/15.
 */
public class Route {
    public final PolylineOptions path = new PolylineOptions();
    public final LatLngBounds.Builder bounds = new LatLngBounds.Builder();
    public final ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>(10);

    public Route() {
        path.color(Color.RED);
        path.width(5);
    }
}
