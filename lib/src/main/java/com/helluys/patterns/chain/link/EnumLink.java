package com.helluys.patterns.chain.link;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class EnumLink<T, E extends Enum<E>> implements Link<T> {

	private final Function<T, E> extractor;
	private final Map<E, Link<T>> links;

	public EnumLink(final Function<T, E> extractor) {
		this.extractor = extractor;
		links = new HashMap<>();
	}

	public final EnumLink<T, E> with(final E value, final Link<T> link) {
		links.put(value, link);
		return this;
	}

	@Override
	public final boolean process(final T value) {
		final E e = extractor.apply(value);
		if (links.containsKey(e)) {
			return links.get(e).process(value);
		}
		return false;
	}
}
