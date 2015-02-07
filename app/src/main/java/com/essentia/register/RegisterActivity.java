package com.essentia.register;

import

android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.essentia.main.MainActivity;
import com.essentia.support.UserObject;
import com.example.kyawzinlatt94.essentia.R;




/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends Activity implements LoaderCallbacks<Cursor> {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the register task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mRegTask = null;

    // UI references.
    private EditText mNameView;
    private EditText mWeightView;
    private EditText mHeightView;
    private EditText mDOBView;
    private RadioGroup rdoGroupGender;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegisterFormView;
    private Context context = this;
    private RegisterDBHelper registerDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the register form.
        mNameView = (EditText) findViewById(R.id.edtRegisterName);
        mPasswordView = (EditText) findViewById(R.id.edtRegisterPassword);
        mDOBView = (EditText) findViewById(R.id.edtRegisterDateOfBirth);
        mWeightView = (EditText) findViewById(R.id.edtRegisterWeight);
        mHeightView =(EditText) findViewById(R.id.edtRegisterHeight);
        rdoGroupGender = (RadioGroup) findViewById(R.id.rdoGenderGroup);
        rdoMale = (RadioButton) findViewById(R.id.rdoRegisterMale);
        rdoFemale = (RadioButton) findViewById(R.id.rdoRegisterFemale);

        Button mRegisterButton = (Button) findViewById(R.id.btnRegister);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual attempt is made.
     */
    public void attemptRegister() {
        if (mRegTask != null) {
            return;
        }

        // Reset errors.
        mNameView.setError(null);
        mPasswordView.setError(null);
        mDOBView.setError(null);
        mWeightView.setError(null);
        mHeightView.setError(null);

        // Store values at the time of the register attempt.
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String dob = mDOBView.getText().toString();
        String weight = mWeightView.getText().toString();
        String height = mHeightView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        //Check for a valid date of birth
        if(!TextUtils.isEmpty(dob)){

        }else {
            mDOBView.setError(getString(R.string.error_field_required));
            focusView = mDOBView;
            cancel = true;
        }

        //Check for a valid weight.
        if(TextUtils.isEmpty(weight)){
            mWeightView.setError(getString(R.string.error_field_required));
        }

        //Check for a valid height.
        if(TextUtils.isEmpty(height)){
            mHeightView.setError(getString(R.string.error_field_required));
        }

        //Get the Selected Gender
        int selected = rdoGroupGender.getCheckedRadioButtonId();
        RadioButton rdoBtn = (RadioButton)findViewById(selected);
        String gender = rdoBtn.getText().toString();

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mRegTask = new UserRegisterTask(name, password, dob, gender, weight, height);
            mRegTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the register form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private UserObject user = new UserObject();

        public UserRegisterTask(String name, String password, String dob,
                         String gender, String weight, String height) {
            user.setName(name);
            user.setPassword(password);
            user.setDateOfBirth(dob);
            user.setGender(gender);
            user.setWeight(weight);
            user.setHeight(height);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            //Create Databases for the app
            createDatabases();

            //Create Record in database for registration
//            if(RegisterUtil.checkUserNameExists(user.getName())) {
//                createUserRecord(user);
//            }else
//                return false;

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            showProgress(false);

            if (success) {
                finish();
                startActivity(new Intent(context, MainActivity.class));
            } else {
                mNameView.setError(getString(R.string.error_user_exist));
                mNameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }

        private void createDatabases(){
            registerDBHelper = new RegisterDBHelper(context);
            registerDBHelper.createTable(registerDBHelper.createStatement(registerDBHelper));
        }
        private void createUserRecord(UserObject user){
            ContentValues dataVals = new ContentValues();
            dataVals.put("username",user.getName());
            dataVals.put("password",user.getPassword());
            dataVals.put("weight",user.getWeight());
            dataVals.put("height",user.getHeight());
            dataVals.put("gender",user.getGender());
            dataVals.put("dob",user.getDateOfBirth());
            dataVals.put("active", "true");
            registerDBHelper.create(registerDBHelper, dataVals);
        }
    }
}



