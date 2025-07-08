#!/bin/bash
set -e

JAVA_OPTS=${JAVA_OPTS:-"-Xms256m -Xmx512m"}

SERVER_PORT=${SERVER_PORT:-8181}
ACTUATOR_HEALTH_URL=${ACTUATOR_HEALTH_URL:-http://localhost:$SERVER_PORT/actuator/health}

exec java $JAVA_OPTS -jar /app/app.jar
