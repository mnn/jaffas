#!/bin/bash

output=jar_output
outtmp=$output/tmp
core=$output/core
binPath=bin_data

echo -n Preparing...

mkdir $output &>/dev/null
mkdir $outtmp &>/dev/null
mkdir $core &>/dev/null

od=`pwd`

if [ ${#output} -lt 5 ]; then
	echo "weird string in output directory variable. halting for safety reasons."
	exit 1
fi

rm -fr "./$outtmp/*"
rm -fr "./$output/*"
rm -fr "./$core/*"
touch "$outtmp/.placeholder"

echo Done
echo -n Parsing version...

ver_line=`grep -Ei 'String +Version' src/minecraft/monnef/core/Reference.java`
if [ $? -ne 0 ]; then
	echo "Cannot determine version"
	exit 2
fi

version=`sed -r 's/^.*"(.*)".*;$/\1/' <<< "$ver_line"`
if [ $? -ne 0 ]; then
	echo "Cannot parse version"
	exit 2
fi

if [ ${#version} -gt 8 ]; then
	echo "Parsed version is suspiciously long, stopping"
	exit 2
fi


echo Done
echo "Version detected: [$version]"
#sleep 0.3

echo -n Copying mod files...
cp $binPath/* "$outtmp"
cp -r reobf/minecraft/{buildcraft,monnef,forestry,extrabiomes} "$outtmp"
rm -fr "$outtmp/monnef/core"
cp "lib/Jsoup_license.txt" "$outtmp"

outName="mod_jaffas_$version"

cd "$outtmp"
zip -q -9r "../$outName.jar" ./*

cd "$od"

echo Done
#jar packing done

#prepare core
echo -n Copying core files...
core2="$core/monnef"
mkdir "$core2" &>/dev/null
cp -r reobf/minecraft/monnef/core "$core2"
unzip -q jars/coremods/monnefCore_dummy.jar -d "$core"
if [ $? -ne 0 ]; then
	echo "Cannot unpack meta-inf stuff"
	exit 3
fi

outName="mod_monnef_core_$version"
cd "$core"
zip -q -9r "../$outName.jar" ./*
cd "$od"
echo Done
#core done



