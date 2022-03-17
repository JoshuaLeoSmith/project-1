package com.revature.exceptions;

public class TransactionException extends RuntimeException {
	public TransactionException() {
		super();
	}

	public TransactionException(String message) {
		super(message);
	}
}
