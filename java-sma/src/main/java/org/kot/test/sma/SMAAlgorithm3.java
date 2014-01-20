package org.kot.test.sma;

import org.kot.test.Algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 19/01/2014 01:34
 */
public class SMAAlgorithm3 implements Algorithm {

	final String[] symbols;

	final MarketData[] buckets;

	public SMAAlgorithm3(final String... symbols) {
		if (null == symbols || 0 > symbols.length) {
			throw new IllegalArgumentException("Product list is not defined");
		}
		buckets = new MarketData[symbols.length];
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = new MarketData(DEPTH);
		}
		Arrays.sort(symbols);
		this.symbols = symbols;
	}

	@Override
	public void update(final String symbol, final int value) {
		final int idx = Arrays.binarySearch(symbols, symbol);
		if (0 > idx) {
			return;
		}
		buckets[idx].put(value);
	}

	public boolean isValid() {
		for (MarketData bucket : buckets) {
			if (!bucket.isValid()) {
				return false;
			}
		}
		return true;
	}

}
