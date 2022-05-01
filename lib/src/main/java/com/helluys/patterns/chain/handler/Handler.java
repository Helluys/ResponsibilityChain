package com.helluys.patterns.chain.handler;

import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.helluys.patterns.common.Either;

/**
 * A generic chain of responsibility node. Processes a command object into a
 * result type.
 *
 * @author Helluys
 */
public interface Handler<Command> {
	void handle(Command command);

	static <Command> Handler<Command> condition(final Predicate<Command> condition, final Handler<Command> trueHandler,
			final Handler<Command> falseHandler) {
		return new ConditionHandler<Command>(condition, trueHandler, falseHandler);
	}

	static <CommandL, CommandR> Handler<Either<CommandL, CommandR>> either(final Handler<CommandL> leftHandler,
			final Handler<CommandR> rightHandler) {
		return new EitherHandler<CommandL, CommandR>(leftHandler, rightHandler);
	}

	static <Command1, Command2> Handler<Command1> map(final Function<Command1, Command2> mapping,
			final Handler<Command2> next) {
		return new MapperHandler<Command1, Command2>(mapping, next);
	}

	static <Command, Type extends Enum<Type>> Handler<Command> fan(final Function<Command, Type> typeExtractor,
			final EnumMap<Type, Handler<Command>> fanner) {
		return new FanHandler<Command, Type>(typeExtractor, fanner);
	}

	static <Command> Handler<Command> terminal(final Consumer<Command> processing) {
		return new TerminalHandler<Command>(processing);
	}
}
