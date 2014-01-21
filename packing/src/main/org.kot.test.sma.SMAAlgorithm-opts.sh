#!/bin/sh

JAVA_OPTS='-server -Xms512m -Xmx512m -XX:PermSize=64m -XX:MaxPermSize=64m '
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:NewRatio=1 -XX:SurvivorRatio=128 -XX:MaxTenuringThreshold=0"
JAVA_OPTS="$JAVA_OPTS -XX:+TieredCompilation -XX:+UseCondCardMark -XX:+UseNUMA -XX:+AlwaysPreTouch -XX:InlineSmallCode=2000"

CP=lib/shared.jar:lib/java-sma.jar