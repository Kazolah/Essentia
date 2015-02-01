package com.essentia.orm;

/**
 * Created by kyawzinlatt94 on 1/30/15.
 */
public class Many2One {

    /** The model_name. */
    private String model_name = null;

    /** The m2o object. */
    private BaseDBHelper m2oObject = null;

    /**
     * Instantiates a new many2 one.
     *
     * @param model_name
     *            the model_name
     */
    public Many2One(String model_name) {
        super();
        this.model_name = model_name;
    }

    /**
     * Gets the model name.
     *
     * @return the model name
     */
    public String getModelName() {
        return model_name;
    }

    /**
     * Instantiates a new many2 one.
     *
     * @param m2oObject
     *            the m2o object
     */
    public Many2One(BaseDBHelper m2oObject) {
        super();
        this.m2oObject = m2oObject;
    }

    /**
     * Checks if is m2 o object.
     *
     * @return true, if is m2 o object
     */
    public boolean isM2OObject() {
        if (this.m2oObject != null) {
            return true;
        }
        return false;
    }

    /**
     * Gets the m2 o object.
     *
     * @return the m2 o object
     */
    public BaseDBHelper getM2OObject() {
        return m2oObject;
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
