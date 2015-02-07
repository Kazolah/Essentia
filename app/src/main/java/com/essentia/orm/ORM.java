package com.essentia.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
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
            if (field.getType() instanceof Many2One) {
                if (((Many2One) field.getType()).isM2OObject()) {
                    BaseDBHelper m2oDb = ((Many2One) field.getType())
                            .getM2OObject();
                    createMany2OneTable(m2oDb);

                }
                type = Types.integer();
            }

            try {
                create.append(field.getName());
                create.append(" ");
                create.append(type.toString());
                create.append(", ");
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
    public void create(BaseDBHelper dbHelper, ContentValues data_values){
        ContentValues values = new ContentValues();

        for (Fields field : dbHelper.getColumns()) {
            values.put(field.getName(),
                    data_values.getAsString(field.getName()));
        }

        // Handling Many2One Record
        HashMap<String, Object> many2onecols = dbHelper.getMany2OneColumns();
        for (String key : many2onecols.keySet()) {
            try {
                if (!values.getAsString(key).equals("false")) {
                    JSONArray m2oArray = new JSONArray(values.getAsString(key));
                    values.put(key, many2oneRecord(m2oArray));
                }
            } catch (Exception e) {
                Log.d("ORM Create","Many2One Record");
            }
        }
        SQLiteDatabase db = getWritableDatabase();
        db.insert(modelToTable(dbHelper.getModelName()), null, values);
        db.close();
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
}
