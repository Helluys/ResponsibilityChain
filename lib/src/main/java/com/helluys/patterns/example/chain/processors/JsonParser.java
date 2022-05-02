package com.helluys.patterns.example.chain.processors;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.helluys.patterns.common.Either;
import com.helluys.patterns.example.chain.command.Command;
import com.helluys.patterns.example.chain.command.ErroneousCommand;
import com.helluys.patterns.example.chain.command.NamedCommand;

public final class JsonParser {

	private final Set<String> sources;

	public JsonParser(final Set<String> sources) {
		this.sources = sources;
	}

	public final boolean isJson(final Command c) {
		return sources.contains(c.source());
	}

	public static final Either<ErroneousCommand, NamedCommand> parse(final Command command) {
		try {
			final JSONObject jsonObject = new JSONObject(command.payload());
			final String name = jsonObject.getString("name");
			final String text = jsonObject.getString("text");

			return Either.ofRight(new NamedCommand(command, name, text));
		} catch (final JSONException e) {
			return Either.ofLeft(new ErroneousCommand(command, "Invalid JSON " + e.getMessage()));
		}
	}
}
