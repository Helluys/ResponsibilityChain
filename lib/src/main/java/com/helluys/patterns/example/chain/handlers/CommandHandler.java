package com.helluys.patterns.example.chain.handlers;

import static java.util.stream.Collectors.toList;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.helluys.patterns.chain.handler.Handler;
import com.helluys.patterns.common.Either;
import com.helluys.patterns.example.chain.command.Command;
import com.helluys.patterns.example.chain.command.CommandA;
import com.helluys.patterns.example.chain.command.CommandB;
import com.helluys.patterns.example.chain.command.CommandC;
import com.helluys.patterns.example.chain.command.CommandType;
import com.helluys.patterns.example.chain.command.ErroneousCommand;
import com.helluys.patterns.example.chain.command.NamedCommand;
import com.helluys.patterns.example.chain.command.TypedCommand;
import com.helluys.patterns.example.chain.processors.JsonParser;
import com.helluys.patterns.example.chain.processors.TextParser;

public final class CommandHandler {

	private final Handler<Command> handler;

	public CommandHandler() {
		final Handler<ErroneousCommand> erroneousHandler = Handler
				.terminal(c -> System.out.println("Command rejected, " + c.error()));

		final EnumMap<CommandType, Consumer<TypedCommand>> typedHandlers = new EnumMap<>(CommandType.class);
		typedHandlers.put(CommandType.A,
				Handler.<TypedCommand>terminal(c -> System.out.println("Command A " + c.text()))::handle);
		typedHandlers.put(CommandType.B,
				Handler.<TypedCommand>terminal(c -> System.out.println("Command B " + c.text()))::handle);
		typedHandlers.put(CommandType.C,
				Handler.<TypedCommand>terminal(c -> System.out.println("Command C " + c.text()))::handle);

		final Handler<NamedCommand> commandProcessor = Handler.map(CommandHandler::typeCommand,
				Handler.either(erroneousHandler, Handler.fan(TypedCommand::type, null)));

		final Handler<Command> unknownSource = Handler
				.terminal(c -> System.out.println("Command rejected, unknown source " + c.source()));

		final List<String> jsonSources = Stream.of("json1", "json2").collect(toList());
		final Handler<Command> jsonHandler = Handler.condition(c -> jsonSources.contains(c.source()),
				Handler.map(JsonParser::parse, Handler.either(erroneousHandler, commandProcessor))::handle,
				unknownSource);

		final List<String> textSources = Stream.of("text1", "text2").collect(toList());
		handler = Handler.condition(c -> textSources.contains(c.source()),
				Handler.map(TextParser::parse, Handler.either(erroneousHandler, commandProcessor))::handle,
				jsonHandler);
	}

	public void handle(final String source, final String command) {
		handler.handle(new Command(source, command));
	}

	private static final Either<ErroneousCommand, TypedCommand> typeCommand(final NamedCommand named) {
		try {
			switch (CommandType.valueOf(named.name())) {
				case A:
					return Either.ofRight(new CommandA(named));
				case B:
					return Either.ofRight(new CommandB(named));
				case C:
					return Either.ofRight(new CommandC(named));
				default:
					throw new UnsupportedOperationException(
							"Unmanaged command type " + CommandType.valueOf(named.name()));
			}
		} catch (final IllegalArgumentException e) {
			return Either.ofLeft(new ErroneousCommand(named, "Illegal command type " + named.name()));
		}
	}
}
