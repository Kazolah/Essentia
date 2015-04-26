package com.essentia.util;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 4/21/15.
 */
public class CustomTutorialDialog extends DialogFragment {

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Tutorial");
        View rootView = inflater.inflate(R.layout.custom_tutorial_dialog, container, false);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.tutorial_view_pager);
        Button btnClose = (Button) rootView.findViewById(R.id.ctd_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = new Fragment();
            switch(position){
                case 0:
                    fragment = new Tab1Fragment();
                    break;
                case 1:
                    fragment = new Tab2Fragment();
                    break;
                case 2:
                    fragment = new Tab3Fragment();
                    break;
                case 3:
                    fragment = new Tab4Fragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 4;
        }

    }

    public static class Tab1Fragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tutorial_tabs, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.ftt_iv1);
            imageView.setImageResource(R.drawable.navigation_tutorial);
            return rootView;
        }
    }
    public static class Tab2Fragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tutorial_tabs, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.ftt_iv1);
            imageView.setImageResource(R.drawable.setting_tutorial);
            return rootView;
        }
    }
    public static class Tab3Fragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tutorial_tabs, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.ftt_iv1);
            imageView.setImageResource(R.drawable.icons_tutorial);
            return rootView;
        }
    }
    public static class Tab4Fragment extends Fragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tutorial_tabs, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.ftt_iv1);
            imageView.setImageResource(R.drawable.activity_tutorial);
            return rootView;
        }
    }
}
