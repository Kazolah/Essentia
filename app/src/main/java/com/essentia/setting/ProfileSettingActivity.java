package com.essentia.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.essentia.dbHelpers.UserDBHelper;
import com.essentia.hrm.HRProvider;
import com.essentia.main.MainActivity;
import com.essentia.support.ApplicationContext;
import com.essentia.support.UserObject;
import com.essentia.tracker.component.TrackerHRM;
import com.example.kyawzinlatt94.essentia.R;

/**
 * Created by kyawzinlatt94 on 2/3/15.
 */
public class ProfileSettingActivity extends Activity{
    private TextView tvAvgHr;
    private TextView tvMaxHr;
    private EditText edtUserName;
    private EditText edtAge;
    private EditText edtWeight;
    private EditText edtHeight;
    private EditText edtMaxHR;
    private EditText edtRestingHR;
    private RadioGroup rdoGender;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private Button btnUpdate;
    private UserDBHelper userDBHelper;
    private UserObject userObject;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor editor;
    private Context context;
    TrackerHRM trackerHRM;
    HRProvider hrProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString((R.string.title_profile)));
        userDBHelper = new UserDBHelper(this);
        userObject = userDBHelper.getUserObject();
        context = this;
        sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

        tvAvgHr = (TextView) findViewById(R.id.ap_avgHR);
        tvMaxHr = (TextView) findViewById(R.id.ap_maxHR);
        edtUserName = (EditText) findViewById(R.id.ap_etName);
        edtAge = (EditText) findViewById(R.id.ap_edAge);
        edtWeight = (EditText) findViewById(R.id.ap_edWeight);
        edtHeight = (EditText) findViewById(R.id.ap_edHeight);
        edtMaxHR = (EditText) findViewById(R.id.ap_edMaxHr);
        edtRestingHR = (EditText) findViewById(R.id.ap_edRestingHR);
        rdoGender = (RadioGroup) findViewById(R.id.ap_rdoGenderGroup);
        rdoMale = (RadioButton) findViewById(R.id.ap_rdoMale);
        rdoFemale = (RadioButton) findViewById(R.id.ap_rdoFemale);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(updateClickListener);

        double dAvgHR = Double.valueOf(userObject.getAvgHR());
        int iAvgHR = (int) dAvgHR;
        tvAvgHr.setText(iAvgHR+"");
        tvMaxHr.setText(userObject.getMaxHR());
        edtMaxHR.setText(userObject.getMaxHR());
        edtRestingHR.setText(userObject.getRestingHR());
        edtUserName.setText(userObject.getName());
        edtAge.setText(userObject.getAge());
        edtWeight.setText(userObject.getWeight());
        edtHeight.setText(userObject.getHeight());
        if(userObject.getGender().equals("Male")){
            rdoMale.setSelected(true);
        }else{
            rdoFemale.setSelected(true);
        }
        trackerHRM = new TrackerHRM();
        hrProvider = trackerHRM.getHrProvider();
    }
    private final View.OnClickListener updateClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int selected = rdoGender.getCheckedRadioButtonId();
            RadioButton rdoBtn = (RadioButton)findViewById(selected);
            String gender = rdoBtn.getText().toString();

            //if maximum heart rate is put 0,
            // use the default equation to decide maximum heart rate
            String maxHR = edtMaxHR.getText().toString();
            if(maxHR.equals("0")){
                double age = Double.valueOf(edtAge.getText().toString());
                maxHR = String.valueOf(getMaxHeartRateZone(age, gender));
            }

            ContentValues dataVals = new ContentValues();
            String userId = userObject.getUserId();
            dataVals.put(UserDBHelper.AGE, edtAge.getText().toString());
            dataVals.put(UserDBHelper.WEIGHT, edtWeight.getText().toString());
            dataVals.put(UserDBHelper.HEIGHT, edtHeight.getText().toString());
            dataVals.put(UserDBHelper.NAME, edtUserName.getText().toString());
            dataVals.put(UserDBHelper.GENDER, gender);
            dataVals.put(UserDBHelper.MAX_HR, maxHR);
            dataVals.put(UserDBHelper.RESTING_HR, edtRestingHR.getText().toString());
            String where = "id=?";
            String[] whereArgs = new String[]{userId};
            try {
                userDBHelper.updateRecord(userDBHelper, dataVals, where, whereArgs);
                ApplicationContext.userObject = userDBHelper.getUserObject();
                Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(context, MainActivity.class));
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Updated failed", Toast.LENGTH_LONG).show();
            }
        }
    };
        public static int getMaxHeartRateZone(Double age, String gender){
            double HRmax = 0;
            if(gender.equals("Male")){
                HRmax = 205.8 - (0.685 * age);
            }else {
                HRmax = 206 - (0.88 * age);
            }
            return (int) HRmax;
        }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
    public void openNotice(View view){
        final ProfileSettingActivity profileActivity = this;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Notice: Measuring Resting Heart Rate");
        alertDialogBuilder.setMessage("Ensure you are at complete rest to measure an accurate resting heart rate.");
        alertDialogBuilder.setPositiveButton("Start Measuring",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        MeasureRestHRTask hrTask = new MeasureRestHRTask(profileActivity);
                        hrTask.execute();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void measureRestingHR(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Information");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void setRestingHRText(String restingHR){
        edtRestingHR.setText(restingHR);
    }
    private class MeasureRestHRTask extends AsyncTask<Void, Void, Boolean>{
        private ProgressDialog dialog;
        int totalHR = 0;
        int counter = 0;
        int restingHR = 0;

        public MeasureRestHRTask(ProfileSettingActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Measuring...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            setRestingHRText(restingHR + "");
            if(result){
                dialog.dismiss();
                measureRestingHR("Resting Heart Rate Updated.");
            }else{
                dialog.dismiss();
                measureRestingHR("Heart Rate Monitor Unavailable.");
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            long startTime = SystemClock.uptimeMillis();
            long endTime = 0L;
            while(endTime-startTime<20000){
                if (hrProvider == null) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    int hrValue = hrProvider.getHRValue();
                    totalHR += hrValue;
                    counter++;
                    restingHR = (int) totalHR / counter;
                }
                endTime = SystemClock.uptimeMillis();
            }
            return true;
        }
    }
}
