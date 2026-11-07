#!/bin/bash

# 1. In one terminal window run:
# ./gradlew dokgen -t
# That build keeps running and waiting for changes to regenerate markdown files.

# 2. Run this script from the guide root directory:
# scripts/webServerStart.sh
# The script will serve the website in http://127.0.0.1:1111,
# it will rebuild and reload the website on changes.

# It requires Zola 0.23 or newer:
# https://www.getzola.org/documentation/getting-started/installation/
# Set the ZOLA environment variable if `zola` is not in your PATH.

cd build/dokgen/zola/ || exit
"${ZOLA:-zola}" serve --open
