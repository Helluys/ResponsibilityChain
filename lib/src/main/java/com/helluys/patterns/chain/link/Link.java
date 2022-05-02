package com.helluys.patterns.chain.link;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.helluys.patterns.common.Either;

/**
 * A responsibility chain link. It may or may not consume the command it is
 * provided in the {@link #process} method. links may represent a single link a
 * or chain of links.
 *
 * @author Helluys
 * @param <Command> the command type
 */
@FunctionalInterface
public interface Link<Command> {

	/**
	 * Returns a new link that consumes with the given processing only if the given
	 * predicate evaluates to true.
	 *
	 * @param <Command>  the command type
	 * @param predicate  the predicate
	 * @param processing the processing
	 * @return a new link
	 */
	static <Command> Link<Command> of(final Predicate<Command> predicate, final Consumer<Command> processing) {
		return v -> {
			if (predicate.test(v)) {
				processing.accept(v);
				return true;
			} else {
				return false;
			}
		};
	}

	/**
	 * Returns a new link that first maps the command to a new type before passing
	 * it on to the provided link.
	 *
	 * @param <Command>    the returned link command type
	 * @param <NewCommand> the provided link command type
	 * @param mapper       the command mapping function
	 * @param link         the link
	 * @return a new mapped link
	 */
	static <Command, NewCommand> Link<Command> of(final Function<Command, NewCommand> mapper,
			final Link<NewCommand> link) {
		return v -> link.process(mapper.apply(v));
	}

	/**
	 * Returns a new end link that first maps the command to a new type before
	 * passing it on to the provided end link.
	 *
	 * @param <Command>    the returned link command type
	 * @param <NewCommand> the provided link command type
	 * @param mapper       the command mapping function
	 * @param link         the end link
	 * @return a new mapped end link
	 * @see EndLink
	 */
	static <Command, NewCommand> EndLink<Command> of(final Function<Command, NewCommand> mapper,
			final EndLink<NewCommand> link) {
		return v -> link.process(mapper.apply(v));
	}

	/**
	 * Returns a new link that first maps the command to a new {@link Either} type
	 * before passing it on to the correct left or right provided link.
	 *
	 * @param <Command>      the returned link command type
	 * @param <LeftCommand>  the provided left link command type
	 * @param <RightCommand> the provided left link command type
	 * @param mapper         the command mapping function
	 * @param left           the left link
	 * @param right          the right link
	 * @return a new mapped link
	 * @see Either
	 */
	static <Command, LeftCommand, RightCommand> Link<Command> of(
			final Function<Command, Either<LeftCommand, RightCommand>> mapper, final Link<LeftCommand> left,
			final Link<RightCommand> right) {
		return of(mapper, either(left, right));
	}

	/**
	 * Returns a new end link that first maps the command to a new {@link Either}
	 * type before passing it on to the correct left or right provided end link.
	 *
	 * @param <Command>      the returned link command type
	 * @param <LeftCommand>  the provided left link command type
	 * @param <RightCommand> the provided left link command type
	 * @param mapper         the command mapping function
	 * @param left           the left end link
	 * @param right          the right end link
	 * @return a new mapped end link
	 * @see EndLink
	 * @see Either
	 */
	static <T, L, R> EndLink<T> of(final Function<T, Either<L, R>> mapper, final EndLink<L> left,
			final EndLink<R> right) {
		return of(mapper, either(left, right));
	}

	/**
	 * Returns a new link for an {@link Either} command type that applies the
	 * correct left or right provided link.
	 *
	 * @param <LeftCommand>  the provided left link command type
	 * @param <RightCommand> the provided left link command type
	 * @param left           the left link
	 * @param right          the right link
	 * @return a new mapped link
	 * @see Either
	 */
	static <LeftCommand, RightCommand> Link<Either<LeftCommand, RightCommand>> either(final Link<LeftCommand> left,
			final Link<RightCommand> right) {
		return v -> v.reduce(left::process, right::process);
	}

	/**
	 * Returns a new end link for an {@link Either} command type that applies the
	 * correct left or right provided end link.
	 *
	 * @param <LeftCommand>  the provided left link command type
	 * @param <RightCommand> the provided left link command type
	 * @param mapper         the command mapping function
	 * @param left           the left end link
	 * @param right          the right end link
	 * @return a new mapped end link
	 * @see EndLink
	 * @see Either
	 */
	static <LeftCommand, RigthCommand> EndLink<Either<LeftCommand, RigthCommand>> either(
			final EndLink<LeftCommand> left, final EndLink<RigthCommand> right) {
		return v -> v.consume(left, right);
	}

	/**
	 * Attempts to process the command.
	 *
	 * @param command the command
	 * @return {@code true} if the command was processed
	 */
	boolean process(Command command);

	/**
	 * Returns a new link that is made of the chain of this link and the provided
	 * one. {@link #process} will be executed first on the this link, and passed on
	 * the next link if the not yet consumed. This method is used to create a chain
	 * of responsibility from individual links or sub-chains.
	 *
	 * @param next the next link
	 * @return a chain made of this link and the provided one
	 */
	default Link<Command> chain(final Link<Command> next) {
		return v -> process(v) ? true : next.process(v);
	}

	/**
	 * Returns a new link that is made of the chain of this link and the provided
	 * end link. {@link #process} will be executed first on the this link, and
	 * passed on the next link if the not yet consumed. This method is used to
	 * complete a chain of responsibility from individual links or sub-chains.
	 *
	 * @param next the next link, that always consumes the command
	 * @return a completed chain made of this link and the provided one
	 */
	default EndLink<Command> chain(final EndLink<Command> next) {
		return v -> {
			if (!process(v)) {
				next.process(v);
			}
		};
	}

	/**
	 * Returns a new link that is made of the chain of this link and the provided
	 * link with a change of command type. {@link #process} will be executed first
	 * on the this link, then mapped and passed on the next link if the not yet
	 * consumed. This method is used to complete a chain of responsibility from
	 * individual links or sub-chains while changing the command type.
	 *
	 * @param <NewCommand> the new command type
	 * @param mapper       the mapping function
	 * @param next         the next link
	 * @return a chain made of this link and the provided one
	 */
	default <NewCommand> Link<Command> map(final Function<Command, NewCommand> mapper, final Link<NewCommand> next) {
		return v -> process(v) ? true : next.process(mapper.apply(v));
	}
}
