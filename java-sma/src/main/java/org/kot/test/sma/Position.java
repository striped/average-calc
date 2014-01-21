package org.kot.test.sma;

import java.util.concurrent.atomic.AtomicInteger;

/**
* Description.
* @author <a href=mailto:striped@gmail.com>striped</a>
* @todo Add JavaDoc
* @created 16/01/2014 00:06
*/
class Position extends AtomicInteger {

	private final int limit;

	public Position(final int start, final int limit) {
		super(start);
		this.limit = limit;
	}

	public int moveOn() {
		final int size = limit;
		for (;;) {
			final int current = get();
			if (compareAndSet(current, (current + 1) % size)) {
				return current;
			}
		}
	}
}
