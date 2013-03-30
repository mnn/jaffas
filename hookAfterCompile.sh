#!/bin/bash

ed="eclipse/Minecraft/bin"
bd="bin_data"
args="-d --preserve=all -f -r"
cargs=""$args" "$bd/*" "$ed/""
cp $cargs

bd="bin_data_forge"
cargs=""$args" "$bd/*" "$ed/""
cp $cargs
