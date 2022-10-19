#!/bin/bash

# docker / jekyll was not working in my laptop because ruby/bundle was using
# ipv6 and failing to download dependencies. I disabled ipv6 by creating
# a /etc/sysctl.d/40-ipv6.conf file to disable ipv6 per interface.

./webServerStop.sh

name=www-openrndr-guide

docker start -ai $name || \
  docker run --name=$name -v $(pwd):/srv/jekyll -p 4000:4000 -i jekyll/jekyll:4.2.0 jekyll serve

