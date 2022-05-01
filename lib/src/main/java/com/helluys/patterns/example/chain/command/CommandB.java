package com.helluys.patterns.example.chain.command;

public class CommandB extends TypedCommand {

	public CommandB(final NamedCommand command) {
		super(command);
	}

	@Override
	public final CommandType type() {
		return CommandType.B;
	}
}
