#!/bin/bash

output=jar_output
outtmp=$output/tmp

mkdir $output &>/dev/null
mkdir $outtmp &>/dev/null

od=`pwd`

rm -fr "$outtmp/*"
touch "$outtmp/.placeholder"

#cp jar_data/* "$outtmp/"
cp eclipse/Minecraft/bin/jaffas_01.png eclipse/Minecraft/bin/jaffas_logo.png "$outtmp"
cp -r reobf/minecraft/* "$outtmp"

cd "$outtmp"
zip -9r "../mod_jaffas.jar" ./*

cd "$od"
