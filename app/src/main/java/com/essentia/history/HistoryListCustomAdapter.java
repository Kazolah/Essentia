package com.essentia.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.essentia.support.WorkoutActivity;
import com.essentia.util.Formatter;
import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 2/12/15.
 */
public class HistoryListCustomAdapter extends ArrayAdapter {

    private static LayoutInflater inflater;
    private Context context;
    private int layoutResourceId;
    private ArrayList<WorkoutActivity> data = null;

    public HistoryListCustomAdapter(Context context, int layoutResourceId, ArrayList<WorkoutActivity> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    //For each row, inflate data
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.date = (TextView)row.findViewById(R.id.chrl_tvDate);
            holder.metrics = (TextView)row.findViewById(R.id.chrl_tvDistance);
            holder.time = (TextView)row.findViewById(R.id.chrl_tvDuration);
            holder.icon = (ImageView)row.findViewById(R.id.chrl_ivActivity);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        WorkoutActivity listItem = data.get(position);
        holder.date.setText(Formatter.parseDate(listItem.getDate()));

        double distance = Double.parseDouble(listItem.getDistance());
        int calorie = Integer.parseInt(listItem.getCalorie());
        String metrics = "";
        metrics = (distance==0 && calorie!=0)?distance + " km" : calorie + " kcal";
        holder.metrics.setText(metrics);

        holder.time.setText(listItem.getDuration());
        holder.icon.setImageResource(getSportType(listItem.getSport()));
        return row;
    }

    public static class ViewHolder{
        public ImageView icon;
        public TextView date;
        public TextView metrics;
        public TextView time;
    }
    private int getSportType(String type){
        switch (type){
            case "Cycling":
                return R.drawable.cycling;
            case "Running":
                return R.drawable.running;
            case "Running, treadmill":
                return R.drawable.running_treadmill;
            case "Walking":
                return R.drawable.walking;
            case "Walking, treadmill":
                return R.drawable.walking_treadmill;
            case "Other":
                return R.drawable.other;
            case "Other, indoor":
                return R.drawable.other_indoor;
            default:
                return R.drawable.running;
        }
    }
}
