package com.revature.exceptions;

public class SavePointNotFoundException extends RuntimeException {
	public SavePointNotFoundException() {
		super();
	}

	public SavePointNotFoundException(String message) {
		super(message);
	}
}