package com.essentia.dbHelpers;

import android.content.Context;

/**
 * Created by kyawzinlatt94 on 3/5/15.
 */
public class DBBuilder {
    Context context;
    public DBBuilder(Context context){
        this.context = context;
    }
    public void buildDBs(){
        createUserTable();
        createLocationTable();
        createActivityTable();
        createActivityHRDetailTable();
        fillData();
    }
    private void createUserTable(){
        UserDBHelper dbHelper = new UserDBHelper(context);
        dbHelper.createTable(dbHelper.createStatement(dbHelper));
    }
    private void createLocationTable(){
        LocationDBHelper dbHelper = new LocationDBHelper(context);
        dbHelper.createTable(dbHelper.createStatement(dbHelper));
    }
    private void createActivityTable(){
        ActivityDBHelper dbHelper = new ActivityDBHelper(context);
        dbHelper.createTable(dbHelper.createStatement(dbHelper));
    }

    private void createActivityHRDetailTable(){
        ActivityHRDetailDBHelper dbHelper = new ActivityHRDetailDBHelper(context);
        dbHelper.createTable(dbHelper.createStatement(dbHelper));
    }
    private void fillData(){
        LocationDBHelper locationDBHelper = new LocationDBHelper(context);
        ActivityDBHelper activityDBHelper = new ActivityDBHelper(context);
        ActivityHRDetailDBHelper dbHelper = new ActivityHRDetailDBHelper(context);

        activityDBHelper.executeSQL("INSERT INTO `activity` (`start_time`, `date`," +
                " `user_id`, `calorie`, `duration`,`distance`, `avg_pace`, `avg_speed`, `max_hr`," +
                " `avg_hr`, `sport`) VALUES ('00:10:00 AM', '14-03-2015', '1', '1200', " +
                "'10000','3000', '0', '0', '210', '190', '1');",null);

        locationDBHelper.executeSQL("INSERT INTO `location` (`activity_id`, `type`, `time`, `latitude`, `longitude`) " +
                "VALUES ('1', '1', '1000', '3.139019', '101.686748')",null);
        locationDBHelper.executeSQL("INSERT INTO `location` (`activity_id`, `type`, `time`, `latitude`, `longitude`) " +
                "VALUES ('1', '3', '1000', '3.139024', '101.686708')",null);
        locationDBHelper.executeSQL("INSERT INTO `location` (`activity_id`, `type`, `time`, `latitude`, `longitude`) " +
                "VALUES ('1', '3', '1000', '3.139030', '101.686662')",null);
        locationDBHelper.executeSQL("INSERT INTO `location` (`activity_id`, `type`, `time`, `latitude`, `longitude`) " +
                "VALUES ('1', '2', '1000', '3.139032', '101.686614')",null);
        for (int i=1;i<=10;i++){
            int j=1;
            if(i>5)
                j=2;
            dbHelper.executeSQL("INSERT INTO `activity_hr_detail` (`time_stamp`, `hr_value`, `hr_zone`, `activity_id`) " +
                    "VALUES ('"+(1000*i)+"', '"+(134+i)+"', '"+j+"', '1')", null);
        }

    }
}
