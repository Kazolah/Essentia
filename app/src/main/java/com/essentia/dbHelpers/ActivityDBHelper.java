package com.essentia.dbHelpers;

import android.content.Context;

import com.essentia.orm.BaseDBHelper;
import com.essentia.orm.Fields;
import com.essentia.orm.Types;
import com.essentia.statistics.StatisticListItem;
import com.essentia.support.WorkoutActivity;
import com.essentia.util.Formatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kyawzinlatt94 on 3/2/15.
 */
public class ActivityDBHelper extends BaseDBHelper{
    public static final String TABLE = "activity";
    public static final String START_TIME = "start_time";
    public static final String DATE = "date";
    public static final String CALORIE = "calorie";
    public static final String DURATION = "duration";
    public static final String DISTANCE = "distance";
    public static final String AVG_PACE = "avg_pace";
    public static final String AVG_SPEED = "avg_speed";
    public static final String MAX_HR = "max_hr";
    public static final String AVG_HR = "avg_hr";
    public static final String SPORT = "sport";
    public static final String ZONE1_INFO = "zone1_info";
    public static final String ZONE2_INFO = "zone2_info";
    public static final String ZONE3_INFO = "zone3_info";
    public static final String ZONE4_INFO = "zone4_info";
    public static final String ZONE5_INFO = "zone5_info";


    /**
     * Instantiates a new base db helper.
     *
     * @param context the context
     */
    public ActivityDBHelper(Context context) {
        super(context);
        this.name = TABLE;
        columns.add(new Fields(DATE,"date", Types.text()));
        columns.add(new Fields(START_TIME,"start_time", Types.text()));
        columns.add(new Fields(DURATION,"duration", Types.text()));
        columns.add(new Fields(CALORIE,"calorie", Types.text()));
        columns.add(new Fields(AVG_PACE,"Average Pace", Types.text()));
        columns.add(new Fields(AVG_SPEED,"Average Speed", Types.text()));
        columns.add(new Fields(AVG_HR,"Average Heart Rate", Types.text()));
        columns.add(new Fields(MAX_HR,"Maximum Heart Rate", Types.text()));
        columns.add(new Fields(DISTANCE,"Distance", Types.text()));
        columns.add(new Fields(SPORT,"Sport", Types.text()));
        columns.add(new Fields(ZONE1_INFO,"Zone 1 Info", Types.text()));
        columns.add(new Fields(ZONE2_INFO,"Zone 2 Info", Types.text()));
        columns.add(new Fields(ZONE3_INFO,"Zone 3 Info", Types.text()));
        columns.add(new Fields(ZONE4_INFO,"Zone 4 Info", Types.text()));
        columns.add(new Fields(ZONE5_INFO,"Zone 5 Info", Types.text()));
    }

    /**
     * Load the information about the activity with id
     * @param activityId ID to be searched
     * @return Activity with loaded information
     */
    public WorkoutActivity queryActivity(String activityId){
        WorkoutActivity workoutActivity = new WorkoutActivity();
        workoutActivity.setId(activityId);
        String[] from = new String[]{DATE, START_TIME, CALORIE, DURATION, AVG_PACE, AVG_SPEED,
                AVG_HR, MAX_HR, DISTANCE, SPORT, ZONE1_INFO, ZONE2_INFO, ZONE3_INFO, ZONE4_INFO,
                ZONE5_INFO};
        String[] where = new String[]{"id=?"};
        String[] whereArgs = new String[]{activityId};
        HashMap<String, Object> result = this.search(this,from,where,whereArgs,null,null,null,null);
        try{
            for(HashMap<String, Object> row : (List<HashMap<String, Object>>) result.get("records")){
                workoutActivity.setDate(row.get(DATE).toString());
                workoutActivity.setStartTime(row.get(START_TIME).toString());
                workoutActivity.setCalorie(row.get(CALORIE).toString());
                workoutActivity.setDuration(row.get(DURATION).toString());
                workoutActivity.setAvgPace(row.get(AVG_PACE).toString());
                workoutActivity.setAvgSpeed(row.get(AVG_SPEED).toString());
                workoutActivity.setAvgHR(row.get(AVG_HR).toString());
                workoutActivity.setMaxHR(row.get(MAX_HR).toString());
                workoutActivity.setDistance(row.get(DISTANCE).toString());
                workoutActivity.setSport(row.get(SPORT).toString());
                workoutActivity.setZone1Info(row.get(ZONE1_INFO).toString());
                workoutActivity.setZone2Info(row.get(ZONE2_INFO).toString());
                workoutActivity.setZone3Info(row.get(ZONE3_INFO).toString());
                workoutActivity.setZone4Info(row.get(ZONE4_INFO).toString());
                workoutActivity.setZone5Info(row.get(ZONE5_INFO).toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return workoutActivity;
    }

    /**
     * Query all activities from database
     * @return Activity with loaded information
     */
    public ArrayList<WorkoutActivity> queryAllActivity(){
        ArrayList<com.essentia.support.WorkoutActivity> list = new ArrayList<>();
        String[] from = new String[]{"id", DATE, START_TIME, CALORIE, DURATION, AVG_PACE, AVG_SPEED,
                AVG_HR, MAX_HR, DISTANCE, SPORT, ZONE1_INFO, ZONE2_INFO, ZONE3_INFO, ZONE4_INFO,
                ZONE5_INFO};
        String[] where = new String[]{};
        String[] whereArgs = new String[]{};
        HashMap<String, Object> result = this.search(this,from,where,whereArgs,null,null,null,null);
        try{
            for(HashMap<String, Object> row : (List<HashMap<String, Object>>) result.get("records")){
                WorkoutActivity workoutActivity = new WorkoutActivity();
                workoutActivity.setId(row.get("id").toString());
                workoutActivity.setDate(row.get(DATE).toString());
                workoutActivity.setStartTime(row.get(START_TIME).toString());
                workoutActivity.setCalorie(row.get(CALORIE).toString());
                workoutActivity.setDuration(row.get(DURATION).toString());
                workoutActivity.setAvgPace(row.get(AVG_PACE).toString());
                workoutActivity.setAvgSpeed(row.get(AVG_SPEED).toString());
                workoutActivity.setAvgHR(row.get(AVG_HR).toString());
                workoutActivity.setMaxHR(row.get(MAX_HR).toString());
                workoutActivity.setDistance(row.get(DISTANCE).toString());
                workoutActivity.setSport(row.get(SPORT).toString());
                workoutActivity.setZone1Info(row.get(ZONE1_INFO).toString());
                workoutActivity.setZone2Info(row.get(ZONE2_INFO).toString());
                workoutActivity.setZone3Info(row.get(ZONE3_INFO).toString());
                workoutActivity.setZone4Info(row.get(ZONE4_INFO).toString());
                workoutActivity.setZone5Info(row.get(ZONE5_INFO).toString());
                list.add(workoutActivity);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public String getTotalCalorie(){
       return getMetrics(CALORIE);
    }
    public String getTotalDistance(){
        return getMetrics(DISTANCE);
    }
    public String getTotalAvgHR(){
        return getMetrics(AVG_HR);
    }
    public String getMetrics(String metricCol){
       String total = "0";
       String[] whereArgs = new String[]{};
        List<HashMap<String, Object>> resultList = this.executeSQL("SELECT SUM("+ metricCol +") AS total " +
                "FROM  `activity` ", whereArgs);
        try{
            for (HashMap<String, Object> row : resultList) {
                try {
                    total = row.get("total").toString();
                }catch(NullPointerException e){
                    total = "0";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return total;
    }
    public ArrayList<StatisticListItem> getStatisticsItems(){
        ArrayList<StatisticListItem> list = new ArrayList<>();
        String sql = "SELECT SUM(calorie) as calorie, SUM(duration) as duration, SUM(distance) as distance, " +
                "SUM(avg_hr) as avg_hr, COUNT(*) as session, strftime('%m', 'date') as stat_month, " +
                "strftime('%Y', 'date') as stat_year\n" + "FROM `activity`\n" + "GROUP BY stat_month, stat_year";
        List<HashMap<String, Object>> resultList = this.executeSQL(sql, null);
        try{
            for (HashMap<String, Object> row : resultList) {
                try {
                    StatisticListItem listItem = new StatisticListItem();
                    listItem.calorie = row.get("calorie").toString();
                    listItem.duration = Formatter.parseMsIntoTime(row.get("duration").toString());
                    listItem.distance = row.get("distance").toString();
                    listItem.heartRate = row.get("avg_hr").toString();
                    listItem.sessions = row.get("session").toString();
                    listItem.month = row.get("stat_month").toString();
                    listItem.year = row.get("stat_year").toString();
                    list.add(listItem);
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
