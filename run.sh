#!/bin/sh

CP=./scala-sma/target/classes:./java-sma/target/classes:./shared/target/classes:~/.m2/repository/org/scala-lang/scala-library/2.10.4-RC1/scala-library-2.10.4-RC1.jar:~/.m2/repository/org/scalaz/scalaz-concurrent_2.10/7.1.0-M4/scalaz-concurrent_2.10-7.1.0-M4.jar:~/.m2/repository/org/scalaz/scalaz-core_2.10/7.1.0-M4/scalaz-core_2.10-7.1.0-M4.jar

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
