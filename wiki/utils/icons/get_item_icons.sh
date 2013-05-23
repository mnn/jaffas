#!/bin/bash
# uses image magick to crop and resize icon images from sprite sheets

function printDoing {
    echo -n "$1 ... "
}

function printDone {
    echo "done"
}

echo
echo "  Icons exporter for jaffas wiki"
echo "--==============================--"
echo

printDoing "Preparing directories"
indir="../../../bin_data/mods"
output=out
tmpdir=tmp
optiPNGbin="../optipng-0.7.4-win32/optipng"

if [ ${#output} -lt 2 ]; then
	echo "weird string in output directory variable. halting for safety reasons."
	exit 1
fi

if [ ${#tmpdir} -lt 2 ]; then
    echo "weird string in tmp directory variable. halting for safety reasons."
    exit 1
fi

mkdir "$output" &> /dev/null
rm -fr ./$output/*.png

mkdir "$tmpdir" &> /dev/null
rm -fr ./$tmpdir/*.png

printDone

#---

printDoing "Copying png files"
find "$indir" -type f | grep -Ei 'mods/.*/textures/.*\.png' | xargs -I {} cp {} "$tmpdir"
printDone

#---

printDoing "Converting"
for file in "$tmpdir"/*.png ; do
    filename=$(basename "$file")
    convert  "$file" -scale 32x32 png8:"$output/$filename"
done
printDone

optiPNGlogFile="optipng.log"
rm -f $optiPNGlogFile
printDoing "Applying OptiPNG"
$optiPNGbin -o7 "$output"/*.png >> $optiPNGlogFile 2>&1
printDone
