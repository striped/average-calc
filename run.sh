#!/bin/sh

CP=java-sma/target/classes:shared/target/classes

JAVA_OPTS='-server -XX:+AggressiveOpts -XX:CompileThreshold=1000'

java -version

classes='org.kot.test.sma.SMAAlgorithm'
threads='1 2 4 8'
symbols='EUR/USD EUR/UAH'

for n in $threads; do
	for class in $classes; do
		for attmpt in 1 2 3; do
			java $JAVA_OPTS -cp "$CP" org.kot.test.Benchmark $class $n $symbols $@
			sleep 2
		done
	done
done
