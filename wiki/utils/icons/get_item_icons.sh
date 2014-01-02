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
indir="../../../bin_data/assets"
output=out
outputB=outB
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

mkdir "$outputB" &> /dev/null
rm -fr ./$outputB/*.png

mkdir "$tmpdir" &> /dev/null
rm -fr ./$tmpdir/*.png

printDone

#---

printDoing "Copying png files"
find "$indir" -type f | grep -Ei 'assets/.*/textures/items/.*\.png' | xargs -I {} cp {} "$tmpdir"
printDone

#---

printDoing "Converting"
for file in "$tmpdir"/*.png ; do
    filename=$(basename "$file")
    convert  "$file" -scale 32x32 png8:"$output/$filename"
done
printDone

#---

printDoing "Making montage of item icons"
montage -tile 30x -label '%t' -font Arial -pointsize 10 -background '#000000' -fill 'gray' -define png:size=32x32 -geometry 32x32+7+7 "$output/*.png" montageItems.png
printDone

#---

printDoing "Preparing blocks and vanilla items"
rm -fr ./$tmpdir/*.png
cp ../../../jars/monnefCoreExporter/*.png "./$tmpdir"
printDone

#---

printDoing "Converting blocks and vanilla items"
for file in "$tmpdir"/*.png ; do
    filename=$(basename "$file")
    filename=`tr '[:upper:]' '[:lower:]' <<< "$filename"`
    convert  "$file" -transparent "rgb(255,0,255)" png8:"$outputB/$filename"
done
printDone

#---

printDoing "Making montage of block and vanilla items"
montage -tile 10x -label '%t' -font Arial -pointsize 10 -background '#000000' -fill 'gray' -define png:size=32x32 -geometry 32x32+75+4 "$outputB/*.png" montageBlocks.png
printDone

#---

printDoing "Applying OptiPNG"
optiPNGlogFile="optipng.log"
rm -f $optiPNGlogFile
$optiPNGbin -o7 "$output"/*.png >> $optiPNGlogFile 2>&1
printDone
