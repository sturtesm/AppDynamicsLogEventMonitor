#!/bin/sh

LOG_FILE=$1

if [ "x${LOG_FILE}" = "x" ]; then
    echo
    echo "expecting startLogEventMonitor.sh <log-file>"
    echo
    exit 1;
fi

JAVA_OPTS=-javaagent:/Users/steve.sturtevant/Tools/AppDynamics/AppDynamicsProAgent/javaagent.jar
JAVA_OPTS=$JAVA_OPTS -Dappdynamics.agent.applicationName=MuleESB
JAVA_OPTS=$JAVA_OPTS -Dappdynamics.agent.tierName=MuleESB -Dappdynamics.agent.nodeName=MuleESB_Node1

java ${JAVA_OPTS} -jar AppdLogMonitor.jar $1
