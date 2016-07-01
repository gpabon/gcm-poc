#!/bin/sh

echo
echo --- GCM PoC ---------------------------------------------

workingDir=`dirname $0`
. ${workingDir}/env.sh

$JAVACMD test.gpabon.gcmpoc.Main

echo
echo ------------------------------------------------------------
