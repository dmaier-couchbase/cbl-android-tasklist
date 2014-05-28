package com.couchbase.demo.lite.app;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.couchbase.demo.lite.app.android.AndroidTable;
import com.couchbase.demo.lite.app.cb.CBHelper;
import com.couchbase.demo.lite.app.cb.CBModel;
import com.couchbase.demo.lite.app.cb.CBReplications;
import com.couchbase.demo.lite.app.model.Task;
import com.couchbase.demo.lite.app.service.CBTaskService;
import com.couchbase.demo.lite.app.service.ITaskService;
import com.couchbase.lite.Database;
import com.couchbase.lite.Revision;
import com.couchbase.lite.ValidationContext;
import com.couchbase.lite.Validator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The main entry point of our Android application
 *
 * The application allows the following:
 *
 *  - Add new tasks via a text input
 *  - Show a simple task list
 *  - Store the tasks objects in the database
 *  - Replicate the task objects via the SyncGateway
 *
 *  The demo covers the following subjects
 *
 *  - Create a local database
 *  - Add data to the local database
 *  - Get data from the local database
 *  - Update properties of documents
 *  - Setup the replication by connecting to the sync-gateway
 *  - Database Change Listener
 *  - Replication Change Listener
 *  - Database Validation
 *
 *  Created by david on 23.05.14.
 */
public class MainActivity extends ActionBarActivity {

    //-- Constants

    /**
     * A global logger name
     */
    public static final String APP_LOG = MainActivity.class.getName();

    /**
     * The database name to connect to
     */
    public static final String DB_NAME = "cb_tl_2";

    /**
     * The user id to work with, in the real world this should be a variable which is set by a
     * log-in procedure. For demoing purposes it is just a constant.
     */
    public static final String USER_ID = "me";


    /**
     * The Replication endpoint which should be used by the application
     */
    public static final String REPLICATION_ENDPOINT = "http://192.168.7.128:4984/sync_gateway";


    //-- Members

    /**
     * The application context
     */
    private static Context ctx;

    /**
     * The database which is used by the application
     */
    private  Database db;


    /**
     * The task service
     */
    private ITaskService taskService;

    /**
     * The replications those should be used by the application
     */
    private CBReplications replications;


    //-- Constructors

    /**
     * The default constructor just makes the
     * To make the application context globally available
     */
    public MainActivity()
    {
        super();
        ctx = this;
    }

    //-- App logic

    /**
     * To override the initialization of the application
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect to the database
        db = CBHelper.connect(DB_NAME);

        //Init the database change listener
        initDBChangeListener();

        //Init the task service wrapper
        taskService = new CBTaskService(db);

        //Setup the replications
        replications = CBHelper.setupReplication(db, REPLICATION_ENDPOINT, CBReplications.ReplicationType.BOTH);
        replications.start();

        //Init the UI parts
        initTaskList();
        initAddTaskButton();
    }


    /**
     * To get the application's context
     *
     * @return
     */
    public static Context getCtx()
    {
        return ctx;
    }


    /**
     * To reset the task list based on the values in the database
     */
    private void initTaskList()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                GridView taskListGrid = (GridView) findViewById(R.id.gridview_entries);

                List<Task> tasks = taskService.getTaskList(USER_ID);

                AndroidTable table = new AndroidTable();

                 for (Task task : tasks)
                 {
                    Map<String, Object> row = new HashMap<String, Object>();
                    row.put(CBModel.TITLE_PROP, task.getTitle());

                    DateFormat formatDate = new SimpleDateFormat();
                    String formCreated = formatDate.format(new Date(task.getCreationTime()));
                    row.put(CBModel.CREATED_PROP, formCreated);

                     table.addRow(row);
                  }

                  String[] from = new String[]{CBModel.TITLE_PROP, CBModel.CREATED_PROP};
                  int[] to = new int[]{R.id.value1, R.id.value2};

                  //Would be better to implement a specific Adapter here
                  SimpleAdapter dataAdapter = new SimpleAdapter(ctx,table.getRows(),R.layout.two_value_entry,from,to);
                  taskListGrid.setAdapter(dataAdapter);
             }
        });
    }

    /**
     * To add some activity to the button
     */
    private void initAddTaskButton()
    {
        final Button addTaskButton = (Button) findViewById(R.id.button_addtask);
        final EditText addTaskText = (EditText) findViewById(R.id.edittext_addtask);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = addTaskText.getText().toString();
                long now = new Date().getTime();

                Task task = new Task(USER_ID, title, now);

                taskService.addTask(task);

            }
        });

    }

    /**
     * Init the database change listener
     *
     */
    private void initDBChangeListener()
    {
        db.addChangeListener( new Database.ChangeListener() {
            @Override
            public void changed(Database.ChangeEvent changeEvent) {

                //Just reset the the task list
                //BTW: In the real world we would work directly with the Adapter of grid view,
                //but in this example we are just refreshing the whole list
                initTaskList();
            }
        });
    }

    /**
     * Just an example validator, currently not really used
     */
    private void initDBValidation()
    {
        db.setValidation("isDeleted", new Validator() {
            @Override
            public void validate(Revision revision, ValidationContext validationContext) {

                if (revision.isDeletion())
                   validationContext.reject("Deletion is not yet implemented. Why was there any deletion?");
            }
        });
    }



    //-- Options Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
