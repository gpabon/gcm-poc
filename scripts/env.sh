#!/bin/sh
# ----------------------------------------------------------------------------
#

CLASSPATH=.

if [ -z "$POC_HOME" ]
then
	workingDir=`dirname $0`
       POC_HOME=$(cd $workingDir/.././ || (echo "Broken PROACTIVE installation" ; exit 1) && echo $PWD)
fi


# ----------------------------------------------------------------------------


JAVA_HOME=${JAVA_HOME-NULL};
if [ "$JAVA_HOME" = "NULL" ]
then
echo
echo "The enviroment variable JAVA_HOME must be set the current jdk distribution"
echo "installed on your computer."
echo "Use "
echo "    export JAVA_HOME=<the directory where is the JDK>"
exit 127
fi

# ----
# Set up the classpath
#

CLASSPATH=$CLASSPATH:$POC_HOME/dist/lib/ProActive.jar
CLASSPATH=$CLASSPATH:$POC_HOME/classes

export CLASSPATH

JAVACMD=$JAVA_HOME/bin/java"\
	-Djava.security.manager \
	-Djava.security.policy=$POC_HOME/scripts/proactive.java.policy \
	-Dlog4j.configuration=file:${POC_HOME}/scripts/proactive-log4j \
   -Dproactive.home=$POC_HOME \
	-Dos=unix"

export POC_HOME
export JAVACMD
