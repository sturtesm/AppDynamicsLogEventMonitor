#!/bin/sh

set APPDYNAMICS_AGENT=

set JAVA_OPTS=-javaagent:%APPDYNAMICS_AGENT%
set JAVA_OPTS=%JAVA_OPTS% -Dappdynamics.agent.applicationName=invoice
set JAVA_OPTS=%$JAVA_OPTS% -Dappdynamics.agent.tierName=Clustered_Supplychain -Dappdynamics.agent.nodeName=Clustered_Supplychain_1

echo java %JAVA_OPTS% -jar AppdLogMonitor.jar <log-file>
java %JAVA_OPTS% -jar AppdLogMonitor.jar <log-file>
