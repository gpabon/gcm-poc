#!/bin/sh

echo
echo --- GCM PoC ---------------------------------------------

workingDir=`dirname $0`
. ${workingDir}/env.sh

JAVACMD=$JAVACMD" -Dgcm.provider=org.objectweb.proactive.core.component.Fractive"
$JAVACMD test.gpabon.gcmpoc.Main

echo
echo ------------------------------------------------------------
