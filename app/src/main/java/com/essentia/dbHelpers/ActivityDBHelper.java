package com.essentia.dbHelpers;

import android.content.Context;

import com.essentia.orm.BaseDBHelper;
import com.essentia.orm.Fields;
import com.essentia.orm.Types;
import com.essentia.support.WorkoutActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kyawzinlatt94 on 3/2/15.
 */
public class ActivityDBHelper extends BaseDBHelper{
    public static final String TABLE = "activity";
    public static final String START_TIME = "start_time";
    public static final String DATE = "date";
    public static final String USER_ID = "user_id";
    public static final String CALORIE = "calorie";
    public static final String DURATION = "duration";
    public static final String DISTANCE = "distance";
    public static final String AVG_PACE = "avg_pace";
    public static final String AVG_SPEED = "avg_speed";
    public static final String MAX_HR = "max_hr";
    public static final String AVG_HR = "avg_hr";
    public static final String SPORT = "sport";


    /**
     * Instantiates a new base db helper.
     *
     * @param context the context
     */
    public ActivityDBHelper(Context context) {
        super(context);
        this.name = TABLE;
        columns.add(new Fields(USER_ID,"User Id",Types.text()));
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
    }

    /**
     * Load the information about the activity with id
     * @param activityId ID to be searched
     * @return Activity with loaded information
     */
    public WorkoutActivity queryActivity(String activityId){
        WorkoutActivity workoutActivity = new WorkoutActivity();
        workoutActivity.setId(activityId);
        String[] from = new String[]{DATE, START_TIME, DURATION, AVG_PACE, AVG_SPEED, AVG_HR, MAX_HR, DISTANCE, SPORT};
        String[] where = new String[]{"id=?"};
        String[] whereArgs = new String[]{activityId};
        HashMap<String, Object> result = this.search(this,from,where,whereArgs,null,null,null,null);
        try{
            for(HashMap<String, Object> row : (List<HashMap<String, Object>>) result.get("records")){
                workoutActivity.setDate(row.get(DATE).toString());
                workoutActivity.setStartTime(row.get(START_TIME).toString());
                workoutActivity.setDuration(row.get(DURATION).toString());
                workoutActivity.setAvgPace(row.get(AVG_PACE).toString());
                workoutActivity.setCalorie(row.get(CALORIE).toString());
                workoutActivity.setAvgSpeed(row.get(AVG_SPEED).toString());
                workoutActivity.setMaxHR(row.get(MAX_HR).toString());
                workoutActivity.setDistance(row.get(DISTANCE).toString());
                workoutActivity.setSport(row.get(SPORT).toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return workoutActivity;
    }
}
