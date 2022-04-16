#!/bin/bash

./webServerStop.sh

docker run -v $(pwd):/srv/jekyll \
    -p 4000:4000 \
    -i \
    jekyll/jekyll:latest \
    jekyll serve
