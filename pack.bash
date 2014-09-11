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
current_phase=""

function ensureDir {
	if [ \! -d "$1" ]; then
		echo "Creating \"$1\"."
		mkdir -p "$1"
	fi
}

function printStart {
	current_phase="$1"
	echo "$current_phase ... "
}

function printDone {
	echo "$current_phase ... done"
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

printStart "Categorizing directories"
cd "$current"

if [ -d  "$dir_all/assets/monnef core" ]; then
	ensureDir "$dir_core/assets"
	mv "$dir_all/assets/monnef core" "$dir_core/assets"
fi
mv "$dir_all/assets" "$dir_jaffas"

# APIs
mv "$dir_all/buildcraft" "$dir_core"
mv "$dir_all/forestry" "$dir_jaffas"
mv "$dir_all/powercrystals" "$dir_jaffas"

# mostly my classes
ensureDir "$dir_core/monnef"
mv "$dir_all/monnef/core" "$dir_core/monnef"
mv "$dir_all/monnef/external" "$dir_core/monnef"
mv "$dir_all/monnef" "$dir_jaffas"

# rest
mv "$dir_all"/* "$dir_jaffas"
printDone

printStart "Creating core manifest"
dir_core_manifest="$dir_core/META-INF"
ensureDir "$dir_core_manifest"
echo -e 'Manifest-Version: 1.0\nFMLCorePlugin: monnef.core.MonnefCorePlugin\n' > "$dir_core_manifest/MANIFEST.MF"
printDone

printStart "Packing core"
cd "$current"
cd "$dir_core"
zip -q -9r "../monnef_core_${core_version}.jar" ./*
cd "$current"
printDone

printStart "Packing mod"
cd "$current"
cd "$dir_jaffas"
zip -q -9r "../jaffas_${mod_version}.jar" ./*
cd "$current"
printDone

