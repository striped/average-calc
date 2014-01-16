package org.kot.test.sma;

/**
* Description.
* @author <a href=mailto:striped@gmail.com>striped</a>
* @todo Add JavaDoc
* @created 16/01/2014 00:06
*/
class MarketData {

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

	public double getAverage() {
		final double value = (double) sum.get();
		return value / data.length;
	}

	public boolean isValid() {
		for (;;) {
			int old = position.get();

			int expected = 0;
			for (int i = 0, idx = old; i < data.length; i++) {
				expected += data[idx];
				idx = (idx + 1) % data.length;
			}
			int actual = sum.get();
			if (old == position.get()) {
				return actual == expected;
			}
		}
	}


}
