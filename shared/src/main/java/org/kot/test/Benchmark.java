package org.kot.test;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 15/01/2014 22:00
 */
public class Benchmark {

	public static final int ITERATIONS = 10000;

	private final Class<Algorithm> clazz;

	private final List<Future<Long>> tasks;

	public Benchmark(final Class<Algorithm> clazz, final int threads, final String[] symbols) throws Exception {
		this.clazz = clazz;
		final Constructor<Algorithm> constructor = this.clazz.getConstructor(String[].class);
		final Algorithm algorithm = constructor.newInstance((Object) symbols);

		tasks = new ArrayList<>(threads);
		final ExecutorService executor = Executors.newFixedThreadPool(threads);
		for (int t = 0; t < threads; t++) {
			tasks.add(executor.submit(createWorker(algorithm, symbols)));
		}
		executor.shutdown();
	}

	@Override
	public String toString() {
		double time = 0;
		for (Future<Long> task : tasks) {
			try {
				time += task.get();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				e.printStackTrace();
				return "Fucked up... ";
			}
		}
		return String.format("[%s]: Execution time (%d workers, %d iterations): %,.3f ns", clazz, tasks.size(), ITERATIONS, time / tasks.size());
	}

	protected Callable<Long> createWorker(final Algorithm algorithm, final String[] symbols) {
		return new Generator(algorithm, symbols);
	}

	protected void cancel() {
		for (Future<Long> task : tasks) {
			task.cancel(true);
		}
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		if (3 > args.length) {
			System.out.printf(
					"Usage %s <class> <threads> <symbol>[ <symbol>]%n" +
					"\tclass - algorithm implementation%n" +
					"\tthreads - number of threads%n" +
					"\tsymbol - product symbol(s)%n",
					Benchmark.class.getName()
			);
			System.exit(13);
		}

		final int threads = Integer.parseInt(args[1]);
		final String[] symbols = Arrays.copyOfRange(args, 2, args.length);

		Class<Algorithm> clazz = (Class<Algorithm>) Class.forName(args[0]);

		final Benchmark app = new Benchmark(clazz, threads, symbols);
		System.out.println(app);
	}

	static class Generator implements Callable<Long> {

		private final Algorithm algorithm;

		private final String[] symbols;

		private static final int warmUp = Math.max(ITERATIONS * 4, 10000 + 2000 + 15000);

		@Override
		public Long call() throws Exception {

			// warm up
			final CompilationMXBean ci = ManagementFactory.getCompilationMXBean();
			long ciTime = ci.getTotalCompilationTime();
			for (int i = 0; i < 10; i++, ciTime = ci.getTotalCompilationTime()) {
				doWork(warmUp);
				//Thread.yield();
				if (ciTime != ci.getTotalCompilationTime()) {
					i = 0;
				}
			}

			long time = System.nanoTime();
			doWork(ITERATIONS);
			return System.nanoTime() - time;
		}

		Generator(final Algorithm algorithm, final String[] symbols) {
			this.algorithm = algorithm;
			this.symbols = symbols;
		}

        void doWork(final int iterations) {
            for (int i = 0; i < iterations; i++) {
                algorithm.update(symbols[i % symbols.length], i);
            }
        }
	}
}
