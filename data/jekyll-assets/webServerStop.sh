#!/bin/bash

id=$(docker container ls | grep jekyll | awk '{print $1;}')

if [ "$id" != "" ]; then
  docker container stop "$id"
fi


