#!/bin/bash

kill -9 `ps -ef | grep rmiregistry | awk '{ print $2 }'`
rmiregistry &

java -Djava.rmi.server.codebase=file:/cac/u01/mgamell/Dropbox/classes/software_engineering/demo1/ads/dist/ads.jar -jar ads.jar server
