#!/bin/bash

output=jar_output
outtmp=$output/tmp

od=`pwd`

rm -fr "$outtmp/*"
touch "$outtmp/.placeholder"

cp jar_data/* "$outtmp/"
cp -r reobf/minecraft/* "$outtmp"

cd "$outtmp"
zip -9r "../mod_jaffas.jar" ./*

cd "$od"
