package com.essentia.orm;

/**
 * Created by kyawzinlatt94 on 1/30/15.
 */
public class SQLStatement {
    /** The table_name. */
    private String table_name;

    /** The type. */
    private String type;

    /** The statement. */
    private String statement;

    /**
     * Gets the table_name.
     *
     * @return the table_name
     */
    public String getTable_name() {

        return table_name;
    }

    /**
     * Sets the table_name.
     *
     * @param table_name
     *            the new table_name
     */
    public void setTable_name(String table_name) {

        this.table_name = table_name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {

        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(String type) {

        this.type = type;
    }

    /**
     * Gets the statement.
     *
     * @return the statement
     */
    public String getStatement() {

        return statement;
    }

    /**
     * Sets the statement.
     *
     * @param statement
     *            the new statement
     */
    public void setStatement(String statement) {

        this.statement = statement;
    }
}
