package com.couchbase.demo.lite.app.service;

import com.couchbase.demo.lite.app.cb.CBHelper;
import com.couchbase.demo.lite.app.cb.CBModel;
import com.couchbase.demo.lite.app.model.Task;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 27.05.14.
 */
public class CBTaskService implements ITaskService {

    /**
     * The database which should be used by the service
     */
    private Database db;

    /**
     * The constructor of the service
     */
    public CBTaskService(Database db)
    {
       this.db = db;
    }


    @Override
    public void addTask(Task task) {

        //Add the task object
        String taskKey = CBHelper.genKey(CBModel.TASK_KEY,new String[]{task.getUserId(), Long.toString(task.getCreationTime())});

        Map<String, Object> props = new HashMap<String, Object>();
        props.put(CBModel.TITLE_PROP, task.getTitle());
        props.put(CBModel.CREATED_PROP, Long.toString(task.getCreationTime()));
        props.put(CBModel.USER_PROP, task.getUserId());
        props.put(CBModel.CHANNELS_PROP, CBHelper.toChannelList(CBModel.CHANNEL_LIST));

        CBHelper.add(db, taskKey, props);

        //Update the channel list
        String taskListKey = CBHelper.genKey(CBModel.TASKLIST_KEY, task.getUserId());
        Document taskList = db.getDocument(taskListKey);

        List<String> tasks = (List<String>) taskList.getProperty(CBModel.TASKS_PROP);
        tasks.add(taskKey);
        Map<String, Object> updatedProps = new HashMap<String, Object>();
        updatedProps.put(CBModel.TASKS_PROP, tasks);
        CBHelper.updateProperties(taskList, updatedProps );
    }

    @Override
    public Task getTask(String userId, long creationTime) {

        Task result = null;

        String taskKey = CBHelper.genKey(CBModel.TASK_KEY,new String[]{userId, Long.toString(creationTime)});
        Document taskDoc = db.getExistingDocument(taskKey);

        if (taskDoc != null)
        {
            String title = (String) taskDoc.getProperty(CBModel.TITLE_PROP);

            result = new Task(userId,title, creationTime);
        }

        return result;
    }


    @Override
    public List<Task> getTaskList(String userId) {

        List<Task> result = new ArrayList<Task>();

        String taskListKey = CBHelper.genKey(CBModel.TASKLIST_KEY, userId);

        Document taskListDoc = null;

        // Create an empty one if not yet existent
        if (!CBHelper.exists(db, taskListKey))
        {
            List<String> tasks  = new ArrayList<String>();
            Map<String, Object> props = new HashMap<String, Object>();
            props.put(CBModel.TASKS_PROP, tasks);
            props.put(CBModel.CHANNELS_PROP, CBHelper.toChannelList(new String[]{"public"}));
            taskListDoc = CBHelper.add(db, taskListKey, props);
        }
        else
        {
            taskListDoc = db.getExistingDocument(taskListKey);
        }


        //Iterate over the tasks by adding them to the Grid-View
        List<String> taskKeys = (List<String>) taskListDoc.getProperty(CBModel.TASKS_PROP);

        for (String taskKey : taskKeys)
        {
            Document taskDoc = db.getDocument(taskKey);

            String title = taskDoc.getProperty(CBModel.TITLE_PROP).toString();
            long creationTime = Long.parseLong(taskDoc.getProperty(CBModel.CREATED_PROP).toString());

            Task task = new Task(userId, title, creationTime);

            result.add(task);
        }

        return result;
    }




}
