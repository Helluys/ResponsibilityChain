package com.helluys.patterns.chain.link;

import java.util.function.Consumer;

public interface EndLink<T> extends Link<T>, Consumer<T> {

	static <T> EndLink<T> of(final Consumer<T> consumer) {
		return consumer::accept;
	}

	@Override
	default boolean process(final T value) {
		accept(value);
		return true;
	}
}
