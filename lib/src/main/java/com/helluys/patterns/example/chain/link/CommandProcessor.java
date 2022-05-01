package com.helluys.patterns.example.chain.link;

import java.util.Set;
import java.util.function.Consumer;

import com.helluys.patterns.chain.link.EnumLink;
import com.helluys.patterns.chain.link.Link;
import com.helluys.patterns.example.chain.command.Command;
import com.helluys.patterns.example.chain.command.CommandA;
import com.helluys.patterns.example.chain.command.CommandB;
import com.helluys.patterns.example.chain.command.CommandC;
import com.helluys.patterns.example.chain.command.CommandType;
import com.helluys.patterns.example.chain.command.ErroneousCommand;
import com.helluys.patterns.example.chain.command.NamedCommand;

public final class CommandProcessor {

	private final Consumer<Command> processor;

	public CommandProcessor(final Set<String> textSources, final Set<String> jsonSources) {
		final Consumer<ErroneousCommand> erroneousProcessor = Link.ofMap(ErroneousCommand::error,
				new LoggerLink("erroneous command "));

		final Consumer<NamedCommand> commandProcessor = new EnumLink<>(CommandProcessor::mapCommandType)
				.with(CommandType.A, Link.ofMap(CommandA::new, Link.ofMap(NamedCommand::text, new LoggerLink("A : "))))
				.with(CommandType.B, Link.ofMap(CommandB::new, Link.ofMap(NamedCommand::text, new LoggerLink("B : "))))
				.with(CommandType.C, Link.ofMap(CommandC::new, Link.ofMap(NamedCommand::text, new LoggerLink("C : "))))
				.endChain(Link.ofMap(NamedCommand::name, new LoggerLink("unknown command name : ")));

		processor = new TextLink(textSources, erroneousProcessor, commandProcessor)
				.chain(new JsonLink(jsonSources, erroneousProcessor, commandProcessor))
				.endChain(c -> System.out.println("unknown command source " + c.source()));
	}

	public final void process(final String source, final String payload) {
		processor.accept(new Command(source, payload));
	}

	private static final CommandType mapCommandType(final NamedCommand c) {
		return CommandType.valueOf(c.name());
	}
}
