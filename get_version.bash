#!/bin/bash
v=`grep -E "Version ?=" src/main/java/monnef/core/Reference.java | sed -r 's@^.*"(.*)".*;$@\1@'`
echo -n $v
