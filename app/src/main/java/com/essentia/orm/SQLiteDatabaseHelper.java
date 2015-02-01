package com.essentia.orm;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kyawzinlatt94 on 1/30/15.
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper{

    /** The database version. */
    public static int DATABASE_VERSION = 1;

    /** The database name. */
    public static String DATABASE_NAME = "EssentiaSQLite";

    /** The system tables. */
    ArrayList<HashMap<String, String>> systemTables;

    /** The context. */
    Context context = null;

    /** The statements. */
    ArrayList<SQLStatement> statements = null;

    public SQLiteDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Sets the system tables.
     *
     * @param systemTables
     *            the system tables
     */
    public void setSystemTables(ArrayList<HashMap<String, String>> systemTables) {
        this.systemTables = systemTables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    /**
     * Creates the table.
     *
     * @param statement
     *            the statement
     */
    public void createTable(SQLStatement statement) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(statement.getStatement());
        db.close();
    }
}
