package org.kot.test.sma;

import org.kot.test.Algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 15/01/2014 21:06
 */
public class SMAAlgorithm implements Algorithm {


	final Map<String, MarketData> buckets;

	public SMAAlgorithm(final String... symbols) {
		if (null == symbols || 0 > symbols.length) {
			throw new IllegalArgumentException("Product list is not defined");
		}
		buckets = new HashMap<String, MarketData>(symbols.length, 1.0f);
		for (String symbol : symbols) {
			buckets.put(symbol, new MarketData(DEPTH));
		}
	}

	@Override
	public void update(final String symbol, final int value) {
		final MarketData bucket = buckets.get(symbol.intern());
		if (null == bucket) {
			return;
		}
		bucket.put(value);
	}

	static class MarketData {

		private final int[] data;

		private final Position position;

		private final Sum sum;

		MarketData(final int size) {
			this.data = new int[size];
			this.position = new Position(0, size);
			this.sum = new Sum(0);
		}

		public void put(final int price) {
			final int pos = position.moveOn();
			final int old = data[pos];
			data[pos] = price;
			sum.add(price - old);
		}

	}

	static class Position extends AtomicInteger {

		private final int limit;

		public Position(final int start, final int limit) {
			super(start);
			this.limit = limit;
		}

		public int moveOn() {
			for (;;) {
				final int current = get();
				if (compareAndSet(current, (current + 1) % limit)) {
					return current;
				}
			}
		}
	}

	static class Sum extends AtomicInteger {

		public Sum(final int start) {
			super(start);
		}

		public double add(final int addendum) {
			for (;;) {
				final int current = get();
				int next = current + addendum;
				if (compareAndSet(current, next)) {
					return next;
				}
			}
		}
	}
}
