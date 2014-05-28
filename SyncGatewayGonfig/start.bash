#!/bin/bash

## Environment
# The installation root of the Sync Gateway
export SYNC_GATEWAY_HOME=/opt/couchbase-sync-gateway

## Alt. Start
# An alternative way to start the service, by using parameters
#nohup $SYNC_GATEWAY_HOME/bin/sync_gateway -url http://localhost:8091 1> /tmp/sync_gateway.out 2>> /tmp/sync_gateway.out &

## Start
# Starts the Sync Gateway in the background by using a configuration file and by redirecting the output to a temp. log file
nohup $SYNC_GATEWAY_HOME/bin/sync_gateway $SYNC_GATEWAY_HOME/cfg/config.json 1> /tmp/sync_gateway.out 2>> /tmp/sync_gateway.out &
