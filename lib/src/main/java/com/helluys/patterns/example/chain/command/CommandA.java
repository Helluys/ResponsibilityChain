package com.helluys.patterns.example.chain.command;

public class CommandA extends TypedCommand {

	public CommandA(final NamedCommand command) {
		super(command);
	}

	@Override
	public final CommandType type() {
		return CommandType.A;
	}
}
