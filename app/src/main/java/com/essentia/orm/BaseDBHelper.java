package com.essentia.orm;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by kyawzinlatt94 on 1/30/15.
 */
public class BaseDBHelper extends ORM{
    /** The columns. */
    public ArrayList<Fields> columns = null;

    /** The name. */
    public String name = "";

    /**
     * Instantiates a new base db helper.
     *
     * @param context
     *            the context
     */
    public BaseDBHelper(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.columns = new ArrayList<Fields>();
        this.columns.add(new Fields("id", "Id", Types.integer()));
    }

    /**
     * Gets the columns.
     *
     * @return the columns
     */
    public ArrayList<Fields> getColumns() {
        return this.columns;
    }

    /**
     * Gets the model name.
     *
     * @return the model name
     */
    public String getModelName() {
        return this.name;
    }

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    public String getTableName() {
        return this.name.replaceAll("\\.", "_");
    }
}
