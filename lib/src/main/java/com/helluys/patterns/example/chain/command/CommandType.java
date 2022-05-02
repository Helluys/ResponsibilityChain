package com.helluys.patterns.example.chain.command;

public enum CommandType {
	A, B, C;

	/**
	 * Reads the {@link CommandType} from the given command name.
	 * @param command the command
	 * @return the {@link CommandType}, or {@code null} if none matched
	 */
	public static final CommandType from(final NamedCommand command) {
		try {
			return CommandType.valueOf(command.name());
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}
}
