package org.kot.test.sma;

import org.junit.Test;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Description.
 * @author <a href=mailto:striped@gmail.com>striped</a>
 * @todo Add JavaDoc
 * @created 16/01/2014 00:07
 */
public class MarketDataTest {

	@Test
	public void testCalculation() {
		final MarketData bucket = new MarketData(10);
		for (int i = 0; i < 10; i++) {
			bucket.put(i + 1);
		}
		assertThat(bucket.getAverage(), closeTo(5.5D, 0.001D));
		assertThat(bucket.isValid(), is(true));
	}
}
