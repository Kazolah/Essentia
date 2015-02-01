package com.essentia.orm;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

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
        this.columns
                .add(new Fields("oea_name", "OpenERP User", Types.varchar(100),
                        false,
                        "OpenERP Account manager name used for login and filter multiple accounts."));
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
     * Gets the server columns.
     *
     * @return the server columns
     */
    public ArrayList<Fields> getServerColumns() {
        ArrayList<Fields> serverCols = new ArrayList<Fields>();
        for (Fields fields : this.columns) {
            if (fields.isCanSync()) {
                serverCols.add(fields);
            }
        }
        return serverCols;
    }

    /**
     * Gets the many2 many columns.
     *
     * @return the many2 many columns
     */
    public HashMap<String, Object> getMany2ManyColumns() {
        HashMap<String, Object> list = new HashMap<String, Object>();
        for (Fields field : this.columns) {
            if (field.getType() instanceof Many2Many) {
                list.put(field.getName(), field.getType());
            }
        }
        return list;
    }

    /**
     * Gets the many2 one columns.
     *
     * @return the many2 one columns
     */
    public HashMap<String, Object> getMany2OneColumns() {
        HashMap<String, Object> list = new HashMap<String, Object>();
        for (Fields field : this.columns) {
            if (field.getType() instanceof Many2One) {
                list.put(field.getName(), field.getType());
            }
        }
        return list;
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
