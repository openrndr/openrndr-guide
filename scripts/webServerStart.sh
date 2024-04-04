#!/bin/bash

# 1. In one terminal window run:
# ./gradlew dokgen -t
# That build keeps running and waiting for changes to regenerate markdown files.

# 2. Run this script from the guide root directory:
# scripts/webServerStart.sh
# The script will server the website in http://localhost:4000,
# it will rebuild and reload the website on changes.

# It requires Ruby and Bundler:
# Install Ruby which comes with gem, then use gem to install bundler.

cd build/dokgen/jekyll/docs/ || exit
bundle exec jekyll serve -d /tmp/guide --livereload --watch --disable-disk-cache
