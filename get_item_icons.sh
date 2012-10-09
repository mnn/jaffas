#!/bin/bash

out=item_icons

mkdir $out &>/dev/null
rm -f "$out/*.png"

items_num=65
n=0

while [ $n -le $items_num ]; do
	echo -n .
	f=$n
	if [ "$f" -lt 10 ]; then
		f=0$f
	fi
	convert "eclipse/Minecraft/bin/jaffas_01.png" -crop 16x16+$[$n % 16 * 16]+$[$n / 16 * 16] +repage -sample 32x32 "$out/$f.png"
	n=$[$n + 1]
done

echo
