package com.revature.exceptions;

public class TransactionClosedException extends RuntimeException {
	public TransactionClosedException() {
		super();
	}

	public TransactionClosedException(String message) {
		super(message);
	}
}
