package com.helluys.patterns.example.chain.processors;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.helluys.patterns.common.Either;
import com.helluys.patterns.example.chain.command.Command;
import com.helluys.patterns.example.chain.command.ErroneousCommand;
import com.helluys.patterns.example.chain.command.NamedCommand;

public final class TextParser {
	private static final Pattern TEXT_PATTERN = Pattern.compile("\\[([A-Z]*)](.*)");

	private final Set<String> sources;

	public TextParser(final Set<String> sources) {
		this.sources = sources;
	}

	public final boolean isText(final Command c) {
		return sources.contains(c.source());
	}

	public static final Either<ErroneousCommand, NamedCommand> parse(final Command command) {
		final Matcher matcher = TextParser.TEXT_PATTERN.matcher(command.payload());
		if (matcher.matches() && matcher.groupCount() == 2) {
			final String name = matcher.group(1);
			final String text = matcher.group(2);

			return Either.ofRight(new NamedCommand(command, name, text));
		}
		return Either.ofLeft(new ErroneousCommand(command, "Invalid text " + command.source()));
	}
}
