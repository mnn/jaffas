#!/bin/bash

find ./ -type f -name "*.java" -exec sed -i '/package /,$!d' '{}' \;
