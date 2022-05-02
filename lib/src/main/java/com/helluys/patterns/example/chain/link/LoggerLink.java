package com.helluys.patterns.example.chain.link;

import com.helluys.patterns.chain.link.EndLink;

final class LoggerLink implements EndLink<String> {

	private final String prefix;

	public LoggerLink(final String prefix) {
		this.prefix = prefix;
	}

	@Override
	public final void accept(final String t) {
		System.out.println(prefix + "'" + t + "'");
	}
}
