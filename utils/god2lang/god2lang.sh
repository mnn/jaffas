#!/bin/bash

cat gameObjects.csv | grep jaffas | awk -F, '{gsub(/(^\")|(\"$)/,"",$1); gsub(/(^\")|(\"$)/,"",$2); print $1 ".name=" $2}' | uniq > gameObjects.lang
