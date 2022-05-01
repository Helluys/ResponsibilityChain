package com.helluys.patterns.chain.handler;

import com.helluys.patterns.common.Either;

final class EitherHandler<CommandL, CommandR> implements Handler<Either<CommandL, CommandR>> {

	private final Handler<CommandL> leftHandler;
	private final Handler<CommandR> rightHandler;

	public EitherHandler(final Handler<CommandL> leftHandler, final Handler<CommandR> rightHandler) {
		this.leftHandler = leftHandler;
		this.rightHandler = rightHandler;
	}

	@Override
	public void handle(final Either<CommandL, CommandR> command) {
		command.consume(leftHandler::handle, rightHandler::handle);
	}
}
