package com.shyam.exceptionhandling.exception;

public class BadRequest extends Exception {
	private static final long serialVersionUID = -9079454849611061074L;
	
	public BadRequest() {
		super();
	}
	
	public BadRequest(final String message) {
		super(message);
	}
}
