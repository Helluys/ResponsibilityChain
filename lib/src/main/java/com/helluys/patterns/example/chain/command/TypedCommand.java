package com.helluys.patterns.example.chain.command;

public abstract class TypedCommand extends NamedCommand {
	public TypedCommand(final NamedCommand command) {
		super(command, command.name(), command.text());
	}

	public abstract CommandType type();
}
