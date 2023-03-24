package com.shyam.exceptionhandling.dto;

import java.util.Map;

import lombok.Data;

@Data
public class UserDefinedErrorResponse {
	private String status;
	private Map<String,UserDefinedExceptionResponse> error;
	private Map<String,String> errorField;
}
