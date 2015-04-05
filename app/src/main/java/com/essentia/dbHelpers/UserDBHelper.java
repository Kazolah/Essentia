package com.essentia.dbHelpers;

import android.content.ContentValues;
import android.content.Context;

import com.essentia.orm.BaseDBHelper;
import com.essentia.orm.Fields;
import com.essentia.orm.Types;
import com.essentia.support.UserObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kyawzinlatt94 on 3/2/15.
 */
public class UserDBHelper extends BaseDBHelper{
    public static final String TABLE = "user";

    public static final String NAME = "name";
    public static final String AGE = "age";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String GENDER = "gender";
    public static final String MAX_HR = "max_hr";
    public static final String AVG_HR = "avg_hr";
    public static final String RESTING_HR = "resting_hr";

    /**
     * Instantiates a new base db helper.
     *
     * @param context the context
     */
    public UserDBHelper(Context context) {
        super(context);

        this.name = TABLE;
        columns.add(new Fields(NAME,"Name", Types.text()));
        columns.add(new Fields(AGE,"Age", Types.text()));
        columns.add(new Fields(HEIGHT,"Height",Types.integer()));
        columns.add(new Fields(WEIGHT,"Weight",Types.integer()));
        columns.add(new Fields(GENDER,"Gender",Types.text()));
        columns.add(new Fields(MAX_HR,"Maximum Heart Rate",Types.integer()));
        columns.add(new Fields(AVG_HR,"Average Heart Rate",Types.integer()));
        columns.add(new Fields(RESTING_HR,"Resting Heart Rate",Types.integer()));
    }
    public void updateAvgHR(String id, String avgHR){
        String where = "id=?";
        String[] whereArgs = new String[]{id};
        ContentValues dataVals = new ContentValues();
        dataVals.put(AVG_HR, avgHR);
        this.updateRecord(this,dataVals, where, whereArgs);
    }
    public UserObject getUserObject(){
        UserObject user = new UserObject();
        String[] from = new String[]{"id",NAME, AGE, HEIGHT, WEIGHT, MAX_HR, AVG_HR, RESTING_HR, GENDER};
        HashMap<String, Object> result = this.search(this, from, null, null, null, null, null, null);
        try {
            for (HashMap<String, Object> row : (List<HashMap<String, Object>>) result.get("records")) {
                user.setUserId(row.get("id").toString());
                user.setName(row.get(NAME).toString());
                user.setAge(row.get(AGE).toString());
                user.setHeight(row.get(HEIGHT).toString());
                user.setWeight(row.get(WEIGHT).toString());
                user.setAvgHR(row.get(AVG_HR).toString());
                user.setMaxHR(row.get(MAX_HR).toString());
                user.setRestingHR(row.get(RESTING_HR).toString());
                user.setGender(row.get(GENDER).toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }
    public int getCount(){
        String total = "0";
        String[] whereArgs = new String[]{};
        List<HashMap<String, Object>> resultList = this.executeSQL("SELECT COUNT(*) AS total " +
                "FROM  `user` ", whereArgs);
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
        return Integer.valueOf(total);
    }
}
