package com.essentia.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 3/23/15.
 */
public class StatisticsCustomAdapter extends ArrayAdapter {

    private static LayoutInflater inflater;
    private Context context;
    private int layoutResourceId;
    private ArrayList<StatisticListItem> data = null;

    public StatisticsCustomAdapter(Context context, int layoutResourceId, ArrayList<StatisticListItem> data){
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
            holder.month = (TextView)row.findViewById(R.id.csrl_tvMonth);
            holder.year = (TextView)row.findViewById(R.id.csrl_tvYear);
            holder.calorie = (TextView)row.findViewById(R.id.csrl_tvcalorie);
            holder.distance = (TextView)row.findViewById(R.id.csrl_tvdistance);
            holder.duration = (TextView)row.findViewById(R.id.csrl_tvduration);
            holder.sessions = (TextView)row.findViewById(R.id.csrl_tvsessions);
            holder.avgHR = (TextView) row.findViewById(R.id.csrl_tvhr);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        StatisticListItem listItem = data.get(position);
        String months[] = {"January", "February", "March", "April",
                "May", "June", "July", "August", "September",
                "October", "November", "December"};
        int index = Integer.valueOf(listItem.month);
        holder.month.setText(months[index-1]);
        holder.year.setText(listItem.year);
        holder.distance.setText(listItem.distance);
        holder.duration.setText(listItem.duration);
        holder.avgHR.setText(listItem.heartRate);
        holder.calorie.setText(listItem.calorie);
        holder.sessions.setText(listItem.sessions);
        return row;
    }

    public static class ViewHolder{
        public TextView month;
        public TextView year;
        public TextView calorie;
        public TextView distance;
        public TextView sessions;
        public TextView avgHR;
        public TextView duration;
    }
}
