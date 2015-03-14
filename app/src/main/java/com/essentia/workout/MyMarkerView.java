package com.essentia.workout;

import android.content.Context;
import android.widget.TextView;

import com.example.kyawzinlatt94.essentia.R;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MarkerView;

/**
 * Created by kyawzinlatt94 on 3/7/15.
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private String txtMarker = "";

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvMarkerContent);
    }
    public void setMarkerResource(String data){
        txtMarker = data;
    }
    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
//            "" + Utils.formatNumber(ce.getHigh(), 0, true)
            tvContent.setText(txtMarker);
        } else {
//            "" + Utils.formatNumber(e.getVal(), 0, true)
            tvContent.setText(txtMarker);
        }
    }

    @Override
    public float getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public float getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}