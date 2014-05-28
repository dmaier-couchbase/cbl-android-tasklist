package com.couchbase.demo.lite.app.model;

/**
 * Implements a task
 *
 * Created by david on 27.05.14.
 */
public class Task  {

    /**
     * The user who owns the task
     */
    private String userId;

    /**
     * The title of the task
     */
    private String title;

    /**
     * The creationTime of the task
     */
    private long creationTime;


    /**
     * The full constructor
     *
     * @param title
     * @param creationTime
     */
    public Task(String userId, String title, long creationTime)
    {
        this.userId = userId;
        this.title = title;
        this.creationTime = creationTime;
    }

    /**
     * To get the user id
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * To get the title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * To get the creation time
     * @return
     */
    public long getCreationTime() {
        return creationTime;
    }
}
