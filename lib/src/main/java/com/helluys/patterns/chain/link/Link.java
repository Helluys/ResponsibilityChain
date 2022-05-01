package com.helluys.patterns.chain.link;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface Link<T> {
	boolean process(T value);

	default Link<T> chain(final Link<T> next) {
		return v -> process(v) ? true : next.process(v);
	}

	default EndLink<T> chain(final EndLink<T> next) {
		return v -> {
			if (!process(v)) {
				next.process(v);
			}
		};
	}

	default EndLink<T> endChain(final Consumer<T> consumer) {
		return v -> {
			if (!process(v)) {
				consumer.accept(v);
			}
		};
	}

	default <U> Link<T> map(final Function<T, U> mapper, final Link<U> next) {
		return v -> process(v) ? true : next.process(mapper.apply(v));
	}

	static <T, U> Link<T> ofMap(final Function<T, U> mapper, final Link<U> next) {
		return v -> next.process(mapper.apply(v));
	}

	static <T, U> EndLink<T> ofMap(final Function<T, U> mapper, final EndLink<U> next) {
		return v -> next.process(mapper.apply(v));
	}
}
