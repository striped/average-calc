#!/bin/sh

java -version

classes='org.kot.test.sma.SMAAlgorithm com.github.plokhotnyuk.sma.SMAAlgorithm2'
threads='1 2 4 8'
symbols='EUR/USD EUR/UAH'

for n in $threads; do
	for class in $classes; do
		for attmpt in {1..3}; do
            [ -f ./$class-opts.sh ] && . ./$class-opts.sh || . ./opts.sh
			java $JAVA_OPTS -cp "$CP" org.kot.test.Benchmark $class $n $symbols $@
			sleep 5
		done
	done
done
