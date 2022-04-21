#!/bin/bash

# find id of running jekyll container
id=$(docker container ls | grep jekyll | awk '{print $1;}')

if [ "$id" != "" ]; then
  # if found, stop it
  docker container stop "$id"
fi


