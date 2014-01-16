package org.kot.test.sma;

import java.util.concurrent.atomic.AtomicInteger;

/**
* Description.
* @author <a href=mailto:striped@gmail.com>striped</a>
* @todo Add JavaDoc
* @created 16/01/2014 00:06
*/
class Sum extends AtomicInteger {

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
