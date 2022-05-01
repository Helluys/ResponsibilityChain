package com.helluys.patterns.example.chain.command;

public class ErroneousCommand extends Command {
	private final String error;

	public ErroneousCommand(final Command command, final String error) {
		super(command.source(), command.payload());
		this.error = error;
	}

	public final String error() {
		return error;
	}
}
