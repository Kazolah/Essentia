package com.essentia.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.essentia.left_drawer.NavigationListItems;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/10/15.
 */
public class MainFragmentCustomAdapter  extends ArrayAdapter<MainFragmentListItems> {

    private static LayoutInflater inflater;
    private Context context;
    private int layoutResourceId;
    private Fragment fragment;
    private MainFragmentListItems data[] = null;

    public MainFragmentCustomAdapter(Context context, int layoutResourceId, MainFragmentListItems[] data){
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
            holder.image1 = (ImageView)row.findViewById(R.id.cmfrl_iv1);
            holder.image2 = (ImageView)row.findViewById(R.id.cmfrl_iv2);
            holder.image3 = (ImageView)row.findViewById(R.id.cmfrl_iv3);
            holder.image4 = (ImageView)row.findViewById(R.id.cmfrl_iv4);
            holder.image5 = (ImageView)row.findViewById(R.id.cmfrl_iv5);
            holder.description = (TextView)row.findViewById(R.id.cmfrl_tv_description);
            holder.selectedDescription = (TextView)row.findViewById(R.id.cmfrl_tv_desc_selected);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        MainFragmentListItems listItem = data[position];
        if(listItem.icon1!=0){ holder.image1.setImageResource(listItem.icon1); }
        if(listItem.icon2!=0){ holder.image2.setImageResource(listItem.icon2); }
        if(listItem.icon3!=0){ holder.image3.setImageResource(listItem.icon3); }
        if(listItem.icon4!=0){ holder.image4.setImageResource(listItem.icon4); }
        if(listItem.icon5!=0){ holder.image5.setImageResource(listItem.icon5); }
        holder.description.setText(listItem.description);
        holder.selectedDescription.setText(listItem.selectedDescription);

        return row;
    }
    private static class ViewHolder{
        public ImageView image1, image2, image3, image4, image5;
        public TextView description, selectedDescription;
    }
    public interface MainFragmentItemListCallBack{
        void onItemClick(int position);
    }
}
