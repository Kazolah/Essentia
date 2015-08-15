package com.essentia.dbHelpers;

import android.content.Context;

/**
 * Created by kyawzinlatt94 on 3/5/15.
 *
 * This class builds database table and populate data.
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

    /**
     * Default data to be filled
     */
    public void fillData(){
        LocationDBHelper locationDBHelper = new LocationDBHelper(context);
        ActivityDBHelper activityDBHelper = new ActivityDBHelper(context);
        ActivityHRDetailDBHelper dbHelper = new ActivityHRDetailDBHelper(context);

        activityDBHelper.executeSQL("INSERT INTO `activity` (`start_time`, `date`," +
                " `calorie`, `duration`,`distance`, `avg_pace`, `avg_speed`, `max_hr`," +
                " `avg_hr`, `sport`, `zone1_info`,`zone2_info`,`zone3_info`,`zone4_info`,`zone5_info`)" +
                " VALUES ('05:20:00 PM', '2015-04-21', '121', " +
                "'440000','0.95', '12:25', '8.5', '180', '166', 'Running','00:00:00-00:02:20','00:02:20-00:06:07','00:06:07-00:07:20','','');",null);

        locationDBHelper.executeSQL("INSERT INTO `location` (`activity_id`, `type`, `time`, `latitude`, `longitude`) " +
                "VALUES ('1', '1', '1000', '2.944130', '101.875095'),"+
                "('1', '3', '1000', '2.944121', '101.875080'),"+
                "('1', '3', '1000', '2.944098', '101.875091'),"+
                "('1', '3', '1000', '2.944069', '101.875107'),"+
                "('1', '3', '1000', '2.944049', '101.875115'),"+
                "('1', '3', '1000', '2.944028', '101.875125'),"+
                "('1', '3', '1000', '2.943994', '101.875143'),"+
                "('1', '3', '1000', '2.943940', '101.875174'),"+
                "('1', '3', '1000', '2.943775', '101.875253'),"+
                "('1', '3', '1000', '2.943635', '101.875314'),"+
                "('1', '3', '1000', '2.943437', '101.875359'),"+
                "('1', '3', '1000', '2.943417', '101.875361'),"+
                "('1', '3', '1000', '2.943243', '101.875305'),"+
                "('1', '3', '1000', '2.943088', '101.875162'),"+
                "('1', '3', '1000', '2.943013', '101.875022'),"+
                "('1', '3', '1000', '2.942997', '101.874887'),"+
                "('1', '3', '1000', '2.943017', '101.874775'),"+
                "('1', '3', '1000', '2.943072', '101.874684'),"+
                "('1', '3', '1000', '2.943141', '101.874628'),"+
                "('1', '3', '1000', '2.943306', '101.874555'),"+
                "('1', '3', '1000', '2.943684', '101.874431'),"+
                "('1', '3', '1000', '2.943753', '101.874407'),"+
                "('1', '3', '1000', '2.944012', '101.874265'),"+
                "('1', '3', '1000', '2.944118', '101.874186'),"+
                "('1', '3', '1000', '2.944282', '101.874042'),"+
                "('1', '3', '1000', '2.944347', '101.874032'),"+
                "('1', '3', '1000', '2.944388', '101.874005'),"+
                "('1', '3', '1000', '2.944422', '101.873955'),"+
                "('1', '3', '1000', '2.944427', '101.873887'),"+
                "('1', '3', '1000', '2.944800', '101.873140'),"+
                "('1', '3', '1000', '2.944905', '101.873000'),"+
                "('1', '3', '1000', '2.944994', '101.872955'),"+
                "('1', '3', '1000', '2.945086', '101.872947'),"+
                "('1', '3', '1000', '2.945211', '101.872962'),"+
                "('1', '3', '1000', '2.945311', '101.873020'),"+
                "('1', '3', '1000', '2.945414', '101.873105'),"+
                "('1', '3', '1000', '2.945468', '101.873216'),"+
                "('1', '3', '1000', '2.945494', '101.873358'),"+
                "('1', '3', '1000', '2.945484', '101.873492'),"+
                "('1', '3', '1000', '2.945441', '101.873620'),"+
                "('1', '3', '1000', '2.945178', '101.873968'),"+
                "('1', '3', '1000', '2.945086', '101.874064'),"+
                "('1', '3', '1000', '2.944837', '101.874353'),"+
                "('1', '3', '1000', '2.944527', '101.874789'),"+
                "('1', '3', '1000', '2.944178', '101.875054'),"+
                "('1', '2', '1000', '2.943644', '101.875317')",null);

        for (int i=1;i<=440;i++){
            if(i<140) {
                dbHelper.executeSQL("INSERT INTO `activity_hr_detail` (`time_stamp`, `hr_value`, `hr_zone`, `activity_id`) " +
                        "VALUES ('" + 1000 * i + "', '" + (150 + i%2) + "', '" + 1 + "', '1')", null);
            }else if(i<367){
                dbHelper.executeSQL("INSERT INTO `activity_hr_detail` (`time_stamp`, `hr_value`, `hr_zone`, `activity_id`) " +
                        "VALUES ('" + 1000 * i + "', '" + (160 + i%2) + "', '" + 2 + "', '1')", null);
            }else{
                dbHelper.executeSQL("INSERT INTO `activity_hr_detail` (`time_stamp`, `hr_value`, `hr_zone`, `activity_id`) " +
                        "VALUES ('" + 1000 * i + "', '" + (181 - i%2) + "', '" + 3 + "', '1')", null);
            }
        }

    }
}
