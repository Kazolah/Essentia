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
}
