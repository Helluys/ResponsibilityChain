package com.helluys.patterns.chain.handler;

import java.util.function.Function;

/**
 * A mapping {@link Handler} that converts the command to another type before
 * sending it to the next handler.
 *
 * @author Helluys
 *
 * @param <Command> type of the command object
 * @param <Result>  type of the processing result
 */
final class MapperHandler<Command1, Command2> implements Handler<Command1> {

	private final Function<Command1, Command2> mapping;
	private final Handler<Command2> next;

	public MapperHandler(final Function<Command1, Command2> mapping, final Handler<Command2> next) {
		this.mapping = mapping;
		this.next = next;
	}

	@Override
	public void handle(final Command1 command) {
		next.handle(mapping.apply(command));
	}
}
