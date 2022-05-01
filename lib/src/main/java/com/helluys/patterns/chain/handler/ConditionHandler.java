package com.helluys.patterns.chain.handler;

import java.util.function.Predicate;

/**
 * A conditional {@link Handler} that applies its processing if a condition on
 * the command is true, and passes the command to the next handler otherwise.
 *
 * @author Helluys
 *
 * @param <Command> type of the command object
 * @param <Result>  type of the processing result
 */
final class ConditionHandler<Command> implements Handler<Command> {

	private final Predicate<Command> condition;
	private final Handler<Command> trueHandler;
	private final Handler<Command> falseHandler;

	public ConditionHandler(final Predicate<Command> condition, final Handler<Command> trueHandler,
			final Handler<Command> falseHandler) {
		this.condition = condition;
		this.trueHandler = trueHandler;
		this.falseHandler = falseHandler;
	}

	@Override
	public void handle(final Command command) {
		if (condition.test(command)) {
			trueHandler.handle(command);
		} else {
			falseHandler.handle(command);
		}
	}
}
