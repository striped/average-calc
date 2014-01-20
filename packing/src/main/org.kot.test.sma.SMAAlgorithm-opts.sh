#!/bin/sh

JAVA_OPTS='-server'
JAVA_OPTS="$JAVA_OPTS -XX:+TieredCompilation -XX:+UseCondCardMark -XX:+UseNUMA -XX:+AlwaysPreTouch"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintCompilation"

CP=lib/shared.jar:lib/java-sma.jar