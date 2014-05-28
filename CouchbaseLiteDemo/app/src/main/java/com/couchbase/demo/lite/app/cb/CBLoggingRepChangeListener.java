package com.couchbase.demo.lite.app.cb;

import android.util.Log;

import com.couchbase.demo.lite.app.MainActivity;
import com.couchbase.demo.lite.app.err.ErrorMessages;
import com.couchbase.lite.replicator.Replication;

/**
 * Just a logging replication change listener.
 *
 * It is used to monitor the replication.
 *
 * Created by david on 26.05.14.
 */
public class CBLoggingRepChangeListener implements Replication.ChangeListener {

    @Override
    public void changed(Replication.ChangeEvent changeEvent) {

        Replication rep = changeEvent.getSource();

        if (!rep.isRunning())
        {
            Log.e(MainActivity.APP_LOG, ErrorMessages.REP_RUN_ERR);
        }
        else
        {
            int count = rep.getCompletedChangesCount();
            int total = rep.getChangesCount();

            Log.i(MainActivity.APP_LOG, count + " of " + total + " replications are finished");
        }

    }
}
