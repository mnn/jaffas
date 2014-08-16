#!/bin/bash

current="$(readlink -f $(dirname "$0"))"

debug=""
outg="-r 25"
fast=""

while [ "$#" -gt 0 ]; do
	case "$1" in
		"debug")
			echo "debug mode enabled"
			debug="-t 10"
			;;
		
		"ffmpeg")
			echo "ffmpeg enabled"
			outg=" -o gource.ppm -r 25"
			runffmpeg=true
			;;

		"fast")
			fast="-c 4"
			;;

		*) echo "Unknown arg: $1"; exit 1
	esac
	shift
done

gource $debug $fast -s 1 -a 1 --viewport 1366x768 -w --title 'Jaffas and More!' --user-image-dir imgs --git-branch "1.7.2" --file-filter '(buildcraft/api)|(forestry/api)|(powercrystals/minefactoryreloaded)|(\.jar$)|(codechicken)|(wiki/.*\.png)|(textures/pre_1\.5)' "$current"/../.. $outg

if [ "$runffmpeg" == true ]; then
	ffmpeg -y -r 12 -f image2pipe -vcodec ppm -i gource.ppm -vcodec libx264 -preset ultrafast -pix_fmt yuv420p -crf 1 -threads 0 -bf 0 gource.mkv
fi
