package com.helluys.patterns.chain.link;

import java.util.function.Consumer;

/**
 * A responsibility chain final link. Unlike a {@link Link}, it cannot be
 * {@link Link#chain chained} with others, it represents the last element of a
 * chain. This allows mapping it back to a {@link Consumer}. It may represent an
 * individual end link or a whole ended chain.
 *
 * @author Helluys
 * @param <Command> the command type
 */
public interface EndLink<Command> extends Consumer<Command> {

	/**
	 * Returns a new end link that always consumes with the given processing.
	 *
	 * @param <Command>  the command type
	 * @param processing the processing
	 * @return a new end link
	 */
	static <Command> EndLink<Command> of(final Consumer<Command> processing) {
		return processing::accept;
	}

	default void process(final Command value) {
		accept(value);
	}
}
