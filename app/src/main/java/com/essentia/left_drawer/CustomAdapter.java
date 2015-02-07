package com.essentia.left_drawer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class CustomAdapter extends ArrayAdapter<NavigationListItems>{

    private static LayoutInflater inflater;
    private Context context;
    private int layoutResourceId;
    private Fragment fragment;
    private NavigationListItems data[] = null;

    public CustomAdapter(Context context, int layoutResourceId, NavigationListItems[] data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
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
            holder.layout = (RelativeLayout)row.findViewById(R.id.bgDrawerItem);
            holder.image = (ImageView)row.findViewById(R.id.imgDrawerItem);
            holder.text = (TextView)row.findViewById(R.id.txtDrawerItem);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        NavigationListItems listItem = data[position];
        holder.text.setText(listItem.title);
        holder.image.setImageResource(listItem.icon);

        if(position==0){
           holder.layout.setBackgroundResource(R.drawable.drawer_header);
           holder.text.setTextColor(Color.WHITE);
           holder.image.setPadding(10,10,10,10);
        }

        return row;
    }

    public static class ViewHolder{
        public ImageView image;
        public TextView text;
        public RelativeLayout layout;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;
        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface CustomListItemCallBack{
        void onItemClick(int position);
    }
}
