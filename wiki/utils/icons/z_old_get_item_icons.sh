#!/bin/bash
# uses image magick to crop and resize icon images from sprite sheets

output=out

if [ ${#output} -lt 2 ]; then
	echo "weird string in output directory variable. halting for safety reasons."
	exit 1
fi

mkdir "$output" &> /dev/null
rm -fr $output/*.png

items_num=255
n=0
declare -a sprites=('jaffas_01_a.png' 'jaffas_02.png' 'jaffas_03.png' 'jaffas_04.png' 'jaffas_05.png' 'jaffas_06.png' 'jaffas_01_b.png')

spritePath='../../../bin_data'

while [ $n -le $items_num ]; do
	echo -n .
	f=$n
	if [ "$f" -lt 10 ]; then
		f=0$f
	fi
    
    if [ "$f" -lt 100 ]; then
            f=0$f
    fi

	i=0
	for t in "${sprites[@]}" ; do
		i=$(( i+1 ))
		ii=$i
		if [ $ii -lt 10 ]; then
			ii="0$ii"
		fi
		fname="$output/${ii}_$f.png"
		convert "$spritePath/$t" -crop 16x16+$[$n % 16 * 16]+$[$n / 16 * 16] +repage -scale 32x32 "$fname"

		# remove blank images
		colors=`identify -format "%k" "$fname"`
		if [ $colors == "1" ]; then
			rm -f $fname
		fi
	done
		
	n=$[$n + 1]
done

echo
