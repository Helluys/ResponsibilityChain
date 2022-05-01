package com.helluys.patterns.example.chain.link;

import java.util.Set;
import java.util.function.Consumer;

import com.helluys.patterns.chain.link.Link;
import com.helluys.patterns.common.Either;
import com.helluys.patterns.example.chain.command.Command;
import com.helluys.patterns.example.chain.command.ErroneousCommand;
import com.helluys.patterns.example.chain.command.NamedCommand;
import com.helluys.patterns.example.chain.processors.JsonParser;

final class JsonLink implements Link<Command> {
	private final Set<String> sources;
	private final Consumer<ErroneousCommand> erroneous;
	private final Consumer<NamedCommand> valid;

	public JsonLink(final Set<String> sources, final Consumer<ErroneousCommand> erroneous,
			final Consumer<NamedCommand> valid) {
		this.sources = sources;
		this.erroneous = erroneous;
		this.valid = valid;
	}

	@Override
	public boolean process(final Command command) {
		if (sources.contains(command.source())) {
			final Either<ErroneousCommand, NamedCommand> json = JsonParser.parse(command);
			json.consume(erroneous, valid);
			return true;
		} else {
			return false;
		}
	}
}
