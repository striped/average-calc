package org.kot.test;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ModuloIncrementBenchmark {

	private static final int N = 10;

	public static final int ITERATIONS = 2 * N;

	private int[] data = new int[N];

    @GenerateMicroBenchmark
    public void incModulo() {
	    int idx = -1;
	    for (int i = 0; i < ITERATIONS; i++) {
		    idx = (idx + 1) % N;
		    data[idx] = i;
	    }
    }

	@GenerateMicroBenchmark
	public void incWhile() {
		int idx = -1;
		for (int i = 0; i < ITERATIONS; i++) {
			idx++;
			while (N <= idx) {
				idx -= N;
			}
			data[idx] = i;
		}
	}

	@GenerateMicroBenchmark
	public void incIf() {
		int idx = -1;
		for (int i = 0; i < ITERATIONS; i++) {
			idx++;
			if (N <= idx) {
				idx -= N;
			}
			data[idx] = i;
		}
	}


	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + ModuloIncrementBenchmark.class.getSimpleName() + ".*")
				.forks(1)
				.build();

		new Runner(opt).run();
	}
}
