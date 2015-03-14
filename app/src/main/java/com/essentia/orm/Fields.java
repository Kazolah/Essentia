package com.essentia.orm;

/**
 * Created by kyawzinlatt94 on 1/30/15.
 */
public class Fields {
    /** The name. */
    private String name;

    /** The title. */
    private String title;

    /** The type. */
    private Object type;

    /** The help. */
    private String help = "";

    /** The can sync. */
    private boolean canSync = true;

    /**
     * Instantiates a new fields.
     *
     * @param name
     *            the name
     * @param title
     *            the title
     * @param type
     *            the type
     */
    public Fields(String name, String title, Object type) {
        super();
        this.name = name;
        this.title = title;
        this.type = type;
    }

    /**
     * Instantiates a new fields.
     *
     * @param name
     *            the name
     * @param title
     *            the title
     * @param type
     *            the type
     * @param canSync
     *            the can sync
     */
    public Fields(String name, String title, Object type, boolean canSync) {
        super();
        this.name = name;
        this.title = title;
        this.type = type;
        this.canSync = canSync;
    }

    /**
     * Instantiates a new fields.
     *
     * @param name
     *            the name
     * @param title
     *            the title
     * @param type
     *            the type
     * @param canSync
     *            the can sync
     * @param help
     *            the help
     */
    public Fields(String name, String title, Object type, boolean canSync,
                  String help) {
        super();
        this.name = name;
        this.title = title;
        this.type = type;
        this.help = help;
        this.canSync = canSync;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *            the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Object getType() {
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

}
