#!/bin/bash

kill -9 `ps -ef | grep rmiregistry | awk '{ print $2 }'`
rmiregistry &

java -jar ads.jar