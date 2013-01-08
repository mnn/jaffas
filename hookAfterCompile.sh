#!/bin/bash

ed="eclipse/Minecraft/bin"
bd="bin_data"
args="-d --preserve=all -f"
cargs=""$args" "$bd/*" "$ed/""

cp $cargs
