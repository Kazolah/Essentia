package com.essentia.summary;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.essentia.util.Route;
import com.example.kyawzinlatt94.essentia.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by kyawzinlatt94 on 3/6/15.
 */
public class WorkoutSummaryMapFragment extends Fragment{
    private WorkoutSummaryActivity summaryActivity;
    private GoogleMap map = null;
    private LatLngBounds mapBounds = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_summary_map, container, false);
        map = ((SupportMapFragment)getFragmentManager().findFragmentById(R.id.fwsm_map)).getMap();
        if (map != null) {
            map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {
                    if (mapBounds != null) {
                        // Move camera.
                        map.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBounds, 5));
                        // Remove listener to prevent position reset on camera
                        // move.
                        map.setOnCameraChangeListener(null);
                    }
                }
            });
        }
        summaryActivity.loadRoute();

        return rootView;
    }
    public GoogleMap getMap(){
        return map;
    }
    public void setRoute(Route route){
        if (map != null) {
            map.addPolyline(route.path);
            mapBounds = route.bounds.build();
            System.err.println("Added polyline");
            int cnt = 0;
            for (MarkerOptions m : route.markers) {
                cnt++;
                map.addMarker(m);
            }
            System.err.println("Added " + cnt + " markers");
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        summaryActivity = (WorkoutSummaryActivity) activity;
    }
}
