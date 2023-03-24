package com.shyam.exceptionhandling.dto;

import lombok.Data;

@Data
public class UserDefinedExceptionResponse {
	private String errorMessage;
	private String requestedURI;	
}
