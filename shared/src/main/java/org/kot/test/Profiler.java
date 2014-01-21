package org.kot.test;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 20/01/2014 23:41
 */
public class Profiler extends Benchmark {

	public Profiler(final Class<Algorithm> clazz, final int threads, final String[] symbols) throws Exception {
		super(clazz, threads, symbols);
	}

	@Override
	protected Callable<Long> createWorker(final Algorithm algorithm, final String[] symbols) {
		return new Sisyphus(algorithm, symbols);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		if (3 > args.length) {
			System.out.printf(
					"Usage %s <class> <threads> <symbol>[ <symbol>]%n" +
					"\tclass - algorithm implementation%n" +
					"\tthreads - number of threads%n" +
					"\tsymbol - product symbol(s)%n",
					Profiler.class.getName()
			);
			System.exit(13);
		}

		final int threads = Integer.parseInt(args[1]);
		final String[] symbols = Arrays.copyOfRange(args, 2, args.length);

		Class<Algorithm> clazz = (Class<Algorithm>) Class.forName(args[0]);

		final Profiler app = new Profiler(clazz, threads, symbols);

		Scanner console = new Scanner(System.in);
		System.out.print("Press Enter to exit..");
		console.nextLine();
		app.cancel();
	}

	static class Sisyphus implements Callable<Long> {

		public static final long IO_DELAY = TimeUnit.MILLISECONDS.toNanos(10);

		private final Algorithm algorithm;

		private final String[] symbols;

		@Override
		public Long call() {
			for (int i = 0; ; i++) {
				if (Thread.interrupted()) {
					return 0L;
				}
				LockSupport.parkNanos(IO_DELAY);
				algorithm.update(symbols[i % symbols.length], i);
			}
		}

		Sisyphus(final Algorithm algorithm, final String[] symbols) {
			this.algorithm = algorithm;
			this.symbols = symbols;
		}
	}
}
