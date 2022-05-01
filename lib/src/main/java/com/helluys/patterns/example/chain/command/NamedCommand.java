package com.helluys.patterns.example.chain.command;

public class NamedCommand extends Command {
	private final String name;
	private final String text;

	public NamedCommand(final Command command, final String name, final String text) {
		super(command.source(), command.payload());
		this.name = name;
		this.text = text;
	}

	public final String name() {
		return name;
	}

	public final String text() {
		return text;
	}
}
