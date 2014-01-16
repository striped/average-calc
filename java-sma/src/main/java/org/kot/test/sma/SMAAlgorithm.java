package org.kot.test.sma;

import org.kot.test.Algorithm;

import java.util.HashMap;
import java.util.Map;

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

	public boolean isValid() {
		for (MarketData bucket : buckets.values()) {
			if (!bucket.isValid()) {
				return false;
			}
		}
		return true;
	}

}
