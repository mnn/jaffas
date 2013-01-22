#!/bin/bash

i=0

tar=cropped
src=source

rm -fr "./$tar/*.png"

for file in $src/*.png ; do
#	echo "$file"
	i=$[$i+1]
	j=$i
	if [ $i -lt 100 ]; then
		j=0$j
	fi

        if [ $i -lt 10 ]; then
                j=0$j
        fi

	convert "$file" -crop 235x156+840+350 +repage "$tar/$j.png"

        convert "$file" -crop 235x115+840+510 +repage "$tar/${j}b.png"
	
#	convert "$file" -crop 32x32+1016+422 +repage "$tar/furn_$j.png"
#	convert "$file" -crop 32x32+1033+422 +repage "$tar/craft_$j.png"
	
	echo -n .
done

montage -tile 3x -geometry +2+2 $tar/* montage.png

