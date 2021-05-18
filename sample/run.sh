#!/usr/bin/env bash

set -e

java ${JAVA_OPTS} \
     -jar -Dspring.profiles.active=${APP_ENV} \
     /app/com.thoughtworks.gac.oidc-integration-sample.jar