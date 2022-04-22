#!/bin/bash

./webServerStop.sh

name=www-openrndr-guide

docker start -ai $name || \
  docker run --name=$name -v $(pwd):/srv/jekyll -p 4000:4000 -i jekyll/jekyll:latest jekyll serve

