package com.essentia.orm;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kyawzinlatt94 on 1/30/15.
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
            if (field.getType() instanceof Many2Many) {
                handleMany2ManyCol(moduleDBHelper, field);
                continue;
            }
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
     * Handles many2many column.
     *
     * If many2many column contain only model name than it will create only m2m
     * related table. If many2many column contain BaseDBHelper object than it
     * will first create master table than create related table.
     *
     * @param db
     *            the db
     * @param field
     *            the field
     */
    public void handleMany2ManyCol(BaseDBHelper db, Fields field) {
        List<Fields> cols = new ArrayList<Fields>();

        // Handle many2many object
        if (field.getType() instanceof Many2Many) {
            Many2Many m2mobj = (Many2Many) field.getType();

            if (m2mobj.isM2MObject()) {
                BaseDBHelper newDb = null;
                newDb = (BaseDBHelper) m2mobj.getM2mObject();

                SQLStatement statement = newDb.createStatement(newDb);
                newDb.createTable(statement);

                Fields dField = new Fields(field.getName(), field.getTitle(),
                        Types.many2Many(newDb.getModelName()));

                newDb.handleMany2ManyCol(db, dField);
            } else {
                // handle many2many model
                String model = m2mobj.getModel_name();
                String rel_table = modelToTable(db.getModelName()) + "_"
                        + modelToTable(model) + "_rel";
                String tab1 = modelToTable(db.getModelName());
                String tab2 = modelToTable(model);
                String tab1_col = tab1 + "_id";
                String tab2_col = tab2 + "_id";
                String common_col = "oea_name";
                cols.add(new Fields(tab1_col, tab1_col, Types.integer()));
                cols.add(new Fields(tab2_col, tab2_col, Types.integer()));
                cols.add(new Fields(common_col, "Android Name", Types.text()));
                SQLStatement many2ManyTable = createStatement(rel_table, cols);
                this.createTable(many2ManyTable);
            }

        }
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
