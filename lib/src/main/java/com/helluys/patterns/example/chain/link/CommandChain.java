package com.helluys.patterns.example.chain.link;

import java.util.Set;
import java.util.function.Consumer;

import com.helluys.patterns.chain.link.EndLink;
import com.helluys.patterns.chain.link.EnumLink;
import com.helluys.patterns.chain.link.Link;
import com.helluys.patterns.example.chain.command.Command;
import com.helluys.patterns.example.chain.command.CommandA;
import com.helluys.patterns.example.chain.command.CommandB;
import com.helluys.patterns.example.chain.command.CommandC;
import com.helluys.patterns.example.chain.command.CommandType;
import com.helluys.patterns.example.chain.command.ErroneousCommand;
import com.helluys.patterns.example.chain.command.NamedCommand;
import com.helluys.patterns.example.chain.processors.JsonParser;
import com.helluys.patterns.example.chain.processors.TextParser;

public final class CommandChain {

	private final Consumer<Command> processor;

	private final TextParser textParser;
	private final JsonParser jsonParser;

	public CommandChain(final Set<String> textSources, final Set<String> jsonSources) {
		this.textParser = new TextParser(textSources);
		this.jsonParser = new JsonParser(jsonSources);

		final EndLink<ErroneousCommand> erroneousProcessor = Link.of(ErroneousCommand::error,
				new LoggerLink("erroneous command : "));

		final EndLink<NamedCommand> commandProcessor = new EnumLink<>(CommandType::from)
				.with(CommandType.A, Link.of(CommandA::new, Link.of(NamedCommand::text, new LoggerLink("A : "))))
				.with(CommandType.B, Link.of(CommandB::new, Link.of(NamedCommand::text, new LoggerLink("B : "))))
				.with(CommandType.C, Link.of(CommandC::new, Link.of(NamedCommand::text, new LoggerLink("C : "))))
				.otherwise(Link.of(NamedCommand::name, new LoggerLink("unknown command name : ")));

		processor = Link
				.<Command> of(textParser::isText, Link.of(TextParser::parse, erroneousProcessor, commandProcessor))
				.chain(Link.of(jsonParser::isJson, Link.of(JsonParser::parse, erroneousProcessor, commandProcessor)))
				.chain(Link.of(Command::source, new LoggerLink("unknown command source : ")));
	}

	public final void process(final String source, final String payload) {
		processor.accept(new Command(source, payload));
	}
}
