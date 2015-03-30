package com.essentia.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kyawzinlatt94
 */
public class ORM extends SQLiteDatabaseHelper{

    /** The user_name. */
    String user_name = "";

    /** The fields. */
    ArrayList<Fields> fields = null;

    /** The table name. */
    String tableName = null;

    /** The context. */
    Context context = null;

    /** The statements. */
    HashMap<String, String> statements = null;


    /**
     * Instantiates a new orm.
     *
     * @param context
     *            the context
     */
    public ORM(Context context) {
        super(context);
        this.context = context;
        this.statements = new HashMap<String, String>();
    }

    /**
     * Creates the statement.
     *
     * @param table
     *            the table
     * @param fields
     *            the fields
     * @return the sQL statement
     */
    private SQLStatement createStatement(String table, List<Fields> fields) {
        SQLStatement statement = new SQLStatement();
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(table);
        sql.append(" (");
        for (Fields field : fields) {
            try {
                sql.append(field.getName());
                sql.append(" ");
                sql.append(field.getType());
                sql.append(", ");
            } catch (Exception e) {

            }
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(")");

        statement.setTable_name(table);
        statement.setType("create");
        statement.setStatement(sql.toString());
        return statement;
    }
    /**
     * Creates the statement.
     *
     * @param moduleDBHelper
     *            the module db helper
     * @return the sQL statement
     */
    public SQLStatement createStatement(BaseDBHelper moduleDBHelper) {
        this.tableName = modelToTable(moduleDBHelper.getModelName());
        this.fields = moduleDBHelper.getColumns();
        SQLStatement statement = new SQLStatement();
        StringBuffer create = new StringBuffer();

        create.append("CREATE TABLE IF NOT EXISTS ");
        create.append(this.tableName);
        create.append(" (");
        for (Fields field : this.fields) {

            Object type = field.getType();
            try {
                if(field.getName().equals("id")){
                 create.append("id integer primary key autoincrement, ");
                }else {
                    create.append(field.getName());
                    create.append(" ");
                    create.append(type.toString());
                    create.append(", ");
                }
            } catch (Exception e) {

            }
        }
        create.deleteCharAt(create.lastIndexOf(","));
        create.append(")");
        this.statements.put("create", create.toString());
        statement.setTable_name(this.tableName);
        statement.setType("create");
        statement.setStatement(create.toString());
        return statement;

    }

    /**
     * Create Record in database
     * @param dbHelper - database
     * @param data_values - values to be inserted
     */
    public long create(BaseDBHelper dbHelper, ContentValues data_values){
        ContentValues values = new ContentValues();

        for (Fields field : dbHelper.getColumns()) {
            values.put(field.getName(),
                    data_values.getAsString(field.getName()));
        }

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(modelToTable(dbHelper.getModelName()), null, values);
        db.close();
        return id;
    }

    public void updateRecord(BaseDBHelper dbHelper, ContentValues data_values,
                             String where, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(modelToTable(dbHelper.getModelName()), data_values, where,
                whereArgs);
        db.close();
    }

    public long create(SQLiteDatabase db, BaseDBHelper dbHelper, ContentValues data_values){
        ContentValues values = new ContentValues();

        for (Fields field : dbHelper.getColumns()) {
            values.put(field.getName(),
                    data_values.getAsString(field.getName()));
        }
        long id = db.insert(modelToTable(dbHelper.getModelName()), null, values);
        return id;
    }


    /**
     * Search
     *
     * @param db
     *            the db
     * @param where
     *            the where
     * @param whereArgs
     *            the where args
     * @param group_by
     *            the group_by
     * @param having
     *            the having
     * @param orderby
     *            the orderby
     * @param ordertype
     *            the ordertype
     * @return the hash map
     */
    public HashMap<String, Object> search(BaseDBHelper db,
                                                String[] columns, String[] where, String[] whereArgs,
                                                String group_by, String having, String orderby, String ordertype) {

        HashMap<String, Object> returnVals = new HashMap<String, Object>();

        String order_by = orderby + " " + ordertype;
        if (orderby == null) {
            order_by = null;
        }

        List<HashMap<String, Object>> results = executeQuery(db, columns,
                where, whereArgs, group_by, having, order_by);
        if (results != null) {
            returnVals.put("total", results.size());
            returnVals.put("records", results);
        } else {
            returnVals.put("total", 0);
            returnVals.put("records", false);
        }
        return returnVals;
    }

    /**
     * Execute query.
     *
     * @param dbHelper
     *            the db helper
     * @param where
     *            the where
     * @param whereVals
     *            the where vals
     * @param group_by
     *            the group_by
     * @param having
     *            the having
     * @param orderby
     *            the orderby
     * @return the list
     */
    private List<HashMap<String, Object>> executeQuery(BaseDBHelper dbHelper,
                                                       String[] fetch_columns, String[] where, String[] whereVals,
                                                       String group_by, String having, String orderby) {
        SQLiteDatabase db = getWritableDatabase();
        List<String> cols = new ArrayList<String>();

        String columns[] = null;
        if (fetch_columns != null) {
            cols = new ArrayList<String>();
            cols = Arrays.asList(fetch_columns);
        } else {
            for (Fields col : dbHelper.getColumns()) {
                 cols.add(col.getName());
            }
        }
        columns = cols.toArray(new String[cols.size()]);
        String whereStmt =whereStatement(where, dbHelper);
        String tableModel = modelToTable(dbHelper.getModelName());
        Cursor cursor = db.query(tableModel,
                columns, whereStmt, whereVals, group_by,
                having, orderby);
        List<HashMap<String, Object>> data = getResult(dbHelper, columns,
                cursor);
        db.close();
        return data;
    }
    public List<HashMap<String, Object>> executeSQL(String sqlQuery,
                                                    String[] args) {
        SQLiteDatabase db = getWritableDatabase();

        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        Cursor cursor = db.rawQuery(sqlQuery.toString(), args);
        String[] columns = cursor.getColumnNames();
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> row = new HashMap<String, Object>();
                for (String key : columns) {
                    row.put(key, cursor.getString(cursor.getColumnIndex(key)));
                }
                data.add(row);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return data;
    }

    /**
     * Where statement.
     *
     * @param where
     *            the where
     * @param db
     *            the db
     * @return the string
     */
    private String whereStatement(String[] where, BaseDBHelper db) {
        if (where == null) {
            return null;
        }
        StringBuffer statement = new StringBuffer();
        for (String whr : where) {
            if (whr.contains(".")) {
                String[] datas = whr.split("\\.");
                String table = datas[0];
                String rel_id = table + "_id";
                String fetch_id = modelToTable(db.getModelName()) + "_id";
                String rel_table = modelToTable(db.getModelName()) + "_"
                        + table + "_rel";
                String subQue = "id in (SELECT " + fetch_id + " FROM "
                        + rel_table + " WHERE " + rel_id + " = ?) ";
                statement.append(subQue);

            } else {
                statement.append(whr);
                statement.append(" ");
            }
        }
        return statement.toString();
    }
    /**
     * Gets the result.
     *
     * @param dbHelper
     *            the db helper
     * @param fetch_columns
     * @param result
     *            the result
     * @return the result
     */
    @SuppressWarnings("unchecked")
    private List<HashMap<String, Object>> getResult(BaseDBHelper dbHelper,
                                                    String[] fetch_columns, Cursor result) {

        List<HashMap<String, Object>> results = null;
        String[] columns = result.getColumnNames();
        if (result.moveToFirst()) {
            results = new ArrayList<HashMap<String, Object>>();
            HashMap<String, Object> row;
            do {
                row = new HashMap<String, Object>();
                for (String col : columns) {
                    String value = result.getString(result.getColumnIndex(col));
                    row.put(col, value);
                }
                List<String> user_columns = null;
                if (fetch_columns != null) {
                    user_columns = Arrays.asList(fetch_columns);
                }

                results.add(row);

            } while (result.moveToNext());
        }
        result.close();
        return results;
    }

    /**
     * Many2One Record Id
     * @param array
     * @return
     */
    public String many2oneRecord(JSONArray array) {
        String id = "";
        try {
            id = array.getString(0).toString();
        } catch (Exception e) {
        }
        return id;
    }
    /**
     * Creates the many2 one table.
     *
     * @param db
     *            the db
     */
    private void createMany2OneTable(BaseDBHelper db) {
        SQLStatement statement = db.createStatement(db);
        db.createTable(statement);
    }

    /**
     * Model to table.
     *
     * @param model
     *            the model
     * @return the string
     */
    public String modelToTable(String model) {
        StringBuffer table = new StringBuffer();
        table.append(model.replaceAll("\\.", "_"));
        return table.toString();
    }

    /**
     * Delete the record with id
     * @param db Database to update
     * @param id Id to be updated
     * @return Boolean Status on operation
     */
    public boolean delete(BaseDBHelper db, int id) {
        try {
            SQLiteDatabase sdb = getWritableDatabase();
            String where = "id = " + id;
            sdb.delete(modelToTable(db.getModelName()), where, null);
            sdb.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete record with where clause
     * @param db Database to be updated
     * @param where Where Clause
     * @param whereArgs Args Clause
     * @return Status on Operation
     */
    public boolean delete(BaseDBHelper db, String where, String[] whereArgs){
        try {
            SQLiteDatabase sdb = getWritableDatabase();
            sdb.delete(modelToTable(db.getModelName()), where, whereArgs);
            sdb.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
