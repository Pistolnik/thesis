#!/bin/bash

set -euo pipefail

source "`dirname \"$0\"`/config.sh"

# dislay error to display error
function bad-usage {
    echo "Usage: $0 full-restart|restart|start|stop|health|stats"
    exit 1
}

if [ "$#" -eq 0 ]; then
    bad-usage
fi

if [ $1 = "full-restart" ]; then
    # stop all containers, delete them, deletes volumes, rebuild images, start new containers
    docker-compose stop && docker-compose down -v && docker-compose build && docker-compose up -d
elif [ $1 == "restart" ]; then
    # same as above without deleting volumes
    docker-compose stop && docker-compose down && docker-compose build && docker-compose up -d
elif [ $1 == "start" ]; then
    # start containers
    docker-compose up -d
elif [ $1 == "stop" ]; then
    # stop containersf
    docker-compose stop
elif [ $1 == "health" ]; then
    # display information about all runing containers
    while :; do clear; docker ps; sleep 2; done
elif [ $1 == "stats" ]; then
    # display different type of information
    docker stats
else
    bad-usage
fi
