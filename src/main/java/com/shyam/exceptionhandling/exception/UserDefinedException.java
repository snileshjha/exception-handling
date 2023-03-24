package com.shyam.exceptionhandling.exception;

public class UserDefinedException extends Exception {
	private static final long serialVersionUID = -9079454849611061074L;
	
	public UserDefinedException() {
		super();
	}
	
	public UserDefinedException(final String message) {
		super(message);
	}
}
