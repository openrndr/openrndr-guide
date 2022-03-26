#!/bin/bash
docker run -v $(pwd):/srv/jekyll \
    -p 4000:4000 \
    -it \
    jekyll/jekyll:latest \
    jekyll serve
