#!/bin/bash

output=jar_output
outtmp=$output/tmp

mkdir $output &>/dev/null
mkdir $outtmp &>/dev/null

od=`pwd`

rm -fr "$outtmp/*"
touch "$outtmp/.placeholder"

version=`grep -Ei '@Mod\(' src/common/monnef/jaffas/food/mod_jaffas.java  | sed -r 's/^.*( ?version ?= ?"(.?*)").*$/\2/'`
#TODO check for failure in version detection

cp eclipse/Minecraft/bin/jaffas_01.png eclipse/Minecraft/bin/jaffas_02.png eclipse/Minecraft/bin/jaffas_logo.png eclipse/Minecraft/bin/guifridge.png eclipse/Minecraft/bin/jaffabrn1.png eclipse/Minecraft/bin/mcmod.info "$outtmp"
cp -r reobf/minecraft/* "$outtmp"

cd "$outtmp"
zip -9r "../mod_jaffas_$version.jar" ./*

cd "$od"
