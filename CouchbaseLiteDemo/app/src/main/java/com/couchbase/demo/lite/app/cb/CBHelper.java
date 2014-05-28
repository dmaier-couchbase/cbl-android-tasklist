package com.couchbase.demo.lite.app.cb;

import android.util.Log;

import com.couchbase.demo.lite.app.MainActivity;
import com.couchbase.demo.lite.app.android.AndroidError;
import com.couchbase.demo.lite.app.err.ErrorMessages;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.replicator.Replication;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 23.05.14.
 */
public class CBHelper {

    //-- Database

    /**
     * Connect to a database by creating it if it is not yet existent
     *
     * @param dbName
     * @return
     */
    public static Database connect(String dbName)
    {
        Database result = null;

        Manager cbManager = null;

        try {

            cbManager = new Manager(MainActivity.getCtx().getFilesDir(), Manager.DEFAULT_OPTIONS);

            boolean validDBName = cbManager.isValidDatabaseName(dbName);

            if (validDBName)

                result = cbManager.getDatabase(dbName);

            else

                throw new Exception("The database name " + dbName + " is not valid");

        } catch (Exception e) {

            AndroidError.show(ErrorMessages.CONNECT_DB_ERR);
            Log.e(MainActivity.APP_LOG, ErrorMessages.CONNECT_DB_ERR + " : " + e.getMessage());
        }


        return result;
    }

    //-- Keys

    /**
     * To generate the key based on a template and a list of values
     *
     * @param tmpl
     * @param values
     * @return
     */
    public static String genKey(String tmpl, String[] values)
    {
        String result = tmpl;

        int i = 0;

        for (String value : values)
        {
            final String TO_REPLACE =  "{" + (i+1) + "}";

            result = result.replace(TO_REPLACE, value);

            i++;
        }

        return result;
    }


    /**
     * To generate the key based on a template and a value
     *
     * @param tmpl
     * @param value
     * @return
     */
    public static String genKey(String tmpl, String value)
    {
        return genKey(tmpl, new String[]{value});
    }

    //-- Documents

    /**
     * To create an empty document with a specific key.
     *
     * Returns null if the document was already existent
     *
     * @param db
     * @param key
     * @return
     */
    public static Document add(Database db, String key)
    {

        Map<String, Object> props = new HashMap<String, Object>();

        return add(db, key, props);

    }

    /**
     * To create a new document with the specific properties
     *
     * Returns null if the document was already existent
     *
     * @param db
     * @param key
     * @return
     */
    public static Document add(Database db, String key, Map<String, Object> props)
    {
        Document doc = db.getExistingDocument(key);

        if (doc == null)
        {
            try
            {
                doc = db.getDocument(key);
                doc.putProperties(props);
                return doc;

            }
            catch (Exception e)
            {
                AndroidError.show(ErrorMessages.ADD_ERR);
                Log.e(MainActivity.APP_LOG, ErrorMessages.ADD_ERR + " :" + e.getMessage());
            }


        }

        return null;
    }

    /**
     * Replaces an existing document.
     *
     * Returns null if the document was not existent.
     *
     * @return
     */
    public static SavedRevision replace(Database db, String key, HashMap<String, Object> props)
    {
        SavedRevision result = null;

        Document doc = db.getExistingDocument(key);


        if (doc != null)
        {
            try {

                result = doc.putProperties(props);

            } catch (CouchbaseLiteException e) {

                AndroidError.show(ErrorMessages.REPLACE_ERR);
                Log.e(MainActivity.APP_LOG, ErrorMessages.REPLACE_ERR + " :" + e.getMessage());
            }
        }

        return result;
    }

    /**
     * Checks if a document with the given key is already existent
     *
     * @param db
     * @param key
     * @return
     */
    public static boolean exists(Database db, String key)
    {
        Document doc = db.getExistingDocument(key);

        if (doc != null)
            return true;
        else
            return false;

    }

    /**
     * Updates only the properties those are given by keeping the old ones
     * @param doc
     * @param props
     * @return
     */
    public static SavedRevision updateProperties(Document doc, Map<String, Object> props)
    {
        SavedRevision rev = null;

        try {

            Map<String, Object> oldProps = doc.getProperties();

            Map<String, Object> newProps = new HashMap<String, Object>();
            newProps.putAll(oldProps);

            for (Map.Entry e : props.entrySet())
            {
                newProps.put((String) e.getKey(), e.getValue());
            }

            rev = doc.putProperties(newProps);

        } catch (CouchbaseLiteException e) {

            AndroidError.show(ErrorMessages.UPDATE_ERR);
            Log.e(MainActivity.APP_LOG, ErrorMessages.UPDATE_ERR + " :" + e.getMessage());
        }

        return rev;
    }

    //-- Replication

    /**
     * Sets the channels property
     *
     * @param doc
     * @param channels
     */
    public static void setChannels(Document doc, List<String> channels)
    {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(CBModel.CHANNELS_PROP, channels);

        updateProperties(doc, props);
    }

    /**
     * To get a channel list from an array of channels
     *
     * @param channels
     * @return
     */
    public static List<String> toChannelList(String[] channels)
    {
        List<String> result = new ArrayList<String>();


        for (String channel : channels)
        {
            result.add(channel);
        }

        return result;
    }


    /**
     * To set up the replications to an endpoint
     *
     * @param db
     * @param endpointURL
     * @param type
     * @return
     */
    public static CBReplications setupReplication(Database db, String endpointURL, CBReplications.ReplicationType type)
    {
        Replication push = null;
        Replication pull = null;

        //Define a replication change lister, in this case just for Logging purposes
        Replication.ChangeListener repChangeListener = new CBLoggingRepChangeListener();

        try
        {

            URL url = new URL(endpointURL);

            if (type == CBReplications.ReplicationType.BOTH || type == CBReplications.ReplicationType.PULL)
            {
                pull = db.createPullReplication(url);
                pull.setContinuous(true);
                pull.addChangeListener(repChangeListener);
            }

            if (type == CBReplications.ReplicationType.BOTH || type == CBReplications.ReplicationType.PUSH)
            {
                push = db.createPushReplication(url);
                push.setContinuous(true);
                push.addChangeListener(repChangeListener);
            }


        }
        catch (Exception e)
        {
            AndroidError.show(ErrorMessages.SETUP_REPLICATION_ERR);
            Log.e(MainActivity.APP_LOG, ErrorMessages.SETUP_REPLICATION_ERR + " :" + e.getMessage());
        }

        return new CBReplications(push, pull);
    }

}
