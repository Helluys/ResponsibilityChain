package com.helluys.patterns.example.chain.processors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.helluys.patterns.common.Either;
import com.helluys.patterns.example.chain.command.Command;
import com.helluys.patterns.example.chain.command.ErroneousCommand;
import com.helluys.patterns.example.chain.command.NamedCommand;

public final class JsonParser {
	private JsonParser() {
		// Nothing
	}

	public static final Either<ErroneousCommand, NamedCommand> parse(final Command command) {
		try {
			final JSONObject jsonObject = new JSONArray(command.payload()).getJSONObject(0);
			final String name = jsonObject.getString("name");
			final String text = jsonObject.getString("text");
	
			return Either.ofRight(new NamedCommand(command, name, text));
		} catch (final JSONException e) {
			return Either.ofLeft(new ErroneousCommand(command, "Invalid JSON " + e.getMessage()));
		}
	}
}
