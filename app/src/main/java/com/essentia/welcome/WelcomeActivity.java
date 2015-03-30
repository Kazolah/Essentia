package com.essentia.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.essentia.dbHelpers.DBBuilder;
import com.essentia.dbHelpers.UserDBHelper;
import com.essentia.main.MainActivity;
import com.essentia.register.RegisterActivity;
import com.essentia.support.ApplicationContext;
import com.essentia.support.UserObject;
import com.example.kyawzinlatt94.essentia.R;


public class WelcomeActivity extends ActionBarActivity {
    private UserDBHelper userDBHelper;
    private UserObject userObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBBuilder dbBuilder = new DBBuilder(this);
        dbBuilder.buildDBs();

        userDBHelper = new UserDBHelper(this);
        int count = userDBHelper.getCount();
        if(count==0) {
            setContentView(R.layout.activity_welcome);
        }else{
            userObject = userDBHelper.getUserObject();
            ApplicationContext.userObject = userObject;
            this.finish();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        getSupportActionBar().hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    public void initGetStarted(View view){
        finish();
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
