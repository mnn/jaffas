#!/bin/bash

output=jar_output
outtmp=$output/tmp
dist=$output/dist
binPath=bin_data

echo -n Preparing...

mkdir $output &>/dev/null
mkdir $outtmp &>/dev/null
mkdir $dist &>/dev/null

od=`pwd`

if [ ${#output} -lt 5 ]; then
	echo "weird string in output directory variable. halting for safety reasons."
	exit 1
fi

rm -fr "./$outtmp/*"
rm -fr "./$dist/*"
rm -fr "./$output/*"
touch "$outtmp/.placeholder"

echo Done
echo -n Parsing version...

ver_line=`grep -Ei 'String +Version' src/minecraft/monnef/jaffas/food/Reference.java`
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

#cp $binPath/jaffas_0{1,2,3,4}.png $binPath/jaffas_logo.png $binPath/guifridge.png $binPath/jaffabrn1.png $binPath/mcmod.info $binPath/guicollector.png $binPath/sharpener.wav $binPath/suck.wav "$outtmp"
#cp $binPath/jaffas_present_{0,1,2,3,4,5}.png $binPath/jaffas_paintings_01.png $binPath/jaffas_candy.png $binPath/jaffas_faucet.png "$outtmp"
cp $binPath/* "$outtmp"
cp -r reobf/minecraft/{buildcraft,monnef,forestry,extrabiomes} "$outtmp"

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
