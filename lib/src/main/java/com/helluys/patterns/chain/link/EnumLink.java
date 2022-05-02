package com.helluys.patterns.chain.link;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link Link} that fans the processing of the command to another link based
 * on the value of an enumerated value extracted from teh command.
 *
 * @author Helluys
 *
 * @param <Command> the command type
 * @param <E>       the enumerated value type
 */
public final class EnumLink<Command, E extends Enum<E>> implements Link<Command> {

	private final Function<Command, E> extractor;
	private final Map<E, EndLink<Command>> links;

	/**
	 * Constructs an instance. Based on the desired behaviour, for unmappable input
	 * the enumerated value extraction function should either:
	 * <li>
	 * <ul>
	 * throw a {@link RuntimeException} which will bubble up to the caller
	 * </ul>
	 * <ul>
	 * return {@code null}, in which case the command is simply not processed
	 * </ul>
	 * </li>
	 *
	 * @param extractor the enumerated value extraction function
	 */
	public EnumLink(final Function<Command, E> extractor) {
		this.extractor = extractor;
		links = new HashMap<>();
	}

	@Override
	public final boolean process(final Command value) {
		final E e = extractor.apply(value);
		if (links.containsKey(e)) {
			links.get(e).process(value);
			return true;
		}
		return false;
	}

	/**
	 * Associates an {@link EndLink} to an enumerated value. This link will be
	 * called on {@link #process} with the command if the extractor returns the
	 * provided enumerated value.
	 *
	 * @param value the enumerated value, must be non-null
	 * @param link  the end link for that enumerated value
	 * @return this {@link EnumLink}
	 */
	public final EnumLink<Command, E> with(final E value, final EndLink<Command> link) {
		Objects.requireNonNull(value);
		links.put(value, link);
		return this;
	}

	/**
	 * Associates the given end link for non-mapped enumerated values. This is
	 * equivalent to {@link #chain(EndLink)}.
	 *
	 * @param defaultProcessing the default processing to apply when the enumerated
	 *                          value is not associated to any {@link Link}
	 * @return this {@link EnumLink} chained with the given end link
	 */
	public final EndLink<Command> otherwise(final EndLink<Command> defaultProcessing) {
		return chain(defaultProcessing);
	}
}
