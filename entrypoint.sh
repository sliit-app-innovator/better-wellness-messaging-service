#!/bin/sh
set -e

# JVM options can be configured using an environment variable
JAVA_OPTS="${JAVA_OPTS:--Xms256m -Xmx512m}"

echo "Starting Spring Boot application..."
exec java $JAVA_OPTS -jar /app.jar
