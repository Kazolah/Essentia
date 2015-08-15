package com.essentia.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essentia.main.TargetZoneFragment.TargetZoneListItems;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 4/4/15.
 *
 * This class is customized list for TargetZoneFragment
 */
public class TargetListCustomAdapter extends ArrayAdapter<TargetZoneListItems> {

    private static LayoutInflater inflater;
    private Context context;
    private int layoutResourceId;
    private Fragment fragment;
    private TargetZoneListItems data[] = null;

    public TargetListCustomAdapter(Context context, int layoutResourceId, TargetZoneListItems[] data){
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
            holder.layout = (RelativeLayout)row.findViewById(R.id.bgTargetItem);
            holder.image = (ImageView)row.findViewById(R.id.imgTargetItem);
            holder.text = (TextView)row.findViewById(R.id.txtTargetZoneName);
            holder.benefits = (TextView)row.findViewById(R.id.txtTargetZoneBenefit);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        TargetZoneListItems listItem = data[position];
        holder.text.setText(listItem.title);
        holder.image.setImageResource(listItem.icon);
        holder.benefits.setText(listItem.benefits);
        return row;
    }

    public static class ViewHolder{
        public ImageView image;
        public TextView text;
        public RelativeLayout layout;
        public TextView benefits;
    }
}
