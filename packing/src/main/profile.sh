#!/bin/sh

java -version

class='org.kot.test.sma.SMAAlgorithm'
threads='8'
symbols='EUR/USD EUR/UAH'

[ -f ./$class-opts.sh ] && . ./$class-opts.sh || . ./opts.sh
JAVA_OPTS="$JAVA_OPTS -XX:+UnlockCommercialFeatures -XX:+FlightRecorder"
JAVA_OPTS="$JAVA_OPTS -XX:+UnlockDiagnosticVMOptions -XX:+PrintTieredEvents -XX:+PrintCompilation -XX:+PrintInlining -XX:+PrintIntrinsics"
java $JAVA_OPTS -cp "$CP" org.kot.test.Profiler $class $threads $symbols $@
