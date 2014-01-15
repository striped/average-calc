package org.kot.test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 15/01/2014 22:00
 */
public class Benchmark {

	public static final int ITERATIONS = 1000;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		if (3 > args.length) {
			System.out.printf(
					"Usage %s <class> <threads> <symbol>[ <symbol>]%n" +
					"\tclass - algorithm implementation%n" +
					"\tthreads - number of threads%n" +
					"\tsymbol - product symbol(s)",
					Benchmark.class.getName()
			);
			System.exit(13);
		}

		final int threads = Integer.parseInt(args[1]);
		final String[] symbols = Arrays.copyOfRange(args, 2, args.length);

		Class<Algorithm> clazz = (Class<Algorithm>) Class.forName(args[0]);
		final Constructor<Algorithm> constructor = clazz.getConstructor(String[].class);
		final Algorithm algorithm = constructor.newInstance((Object) symbols);

		List<Future<Long>> generators = new ArrayList<Future<Long>>(threads);
		final ExecutorService executor = Executors.newFixedThreadPool(threads);
		for (int t = 0; t < threads; t++) {
			generators.add(executor.submit(new Generator(algorithm, symbols)));
		}
		executor.shutdown();

		long time = 0;
		for (Future<Long> task : generators) {
			time += task.get();
		}

		System.out.printf("Total time: %,d%n", time);
	}

	static class Generator implements Callable<Long> {

		private final Algorithm algorithm;

		private final String[] symbols;

		Generator(final Algorithm algorithm, final String[] symbols) {
			this.algorithm = algorithm;
			this.symbols = symbols;
		}

		@Override
		public Long call() throws Exception {
			final long warmUp = ITERATIONS << 2;
			for (long i = 0; i < warmUp; i++) {
				algorithm.update(symbols[(int) i % 2], (int)i);
			}
			long time = System.nanoTime();
			for (int i = 0; i < ITERATIONS; i++) {
				algorithm.update(symbols[i % 2], i);
			}
			return System.nanoTime() - time;
		}
	}
}
