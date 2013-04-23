#!/bin/bash

output=jar_output
outtmp=$output/tmp
core=$output/core
binPath=bin_data
dist=$output/dist

echo -n Preparing...

od=`pwd`
cd "$od"

if [ ${#output} -lt 5 ]; then
	echo "weird string in output directory variable. halting for safety reasons."
	exit 1
fi

rm -fr ./$outtmp/*
rm -fr ./$output/*
rm -fr ./$core/*
rm -fr ./$dist/*

mkdir $output &>/dev/null
mkdir $outtmp &>/dev/null
mkdir $core &>/dev/null
mkdir $dist &>/dev/null

touch "$outtmp/.placeholder"

echo Done
echo -n Parsing version...

ver_line=`grep -Ei 'String +Version' src/minecraft/monnef/jaffas/food/common/Reference.java`
test $? -ne 0 && { echo "Cannot determine version" ; exit 2 ; }

version=`sed -r 's/^.*"(.*)".*;$/\1/' <<< "$ver_line"`
test $? -ne 0 && { echo "Cannot parse version" ; exit 2 ; }

test ${#version} -gt 8 && { echo "Parsed version is suspiciously long, stopping" ; exit 2 ; }

echo Done
echo "Version detected: [$version]"
#sleep 0.3

echo -n Copying mod files...
cp -r $binPath/* "$outtmp"
cp -r reobf/minecraft/{buildcraft,monnef,forestry,extrabiomes} "$outtmp"
rm -fr "$outtmp/monnef/core"
cp "lib/Jsoup_license.txt" "$outtmp"

outName="mod_jaffas_$version"

cd "$outtmp"
zip -q -9r "../$outName.jar" ./*

cd "$od"

echo Done
#jar packing done

#core version
echo -n Parsing core version...
ver_line=`grep -Ei 'String +Version' src/minecraft/monnef/core/Reference.java`
test $? -ne 0 && { echo "Cannot determine version" ; exit 2 ; }

version_core=`sed -r 's/^.*"(.*)".*;$/\1/' <<< "$ver_line"`
test $? -ne 0 && { echo "Cannot parse version" ; exit 2 ; }

test ${#version_core} -gt 8 && { echo "Parsed version is suspiciously long, stopping" ; exit 2 ; }
echo Done
echo "Version detected: [$version_core]"

#prepare core
echo -n Copying core files...
core2="$core/monnef"
mkdir "$core2" &>/dev/null
cp -r reobf/minecraft/monnef/core "$core2"
unzip -q jars/coremods/monnefCore_dummy.jar -d "$core"
cp bin_data/jaffas_mappings.ser "$core"
if [ $? -ne 0 ]; then
	echo "Cannot unpack meta-inf stuff"
	exit 3
fi

outNameB="mod_monnef_core_$version_core"
cd "$core"
zip -q -9r "../$outNameB.jar" ./*
cd "$od"
echo Done
#core done

echo -n Creating final zips...
cd $dist
mkdir mods
cd "$od"
cp "$output/$outName.jar" "$dist/mods"
cd "$dist"
outNameZ="mod_jaffas_${version}_packed"
zip -q -9r "../$outNameZ.zip" ./*
cd "$od"

cd $dist
rm -fr mods
mkdir coremods
cd "$od"
cp "$output/$outNameB.jar" "$dist/coremods"
cd "$dist"
outNameZ="monnef_core_${version_core}_packed"
zip -q -9r "../$outNameZ.zip" ./*

cd "$od"
echo Done



