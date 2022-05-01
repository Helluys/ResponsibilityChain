package com.helluys.patterns.example.chain.command;

public class CommandC extends TypedCommand {

	public CommandC(final NamedCommand command) {
		super(command);
	}

	@Override
	public final CommandType type() {
		return CommandType.C;
	}
}
