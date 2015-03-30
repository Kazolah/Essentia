package com.essentia.dbHelpers;

import android.content.Context;

import com.essentia.orm.BaseDBHelper;
import com.essentia.orm.Fields;
import com.essentia.orm.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kyawzinlatt94 on 3/3/15.
 */
public class ActivityHRDetailDBHelper extends BaseDBHelper{
    public static final String TABLE = "activity.hr.detail";
    public static final String TIME_IN_MS = "time_stamp";
    public static final String HR_VALUE = "hr_value";
    public static final String HR_ZONE = "hr_zone";
    public static final String ACTIVITY_ID = "activity_id";
    /**
     * Instantiates a new base db helper.
     *
     * @param context the context
     */
    public ActivityHRDetailDBHelper(Context context) {
        super(context);
        this.name = TABLE;
        columns.add(new Fields(TIME_IN_MS, "Time Stamp", Types.integer()));
        columns.add(new Fields(HR_VALUE, "Heart Rate Value", Types.integer()));
        columns.add(new Fields(HR_ZONE, "Heart Rate Zones", Types.integer()));
        columns.add(new Fields(ACTIVITY_ID, "Activity Id", Types.integer()));

    }
    public ArrayList<HRDetailQuery> queryHRDetail(String activityId){
        ArrayList<HRDetailQuery> queryList = new ArrayList<>();
        String[] from = new String[]{TIME_IN_MS, HR_VALUE, HR_ZONE};
        String[] where = new String[]{ACTIVITY_ID + "=?"};
        String[] whereArgs = new String[]{activityId};
        HashMap<String, Object> result = this.search(this,from,where, whereArgs,null,null,null,null);
        try{
            for (HashMap<String, Object> row : (List<HashMap<String, Object>>) result
                    .get("records")) {
               queryList.add(convertToHRDetailQuery(row));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return queryList;
    }

    public int queryZonePercentage(String activityId, int zone){
        String hrZone = String.valueOf(zone);
        if(hrZone.equals(null) || activityId.equals(null)){
            return 0;
        }
        String totalHRDuration = "";
        String[] args = new String[]{hrZone, activityId};
        List<HashMap<String, Object>> resultList = this.executeSQL("SELECT SUM( time_stamp ) AS total " +
            "FROM  `activity_hr_detail` " + "WHERE hr_zone =? AND activity_id =?", args);
        try{
            for (HashMap<String, Object> row : resultList) {
                try {
                    totalHRDuration = row.get("total").toString();
                }catch(NullPointerException e){
                    totalHRDuration = "0";
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        totalHRDuration = (totalHRDuration.equals(""))?"0":totalHRDuration;
        return Integer.valueOf(totalHRDuration);
    }
    /**
     * Convert the Hashmap into HRDetailQuery object
     * @param row
     * @return
     */
    public HRDetailQuery convertToHRDetailQuery(HashMap<String, Object> row){
        HRDetailQuery detailQuery = new HRDetailQuery(row.get(HR_VALUE).toString(),
                row.get(HR_ZONE).toString(), row.get(TIME_IN_MS).toString());
        return detailQuery;
    }

    public class HRDetailQuery{
        public String hrValue;
        public String hrZone;
        public String timeInMs;
        public HRDetailQuery(String hrValue, String hrZone, String timeInMs){
            this.hrValue = hrValue;
            this.hrZone = hrZone;
            this.timeInMs = timeInMs;
        }
        public String getHrValue(){
            return hrValue;
        }
        public String getHrZone(){
            return hrZone;
        }
        public String getTimeInMs(){
            return timeInMs;
        }
    }
}
