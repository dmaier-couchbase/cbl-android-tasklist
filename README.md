Couchbase Lite - Android Tasklist Example
=========================================

A simple tasklist demo application how to use 'Couchbase Lite -  Android' together with the 'Sync Gateway'.

The mobile application allows you to:

- Add new tasks via a text input
- Show a simple task list
- Store the tasks objects in the database
- Replicate the task objects via the Sync Gateway to a Couchbase Server Cluster

The application covers the following subjects:

- Create a local database
- Add data to the local database
- Get data from the local database
- Update properties of documents
- Setup a bidirectional replication by connecting to the Sync Gateway
- Database Change Listener
- Replication Change Listener
- Database Validation

The demo shows the following:

- Explain the source code
- Show the Sync Gateway configuration
- Start the Sync Gateway by connecting to a Couchbase Server cluster
- Start the application twice (by using 2 different Android emulator instances)
- Enter a new task in Android Device #1 and see that it is syncronized one second later to Device #2
- Look at the synchronized data on side of the Couchbase Server and see that that the Sync Gateway added some administrative sync. meta data to your JSON documents

![alt tag](https://raw.github.com/dmaier-couchbase/cbl-android-tasklist/master/Screenshots/CouchbaseLiteAndroidDemoScreenshot.png)
