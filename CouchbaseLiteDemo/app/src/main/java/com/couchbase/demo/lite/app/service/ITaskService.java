package com.couchbase.demo.lite.app.service;

import com.couchbase.demo.lite.app.model.Task;

import java.util.List;

/**
 * Created by david on 27.05.14.
 */
public interface ITaskService {

    /**
     * To add a task to the task list
     * @param task
     */
    public void addTask(Task task);

    /**
     * To get a specific task from the task list. Returns null if the task was not existent.
     *
     * @param userId
     * @param creationTime
     */
    public Task getTask(String userId, long creationTime);

    /**
     * To get all tasks of an user
     *
     * @return
     */
    public List<Task> getTaskList(String userId);

}
