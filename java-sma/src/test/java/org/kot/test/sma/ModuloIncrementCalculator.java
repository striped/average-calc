package org.kot.test.sma;

/**
 * Silly check inspired by Andriy and Mr. Adam Crume <a href="http://www.adamcrume.com/blog/archive/2012/12/06/overhead-of-the-modulo-operator">@ here</a>.
 * <p/>
 * At a moment of creation was checked on JDK7 u45 (ava HotSpot(TM) 64-Bit Server VM, build 24.45-b08, mixed mode) with following results:
 * {@code
 *  [Modulo]: Took 36.890 ns
 *  [While]: Took 203.904 ns
 *  [If]: Took 32.205 ns
 * }
 * So kindly burst the myth about modulo overhead in JIT: {@code If} differs on a lever of deviation and {@code While} is far behind.
 * <p/>
 * <b>Conclusion</b>. Follwo the KISS: dont make a mess in a code, and JIT will be able help you.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @created 21/01/2014 21:30
 */
public class ModuloIncrementCalculator {

	public static void main(String... args) {
		if (0 == args.length) {
			System.out.printf(
					"Usage %s [type]%n" +
					"\ttype - either:%n" +
					"\t\tnormal (default) - (x + 1) % n;%n" +
					"\t\twhile - x++; while (n < x) x -= n;%n" +
					"\t\tif - x++; if (n < x) x -= n;%n",
					ModuloIncrementCalculator.class.getName()
			);
			System.exit(13);
		}
		Increment f;
		switch (args[0]) {
			case "while": f = new While(); break;
			case "if": f = new If(); break;
			case "normal":
			default: f = new Modulo();
		}
		long damp = 0;
		int max = (10000 + 15000 + 2000) << 5;
		for (int i = 0; i < max; i++) {
			damp += f.inc(i, 10);
		}
		max = 10000;
		double time = System.nanoTime();
		for (int i = 0; i < max; i++) {
			damp += f.inc(i, 10);
		}
		time = System.nanoTime() - time;
		System.out.printf("[%s]: Took %,.3f ns", f.getClass().getSimpleName(), time / max);
	}

	interface Increment {
		int inc(int i, int n);
	}

	static class Modulo implements Increment {

		@Override
		public int inc(final int i, final int n) {
			return (i + 1) % n;
		}
	}

	static class While implements Increment {

		@Override
		public int inc(int i, final int n) {
			i++;
			while (n < i) {
				i -= n;
			}
			return i;
		}
	}

	static class If implements Increment {

		@Override
		public int inc(int i, final int n) {
			i++;
			if (n < i) {
				i -= n;
			}
			return i;
		}
	}
}
