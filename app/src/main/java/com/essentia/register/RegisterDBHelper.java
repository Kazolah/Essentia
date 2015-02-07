package com.essentia.register;

import android.content.Context;

import com.essentia.orm.BaseDBHelper;
import com.essentia.orm.Fields;
import com.essentia.orm.Types;

/**
 * Created by kyawzinlatt94
 */
public class RegisterDBHelper extends BaseDBHelper {
    /**
     * Instantiates a new base db helper.
     *
     * @param context the context
     */
    public RegisterDBHelper(Context context) {
        super(context);
        this.name ="users";

        columns.add(new Fields("username","User Name", Types.text()));
        columns.add(new Fields("password","Password",Types.text()));
        columns.add(new Fields("dob","Date Of Birth", Types.text()));
        columns.add(new Fields("gender","Gender", Types.text()));
        columns.add(new Fields("weight","Weight", Types.text()));
        columns.add(new Fields("height","Height", Types.text()));
        columns.add(new Fields("active","Active",Types.text()));
    }
}
