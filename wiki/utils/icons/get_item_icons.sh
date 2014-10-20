#!/bin/bash

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

printDoing "Preparing blocks and vanilla items"
rm -fr ./$tmpdir/*.png
cp ../../../eclipse/monnefCoreExporter/*.png "./$tmpdir"
printDone

#---

printDoing "Converting blocks and vanilla items"
for file in "$tmpdir"/*.png ; do
    filename=$(basename "$file")
	filename=`sed -r 's/(item\.|tile\.)(.)/Grid_\u\2/' <<< "$filename" | sed -r 's/[jJ]affas\.(.)/\u\1/' | sed -r 's/([a-z])([A-Z])/\1_\2/g'`
    convert  "$file" -transparent "rgb(255,0,255)" png8:"$output/$filename"
done
printDone

#---

printDoing "Making montage of block and vanilla items"
montage -tile 10x -label '%t' -font Arial -pointsize 10 -background '#000000' -fill 'gray' -define png:size=32x32 -geometry 32x32+75+4 "$output/*.png" montage.png
printDone

#---

printDoing "Applying OptiPNG"
optiPNGlogFile="optipng.log"
rm -f $optiPNGlogFile
$optiPNGbin -o7 "$output"/*.png >> $optiPNGlogFile 2>&1
printDone
