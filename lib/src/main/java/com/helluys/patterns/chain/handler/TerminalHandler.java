package com.helluys.patterns.chain.handler;

import java.util.function.Consumer;

/**
 * A terminal {@link Handler} that applies its processing on the condition.
 *
 * @author Helluys
 *
 * @param <Command> type of the command object
 * @param <Result>  type of the processing result
 */
final class TerminalHandler<Command> implements Handler<Command> {

	private final Consumer<Command> processing;

	public TerminalHandler(final Consumer<Command> processing) {
		this.processing = processing;
	}

	@Override
	public void handle(final Command command) {
		processing.accept(command);
	}
}
