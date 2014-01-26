#!/bin/sh

java -version

classes='org.kot.test.sma.SMAAlgorithm com.github.plokhotnyuk.sma.SMAAlgorithm2'
threads='1 2 4 8'
symbols='EUR/USD EUR/UAH'

java -jar target/microbenchmarks.jar -wi 10 -i 3 -f 1
