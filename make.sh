#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PATH="$PATH:$DIR/utils/"
echo "Current path var: $PATH"

declare -a Work=('./recompile.sh' './reobfuscate.sh' './pack.sh');

for t in "${Work[@]}"; do
	echo "Running: $t"
	$t
	if [ "$?" -ne 0 ]; then
		echo "Failure!"
		exit 1
	fi
done

echo "Success!"
