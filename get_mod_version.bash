#!/bin/bash
v=`grep -E "Version ?=" src/main/java/monnef/jaffas/food/common/Reference.java | sed -r 's@^.*"(.*)".*;$@\1@'`
echo -n $v
