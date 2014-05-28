package com.couchbase.demo.lite.app.cb;

import com.couchbase.lite.replicator.Replication;

/**
 * Just a structure to access both Couchbase replications together
 *
 * Created by david on 23.05.14.
 */
public class CBReplications {

    /**
     * The possible replication types
     */
    public enum ReplicationType{PULL, PUSH, BOTH};

    /**
     * The push replication
     */
    private Replication push;

    /**
     * The pull replication
     */
    private Replication pull;

    /**
     * The default constructor
     *
     * @param push
     * @param pull
     */
    CBReplications(Replication push, Replication pull)
    {
        this.pull = pull;
        this.push = push;
    }

    /**
     * To get the pull replication
     *
     * @return
     */
    public Replication getPull() {
        return pull;
    }

    /**
     * To get the push replication
     *
     * @return
     */
    public Replication getPush() {
        return push;
    }

    /**
     * To check if the push replication is used
     *
     * @return
     */
    boolean isPush()
    {
        if (push != null)
            return true;

        return false;
    }

    /**
     * To check if the pull replication is used
     *
     * @return
     */
    boolean isPull()
    {
        if (pull != null)
            return true;

        return false;
    }


    /**
     * To start both replications
     */
    public void start()
    {
        if (isPull()) pull.start();
        if (isPush()) push.start();
    }
}
