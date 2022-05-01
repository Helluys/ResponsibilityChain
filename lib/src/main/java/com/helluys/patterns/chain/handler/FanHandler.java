package com.helluys.patterns.chain.handler;

import java.util.EnumMap;
import java.util.function.Function;

/**
 * A {@link Handler} that fans to multiple handlers based on an enumerated
 * property of the command.
 *
 * @author Helluys
 *
 * @param <Type>    type of the enumerated property
 * @param <Command> type of the command object
 * @param <Result>  type of the processing result
 */
final class FanHandler<Command, Type extends Enum<Type>> implements Handler<Command> {

	private final Function<Command, Type> typeExtractor;
	private final EnumMap<Type, Handler<Command>> fanner;

	public FanHandler(final Function<Command, Type> typeExtractor, final EnumMap<Type, Handler<Command>> fanner) {
		this.typeExtractor = typeExtractor;
		this.fanner = fanner;
	}

	@Override
	public void handle(final Command command) {
		fanner.get(typeExtractor.apply(command)).handle(command);
	}
}
