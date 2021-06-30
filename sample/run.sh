#!/bin/sh

set -e

java ${JAVA_OPTS} \
     -jar -Dspring.profiles.active=${APP_ENV} \
     /app/com.thoughtworks.inspac.integration-sample.jar