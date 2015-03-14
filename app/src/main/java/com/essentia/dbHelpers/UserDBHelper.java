package com.essentia.dbHelpers;

import android.content.Context;

import com.essentia.orm.BaseDBHelper;
import com.essentia.orm.Fields;
import com.essentia.orm.Types;

/**
 * Created by kyawzinlatt94 on 3/2/15.
 */
public class UserDBHelper extends BaseDBHelper{
    public static final String TABLE = "user";
    public static final String NAME = "name";
    public static final String DOB = "dob";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String MAX_HR = "max_hr";
    public static final String AVG_HR = "avg_hr";
    public static final String STATUS = "status";

    /**
     * Instantiates a new base db helper.
     *
     * @param context the context
     */
    public UserDBHelper(Context context) {
        super(context);

        this.name = TABLE;
        columns.add(new Fields(NAME,"Name", Types.text()));
        columns.add(new Fields(DOB,"DOB",Types.text()));
        columns.add(new Fields(HEIGHT,"Height",Types.integer()));
        columns.add(new Fields(WEIGHT,"Weight",Types.integer()));
        columns.add(new Fields(MAX_HR,"Maximum Heart Rate",Types.integer()));
        columns.add(new Fields(AVG_HR,"Average Heart Rate",Types.integer()));
        columns.add(new Fields(STATUS,"Status",Types.text()));
    }

}
