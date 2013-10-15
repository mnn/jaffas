#!/bin/bash

echo -n "Removing old scala binary files ... "
rm -fr lib/monnef
rm -fr bin/minecraft
echo done

w='./recompile.sh'
echo "Running: $w"
$w

if [ "$?" -eq 0 ]; then
	echo "Recompile ended fine, this script should not be used."
	exit 1
fi

echo "Recompile is done."

echo -n "Renaming log file ... "
cclog=client_compile.log
mv logs/$cclog logs/${cclog}.scala
echo done

echo -n "Copying scala files to lib directory ... "
cp -r bin/minecraft/monnef lib
echo done
