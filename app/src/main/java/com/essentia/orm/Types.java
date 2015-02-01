package com.essentia.orm;

/**
 * Created by kyawzinlatt94 on 1/30/15.
 * Handles SQLite datatypes required to handle database
 */
public class Types {

    /**
     * Integer.
     *
     * @return the string
     */
    public static String integer() {
        return "INTEGER";
    }

    /**
     * Varchar.
     *
     * @param size
     *            the size
     * @return the string
     */
    public static String varchar(int size) {
        return "VARCHAR(" + String.valueOf(size) + ")";
    }

    /**
     * Text.
     *
     * @return the string
     */
    public static String text() {
        return "TEXT";
    }

    /**
     * Blob.
     *
     * @return the string
     */
    public static String blob() {
        return "BLOB";
    }

    /**
     * many2one.
     *
     * @param model
     *            the model
     * @return the many2 many
     */
    public static Many2Many many2Many(String model) {
        return new Many2Many(model);
    }

    /**
     * Many2 one.
     *
     * @param model
     *            the model
     * @return the many2 one
     */
    public static Many2One many2One(String model) {
        return new Many2One(model);
    }

    /**
     * Many2 many.
     *
     * @param m2mObj
     *            the m2m obj
     * @return the many2 many
     */
    public static Many2Many many2Many(BaseDBHelper m2mObj) {
        // TODO Auto-generated method stub
        return new Many2Many(m2mObj);
    }

    /**
     * Many2 one.
     *
     * @param m2oObj
     *            the m2o obj
     * @return the object
     */
    public static Object many2One(BaseDBHelper m2oObj) {
        // TODO Auto-generated method stub
        return new Many2One(m2oObj);
    }

}
