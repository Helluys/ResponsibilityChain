package com.helluys.patterns.example.chain.command;

public class Command {
	private final String source;
	private final String payload;

	public Command(final String source, final String payload) {
		this.source = source;
		this.payload = payload;
	}

	public String source() {
		return source;
	}

	public String payload() {
		return payload;
	}
}
