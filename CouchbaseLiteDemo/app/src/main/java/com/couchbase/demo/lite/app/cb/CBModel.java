package com.couchbase.demo.lite.app.cb;

/**
 * Created by david on 22.05.14.
 */
public interface CBModel {

    public static final String TASKLIST_PREFIX = "tasklist";
    public static final String TASKLIST_KEY = TASKLIST_PREFIX + "_{1}";
    public static final String TASK_PREFIX = "task";
    public static final String TASK_KEY = TASK_PREFIX + "_{1}_{2}";

    public static final String TASKS_PROP = "tasks";

    public static final String TITLE_PROP = "title";
    public static final String CREATED_PROP = "created";
    public static final String USER_PROP = "user";

    public static final String CHANNELS_PROP = "channels";
    public static final String[] CHANNEL_LIST = new String[]{"public"};
}
