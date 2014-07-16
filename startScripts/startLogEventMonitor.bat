#!/bin/sh

set JAVA_OPTS=-javaagent:/Users/steve.sturtevant/Tools/AppDynamics/AppDynamicsProAgent/javaagent.jar
set JAVA_OPTS=%JAVA_OPTS% -Dappdynamics.agent.applicationName=MuleESB
set JAVA_OPTS=%$JAVA_OPTS% -Dappdynamics.agent.tierName=MuleESB -Dappdynamics.agent.nodeName=MuleESB_Node1

java %JAVA_OPTS% -jar AppdLogMonitor.jar $1
