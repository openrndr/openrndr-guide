#!/bin/bash

./webServerStop.sh

# find id of an existing container
id=$(docker ps -a | grep jekyll | awk '{print $1;}')

if [ "$id" != "" ]; then
  # if found, run the existing container
  docker start -i "$id"
else
  # otherwise create a new one and run it
  docker run -v $(pwd):/srv/jekyll -p 4000:4000 -i jekyll/jekyll:3.8 jekyll serve
fi

