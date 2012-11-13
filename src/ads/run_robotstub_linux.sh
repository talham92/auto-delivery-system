#!/bin/bash

java -Djava.rmi.server.codebase=file:`pwd`/ads.jar -jar ads.jar stub
