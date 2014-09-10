#!/bin/bash
echo "-------------------------------------------------------------------------"
echo "  Packing script for Minecraft mod \"Jaffas and More!\" and \"monnef core\""
echo "      Created by monnef"
echo "-------------------------------------------------------------------------"
echo

current="$(readlink -f $(dirname "$0"))"

combined_jar="build/libs/combined.jar"
output_dir="jar_output"
dir_all="$output_dir/all"
dir_core="$output_dir/core"
dir_jaffas="$output_dir/jaffas"

function ensureDir {
	if [ \! -d "$1" ]; then
		echo "Creating \"$1\"."
		mkdir "$1"
	fi
}

function printStart {
	echo -n "$1 ... "
}

function printDone {
	echo "done"
}


if [ \! -f "$combined_jar" ]; then
	echo "Combined jar is missing."
	exit 1
fi

printStart "Version parsing"
mod_version=`bash get_mod_version.bash`
core_version=`bash get_version.bash`
printDone
echo "mod version: ${mod_version}, core version: ${core_version}"

rm -fr "./$output_dir"

ensureDir "$output_dir"
ensureDir "$dir_all"
ensureDir "$dir_core"
ensureDir "$dir_jaffas"

printStart "Unpacking jar"
unzip -q "$combined_jar" -d "$dir_all"
printDone

# TODO: moving to appropriate directories ($dir_core and $dir_jaffas) and zipping
