package com.sender.app.exception;

public class SenderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SenderException() {
		super();
	}

	public SenderException(final String message) {
		super(message);
	}

	public SenderException(final Throwable cause) {
		super(cause);
	}

	public SenderException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
