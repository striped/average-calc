#!/bin/sh

JAVA_OPTS='-server'
#JAVA_OPTS="$JAVA_OPTS -Xms2g -Xmx2g -XX:NewSize=1g -Xss228k -XX:PermSize=64m -XX:MaxPermSize=64m -XX:+UseG1GC"
JAVA_OPTS="$JAVA_OPTS -XX:+TieredCompilation -XX:+UseNUMA -XX:+AlwaysPreTouch"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCondCardMark -XX:-UseBiasedLocking"

CP=lib/shared.jar:lib/scala-sma.jar:lib/scala-library.jar:lib/scalaz-concurrent_2.10.jar:lib/scalaz-core_2.10.jar:lib/scalaz-effect_2.10.jar
