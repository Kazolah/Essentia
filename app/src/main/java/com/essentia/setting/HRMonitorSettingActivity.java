package com.essentia.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.essentia.hrm.HRDeviceReference;
import com.essentia.hrm.HRManager;
import com.essentia.hrm.HRProvider;
import com.essentia.hrm.HRProvider.HRClient;
import com.essentia.tracker.component.TrackerHRM;
import com.example.kyawzinlatt94.essentia.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kyawzinlatt94 on 2/15/15.
 * This property is referred from Runner Up OpenSource Application.
 * Acknowledgement: jonas.oreland@gmail.com
 */
public class HRMonitorSettingActivity extends Activity implements HRClient{
    private final Handler handler = new Handler();

    private List<HRProvider> providers;
    private String btDeviceName;
    private String btAddress;
    private String btProviderName;
    private HRProvider hrProvider;

    private Button btnConnect;
    private Button btnScan;
    private TextView tvDeviceName;
    private TextView tvHR;
    private TextView tvBatteryLevel;

    private Timer hrReader;

    private DeviceAdapter deviceAdapter;
    boolean isScanning = false;

    private static SettingActivity.Callback callback;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting_hrm_con);

        providers = HRManager.getHRProviderList(this);
        deviceAdapter = new DeviceAdapter(this);

        if(providers.isEmpty()){
            notSupported();
            return;
        }
        tvDeviceName = (TextView) findViewById(R.id.fshc_tvDeviceName);
        tvHR = (TextView) findViewById(R.id.fshc_tvHr);
        tvBatteryLevel = (TextView) findViewById(R.id.fshc_tvBatteryLevel);
        tvBatteryLevel.setVisibility(View.GONE);

        btnScan = (Button) findViewById(R.id.btnHRMScan);
        btnScan.setOnClickListener(scanButtonClick);

        btnConnect = (Button) findViewById(R.id.btnHRMConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        load();
        open();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hrm_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_hrmsettings_clear:
                clearHRSettings();
                break;
            case R.id.menu_hrzones:
                hrZonesClick.onClick(null);
                break;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close();
        stopTimer();
    }

    @Override
    public void onOpenResult(boolean ok) {
//        log(hrProvider.getProviderName() + "::onOpenResult(" + ok + ")");
        if (isScanning) {
            isScanning = false;
            startScan();
            return;
        }

        updateView();
    }

    @Override
    public void onScanResult(HRDeviceReference device) {
//        log(hrProvider.getProviderName() + "::onScanResult(" + device.getAddress() + ", "
//                + device.getName() + ")");
        deviceAdapter.deviceList.add(device);
        deviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConnectResult(boolean connectOK) {
//        log(hrProvider.getProviderName() + "::onConnectResult(" + connectOK + ")");
        if (connectOK) {
            save();
            if (hrProvider.getBatteryLevel() > 0) {
                tvBatteryLevel.setVisibility(View.VISIBLE);
                tvBatteryLevel.setText(hrProvider.getBatteryLevel()+" ");
            }
            startTimer();
        } else {
        }
        updateView();
    }

    @Override
    public void onDisconnectResult(boolean disconnectOK) {
//        log(hrProvider.getProviderName() + "::onDisconnectResult(" + disconnectOK + ")");
    }

    @Override
    public void onCloseResult(boolean closeOK) {
//        log(hrProvider.getProviderName() + "::onCloseResult(" + closeOK + ")");
    }

    @Override
    public void log(HRProvider src, String msg) {
//        log(src.getProviderName() + ": " + msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (!hrProvider.isEnabled()) {
//                log("Bluetooth not enabled!");
                btnScan.setEnabled(false);
                btnConnect.setEnabled(false);
                return;
            }
            load();
            open();
            return;
        }
        if (requestCode == 123) {
            startScan();
            return;
        }
    }

    private final View.OnClickListener hrZonesClick = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // startActivity(new Intent(HRSettingsActivity.this, HRZonesActivity.class));
        }
    };

    private void clearHRSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.clear_hr_settings));
        builder.setMessage(getString(R.string.prompt_sure));
        builder.setPositiveButton(getString(R.string.prompt_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                doClear();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        dialog.dismiss();
                    }

                });
        builder.show();
    }

    private void open(){
        if(hrProvider !=null && !hrProvider.isEnabled()){
            if(hrProvider.startEnableIntent(this, 0) == true){
                return;
            }
            hrProvider = null;
        }
        if(hrProvider != null){
            hrProvider.open(handler, this);
        }else{
            updateView();
        }
    }

    /**
     * Close Bluetooth Gatt Client
     */
    private void close(){
        if (hrProvider != null) {
            TrackerHRM.setHrProvider(hrProvider);
            //log(hrProvider.getProviderName() + ".close()");
//            hrProvider.close();
//            hrProvider = null;
        }
    }

    private void startScan(){
        updateView();
        deviceAdapter.deviceList.clear();
        hrProvider.startScan();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.scanning));
        builder.setPositiveButton(getString(R.string.btn_hrm_connect),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        hrProvider.stopScan();
                        connect();
                        updateView();
                        dialog.dismiss();
                    }
                });
        if (hrProvider.isBondingDevice()) {
            builder.setNeutralButton("Pairing", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Intent i = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivityForResult(i, 123);
                }
            });
        }
        builder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        hrProvider.stopScan();
                        load();
                        open();
                        dialog.dismiss();
                        updateView();
                    }
                });

        builder.setSingleChoiceItems(deviceAdapter, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        HRDeviceReference hrDevice = deviceAdapter.deviceList.get(arg1);
                        btAddress = hrDevice.getAddress();
                        btDeviceName = hrDevice.getName();
                    }
                });
        builder.show();
    }

    private void connect(){
        stopTimer();
        if (hrProvider == null || btDeviceName == null || btAddress == null) {
            updateView();
            return;
        }
        if (hrProvider.isConnecting() || hrProvider.isConnected()) {
            hrProvider.disconnect();
            updateView();
            return;
        }

        tvDeviceName.setText(getName());
        tvHR.setText("?");
        String name = btDeviceName;
        if (name == null || name.length() == 0) {
            name = btAddress;
        }
        hrProvider.connect(HRDeviceReference.create(btProviderName, btDeviceName, btAddress));
        updateView();
    }

    /**
     * Save info about the connected device
     */
    private void save(){
        Resources res = getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString(res.getString(R.string.pref_bt_name), btDeviceName);
        ed.putString(res.getString(R.string.pref_bt_address), btAddress);
        ed.putString(res.getString(R.string.pref_bt_provider), hrProvider.getProviderName());
        ed.commit();
    }

    /**
     * Load info about the connected device
     */
    private void load(){
        Resources res = getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        btDeviceName = prefs.getString(res.getString(R.string.pref_bt_name),null);
        btAddress = prefs.getString(res.getString(R.string.pref_bt_address), null);
        btProviderName = prefs.getString(res.getString(R.string.pref_bt_provider), null);
        System.err.println("btName: " + btDeviceName);
        System.err.println("btAddress: " + btAddress);
        System.err.println("btProviderName: " + btProviderName);

        if (btProviderName != null) {
            hrProvider = HRManager.getHRProvider(this, btProviderName);
        }
    }

    /**
     * Clear info about the previous connected device
     */
    private void doClear(){
        Resources res = getResources();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = prefs.edit();
        ed.remove(res.getString(R.string.pref_bt_name));
        ed.remove(res.getString(R.string.pref_bt_address));
        ed.remove(res.getString(R.string.pref_bt_provider));
        ed.commit();
    }

    private void updateView(){
        if (hrProvider == null) {
            btnScan.setEnabled(true);
            btnConnect.setEnabled(false);
            btnConnect.setText(getString(R.string.btn_hrm_connect));
            tvDeviceName.setText(getString(R.string.description_no_device));
            tvHR.setText("--");
            return;
        }

        if (btDeviceName != null) {
            tvDeviceName.setText(btDeviceName);
        } else {
            tvDeviceName.setText(getString(R.string.description_no_device));
            tvHR.setText("--");
        }

        if (hrProvider.isConnected()) {
            btnConnect.setText(getString(R.string.btn_hrm_disconnect));
            btnConnect.setEnabled(true);
        } else if (hrProvider.isConnecting()) {
            btnConnect.setEnabled(false);
            btnConnect.setText(getString(R.string.btn_hrm_connecting));
        } else {
            btnConnect.setEnabled(btDeviceName == null ? false : true);
            btnConnect.setText(getString(R.string.btn_hrm_connect));
        }
    }

    private void startTimer(){
        hrReader = new Timer();
        hrReader.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        readHR();
                    }
                });
            }
        }, 0, 500);
    }

    private void stopTimer(){
        if(hrReader == null)
            return;

        hrReader.cancel();
        hrReader.purge();
        hrReader = null;
    }

    protected void readHR(){
        if (hrProvider != null) {
            long age = hrProvider.getHRValueTimestamp();
            int hrValue = hrProvider.getHRValue();
            tvHR.setText(Integer.toString(hrValue));
        }
    }
    public void notSupported(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.err_msg_hrm_not_supported));
        builder.setMessage(getString(R.string.prompt_try_again_later));
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        };
        builder.setNegativeButton(getString(R.string.prompt_ok), listener);
        builder.show();
        return;
    }

    private final View.OnClickListener scanButtonClick = new View.OnClickListener(){
        public void onClick(View v) {
            clear();
            stopTimer();

            close();
            isScanning = true;
            selectProvider();
        }
    };
    private void selectProvider(){
        final CharSequence items[] = new CharSequence[providers.size()];
        final CharSequence itemNames[] = new CharSequence[providers.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = providers.get(i).getProviderName();
            itemNames[i] = providers.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_bt_device));
        builder.setPositiveButton(getString(R.string.prompt_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        open();
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isScanning = false;
                        load();
                        open();
                        dialog.dismiss();
                    }

                });
        builder.setSingleChoiceItems(itemNames, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        hrProvider = HRManager.getHRProvider(HRMonitorSettingActivity.this,
                                items[arg1].toString());
//                        log("hrProvider = " + hrProvider.getProviderName());
                    }
                });
        builder.show();
    }
    private CharSequence getName() {
        if (btDeviceName != null && btDeviceName.length() > 0)
            return btDeviceName;
        return btAddress;
    }

    private void clear(){
        btAddress = null;
        btDeviceName = null;
        btProviderName = null;
    }

    /**
     * Private Class
     */
    class DeviceAdapter extends BaseAdapter {

        final ArrayList<HRDeviceReference> deviceList = new ArrayList<HRDeviceReference>();
        LayoutInflater inflater = null;
        Resources resources = null;

        DeviceAdapter(Context ctx) {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resources = ctx.getResources();
        }

        @Override
        public int getCount() {
            return deviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row;
            if (convertView == null) {
                row = inflater.inflate(android.R.layout.simple_list_item_single_choice,
                        null);
            } else {
                row = convertView;
            }
            TextView tv = (TextView) row.findViewById(android.R.id.text1);
            tv.setTextColor(resources.getColor(R.color.background_material_dark));

            HRDeviceReference btDevice = deviceList.get(position);
            tv.setTag(btDevice);
            tv.setText(btDevice.getName());

            return tv;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(callback!=null) {
            callback.updateHRMIcon();
        }
        this.finish();
    }

    public static void setCallbackActivity(SettingActivity.Callback callbackActivity){
        callback = callbackActivity;
    }

}
