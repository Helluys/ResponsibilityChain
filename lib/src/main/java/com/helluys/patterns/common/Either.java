package com.helluys.patterns.common;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Either a left or a right.
 *
 * @param <L> the left type
 * @param <R> the right type
 *
 * @author Helluys
 */
public interface Either<L, R> {

	/**
	 * Construct a left valued {@link Either}.
	 *
	 * @param <L>   the left type
	 * @param <R>   the right type
	 * @param value the left value
	 * @return a left valued {@link Either}
	 */
	static <L, R> Either<L, R> ofLeft(final L value) {
		return new Either<L, R>() {

			@Override
			public boolean isLeft() {
				return true;
			}

			@Override
			public L left() {
				return value;
			}

			@Override
			public R right() {
				throw new IllegalStateException("Either is a right");
			}
		};
	}

	/**
	 * Construct a right valued {@link Either}.
	 *
	 * @param <L>   the left type
	 * @param <R>   the right type
	 * @param value the right value
	 * @return a right valued {@link Either}
	 */
	static <L, R> Either<L, R> ofRight(final R value) {
		return new Either<L, R>() {

			@Override
			public boolean isLeft() {
				return false;
			}

			@Override
			public L left() {
				throw new IllegalStateException("Either is a right");
			}

			@Override
			public R right() {
				return value;
			}
		};
	}

	/**
	 * @return true if this is a left valued {@link Either}
	 */
	boolean isLeft();

	/**
	 * @return the left value if this is a left valued {@link Either}
	 * @throws IllegalStateException if this is a right value {@link Either}
	 */
	L left();

	/**
	 * Returns the left value if this is a left valued {@link Either}, or the given
	 * value otherwise.
	 *
	 * @param value the default value
	 * @return the left value if this is a left valued {@link Either}, or the given
	 *         value otherwise
	 */
	default L leftOrElse(final L value) {
		return isLeft() ? left() : value;
	}

	/**
	 * Returns the left value if this is a left valued {@link Either}, or the
	 * supplied value otherwise.
	 *
	 * @param value the default value
	 * @return the left value if this is a left valued {@link Either}, or the
	 *         supplied value otherwise
	 */
	default L leftOrElseGet(final Supplier<L> supplier) {
		return isLeft() ? left() : supplier.get();
	}

	/**
	 * @return true if this is a right valued {@link Either}
	 */
	default boolean isRight() {
		return !isLeft();
	}

	/**
	 * @return the right value if this is a right valued {@link Either}
	 * @throws IllegalStateException if this is a left value {@link Either}
	 */
	R right();

	/**
	 * Returns the right value if this is a right valued {@link Either}, or the
	 * given value otherwise.
	 *
	 * @param value the default value
	 * @return the right value if this is a right valued {@link Either}, or the
	 *         given value otherwise
	 */
	default R rightOrElse(final R value) {
		return isRight() ? right() : value;
	}

	/**
	 * Returns the right value if this is a right valued {@link Either}, or the
	 * supplied value otherwise.
	 *
	 * @param value the default value
	 * @return the right value if this is a right valued {@link Either}, or the
	 *         supplied value otherwise
	 */
	default R rightOrElseGet(final Supplier<R> supplier) {
		return isRight() ? right() : supplier.get();
	}

	/**
	 * Map the left or right value to new types.
	 *
	 * @param <TL>        the resulting left type
	 * @param <TR>        the resulting right type
	 * @param leftMapper  the mapping function in case this {@link Either} is left
	 *                    valued
	 * @param rightMapper the mapping function in case this {@link Either} is right
	 *                    valued
	 * @return the reduced value
	 */
	default <TL, TR> Either<TL, TR> map(final Function<L, TL> leftMapper, final Function<R, TR> rightMapper) {
		return isLeft() ? ofLeft(leftMapper.apply(left())) : ofRight(rightMapper.apply(right()));
	}

	/**
	 * Reduce the value to a common type.
	 *
	 * @param <T>         the resulting type
	 * @param leftMapper  the mapping function in case this {@link Either} is left
	 *                    valued
	 * @param rightMapper the mapping function in case this {@link Either} is right
	 *                    valued
	 * @return the reduced value
	 */
	default <T> T reduce(final Function<L, T> leftMapper, final Function<R, T> rightMapper) {
		return isLeft() ? leftMapper.apply(left()) : rightMapper.apply(right());
	}

	/**
	 * Consumer either values.
	 *
	 * @param leftConsumer  the consuming function in case this {@link Either} is
	 *                      left valued
	 * @param rightConsumer the consuming function in case this {@link Either} is
	 *                      right valued
	 */
	default void consume(final Consumer<L> leftConsumer, final Consumer<R> rightConsumer) {
		if (isLeft()) {
			leftConsumer.accept(left());
		} else {
			rightConsumer.accept(right());
		}
	}
}
