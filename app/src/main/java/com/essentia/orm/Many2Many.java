package com.essentia.orm;

/**
 * Created by kyawzinlatt94 on 1/30/15.
 */
public class Many2Many {
    /** The model_name. */
    private String model_name = null;

    /** The m2m object. */
    private BaseDBHelper m2mObject = null;

    /**
     * Instantiates a new many2 many.
     *
     * @param model
     *            the model
     */
    public Many2Many(String model) {
        // TODO Auto-generated constructor stub
        this.model_name = model;
    }

    /**
     * Instantiates a new many2 many.
     *
     * @param obj
     *            the obj
     */
    public Many2Many(BaseDBHelper obj) {
        // TODO Auto-generated constructor stub
        this.m2mObject = obj;
    }

    /**
     * Gets the model_name.
     *
     * @return the model_name
     */
    public String getModel_name() {
        return model_name;
    }

    /**
     * Gets the m2m object.
     *
     * @return the m2m object
     */
    public BaseDBHelper getM2mObject() {
        return m2mObject;
    }

    /**
     * Checks if is m2 m object.
     *
     * @return true, if is m2 m object
     */
    public boolean isM2MObject() {
        if (this.m2mObject != null) {
            return true;
        }
        return false;
    }

    /**
     * Checks if is modle name.
     *
     * @return true, if is modle name
     */
    public boolean isModleName() {
        if (this.model_name != null) {
            return true;
        }
        return false;
    }
}
