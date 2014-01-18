#!/bin/sh

CP=scala-sma/target/classes:java-sma/target/classes:shared/target/classes

JAVA_OPTS='-server -Xms2g -Xmx2g -XX:NewSize=1g -Xss228k -XX:PermSize=64m -XX:MaxPermSize=64m -XX:+TieredCompilation -XX:+UseG1GC -XX:+UseNUMA -XX:+UseCondCardMark -XX:-UseBiasedLocking -XX:+AlwaysPreTouch'

java -version

classes='com.github.plokhotnyuk.sma.SMAAlgorithm2 org.kot.test.sma.SMAAlgorithm'
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
