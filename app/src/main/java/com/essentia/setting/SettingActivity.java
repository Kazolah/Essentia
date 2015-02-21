package com.essentia.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.essentia.account.UserProfileActivity;
import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingActivity extends ActionBarActivity{
    private ListView setupListView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);
        setTitle(getString((R.string.title_setting)));
        ListItems listItems[] = new ListItems[]{
                new ListItems(getString(R.string.title_profile),
                        getString(R.string.description_profile)),
                new ListItems(getString(R.string.title_heart_rate_monitor),
                        getString(R.string.description_heart_rate_monitor)),
                new ListItems(getString(R.string.title_workout_preferences),
                        getString(R.string.description_workout_preferences)),
                new ListItems(getString(R.string.title_audio_preferences),
                        getString(R.string.description_audio_preferences)),
                new ListItems(getString(R.string.title_notification_settings),
                        getString(R.string.description_notification_settings)),
                new ListItems(getString(R.string.title_about),
                        getString(R.string.description_about))
        };
        list = populateList(listItems);
        adapter = new SimpleAdapter(this, list,
                R.layout.custom_setting_row_layout, new String[]{"Title","Description"},
                new int[]{R.id.txtCustomRowSettingTitle, R.id.txtCustomRowSettingDescription});
        setupListView = (ListView)findViewById(R.id.setting_listview);
        setupListView.setAdapter(adapter);
        setupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectItem(int position) {
        switch(position){
            case 0:
                finish();
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case 1:
                finish();
                startActivity(new Intent(this,HRMonitorSettingActivity.class ));
                break;
        }
    }
    private ArrayList<HashMap<String, Object>> populateList(ListItems[] listItems){
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for(ListItems listItem:listItems){
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("Title", listItem.title);
            temp.put("Description", listItem.description);
            list.add(temp);
        }
        return list;
    }

    /**
     * Private Classes
     */
    private class ListItems {
        public String title;
        public String description;
        public ListItems(String title, String description){
            this.title = title;
            this.description = description;
        }
    }
}
