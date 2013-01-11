#!/bin/bash

output=jar_output
outtmp=$output/tmp
dist=$output/dist

echo -n Preparing...

mkdir $output &>/dev/null
mkdir $outtmp &>/dev/null
mkdir $dist &>/dev/null

od=`pwd`

rm -fr "$outtmp/*"
rm -fr "$dist/*"
touch "$outtmp/.placeholder"

echo Done
echo -n Parsing version...

ver_line=`grep -Ei 'String +Version' src/common/monnef/core/Version.java`
if [ $? -ne 0 ]; then
	echo "Cannot determine version"
	exit
fi

version=`sed -r 's/^.*"(.*)".*;$/\1/' <<< "$ver_line"`
if [ $? -ne 0 ]; then
	echo "Cannot parse version"
	exit
fi

echo Done
echo "Version detected: [$version]"
#sleep 0.3

echo -n Copying mod files...

cp eclipse/Minecraft/bin/jaffas_0{1,2,3,4}.png eclipse/Minecraft/bin/jaffas_logo.png eclipse/Minecraft/bin/guifridge.png eclipse/Minecraft/bin/jaffabrn1.png eclipse/Minecraft/bin/mcmod.info eclipse/Minecraft/bin/guicollector.png eclipse/Minecraft/bin/sharpener.wav eclipse/Minecraft/bin/suck.wav "$outtmp"
cp eclipse/Minecraft/bin/jaffas_present_{0,1,2,3,4,5}.png eclipse/Minecraft/bin/jaffas_paintings_01.png eclipse/Minecraft/bin/jaffas_candy.png eclipse/Minecraft/bin/jaffas_faucet.png "$outtmp"
cp -r reobf/minecraft/{buildcraft,monnef,forestry} "$outtmp"

outName="mod_jaffas_$version"

cd "$outtmp"
zip -q -9r "../$outName.jar" ./*

cd "$od"

echo Done
#jar packing done

#prepare libs - jsoup
echo -n Copying libraries...
mkdir $dist/mods &>/dev/null
#mkdir $dist/lib &>/dev/null
cp "$output/$outName.jar" "$dist/mods"
cp "lib/jsoup-1.7.1.jar" "$dist/mods"
cp "lib/Jsoup_license.txt" "$dist/mods"
echo Done
echo -n Packing...

cd $dist
zip -q -9r "../${outName}_packed.zip" ./*

echo Done
