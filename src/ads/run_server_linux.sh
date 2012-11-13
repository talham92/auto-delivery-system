#!/bin/bash

kill -9 `ps -ef | grep rmiregistry | awk '{ print $2 }'`
rmiregistry &

java -Djava.rmi.server.codebase=file:`pwd`/ads.jar -jar ads.jar server
