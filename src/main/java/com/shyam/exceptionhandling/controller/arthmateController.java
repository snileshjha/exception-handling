package com.shyam.exceptionhandling.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shyam.config.JwtTokenProvider;
import com.shyam.exceptionhandling.dto.UserDefinedExceptionResponse;
import com.shyam.exceptionhandling.exception.BadRequest;
import com.shyam.exceptionhandling.exception.UserDefinedException;




@ControllerAdvice
public class arthmateController {


	@Autowired
	private JwtTokenProvider tokenProvider;
	
	private static final Logger LOGGER = LogManager.getLogger(arthmateController.class);
	private static final String FAILED = "failed";
	private static final String STATUS = "status";
	
	
	
	@ExceptionHandler(BadRequest.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleExceptions(final BadRequest exception, final HttpServletRequest request) {

		LOGGER.error("Controller->>> handleException Exception  Details--> {}", exception);
		Map<String, Object> error = setCommonExceptionCode(exception, request);

		return new ResponseEntity<>(error, HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleException(final Exception exception, final HttpServletRequest request) {

		LOGGER.error("Controller->>> handleException Exception  Details--> {}", exception);
		Map<String, Object> error = setCommonExceptionCode(exception, request);

		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(IOException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleException(final IOException exception, final HttpServletRequest request) {

		LOGGER.error("Controller->>> handleException IOException Details--> {}", exception);
		Map<String, Object> error = setCommonExceptionCode(exception, request);

		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(ServletException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleException(final ServletException exception, final HttpServletRequest request) {

		LOGGER.error("Controller->>> handleException ServletException Details--> {}", exception);
		Map<String, Object> error = setCommonExceptionCode(exception, request);

		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(SQLException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleException(final SQLException exception, final HttpServletRequest request) {

		LOGGER.error("Controller->>> handleException SQLException Details--> {}", exception);
		Map<String, Object> error = setCommonExceptionCode(exception, request);

		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(ClassNotFoundException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleException(final ClassNotFoundException exception, final HttpServletRequest request) {

		LOGGER.error("Controller->>> handleException ClassNotFoundException Details--> {}", exception);
		Map<String, Object> error = setCommonExceptionCode(exception, request);

		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(UserDefinedException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<?> handleException(final UserDefinedException exception, final HttpServletRequest request)  {

		LOGGER.error("Controller->>> handleException UserDefinedException Details--> {}", exception);
		Map<String, Object> mapError = new HashMap<>();
		UserDefinedExceptionResponse error = new UserDefinedExceptionResponse();
		error.setErrorMessage(exception.getMessage());
		error.setRequestedURI(request.getRequestURI());
		mapError.put(STATUS, "failed");
		mapError.put("error", error);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		String jwt = null;
		
		// This code is for Handling nbfcToken  
		try {
			 jwt = "Bearer " + tokenProvider.generateToken();			
			
		} catch (UserDefinedException e) {
			LOGGER.error("Controller->>> handleException UserDefinedException Details--> {}", e);
		} catch (Exception e) {
			LOGGER.error("Controller->>> handleException UserDefinedException Details--> {}", e);
		}
		
		responseHeaders.add("Authentication-Token", jwt);
		mapError.put("arthmateToken", jwt);
		return ResponseEntity.ok().headers(responseHeaders).body(mapError);
	}

	
	// field level validation
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex,
			final HttpServletRequest request) {
		//InputFieldValidation inputFieldValidation = new InputFieldValidation();

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
			LOGGER.error(
					"Controller->>> handleValidationExceptions MethodArgumentNotValidException Details--> {}",
					error);
		});

		/* change for Proper Error Message */
		String errorObj = new String();
		for (Map.Entry<String, String> entry : errors.entrySet()) {
			
			errorObj=errorObj+""+entry.getValue();  
		}

		Map<String, Object> mapError = new HashMap<>();
		UserDefinedExceptionResponse error = new UserDefinedExceptionResponse();
		error.setErrorMessage(errorObj);
		error.setRequestedURI(request.getRequestURI());
		mapError.put(STATUS, "failed");
		mapError.put("error", error);

		HttpHeaders responseHeaders = new HttpHeaders();
		String jwt = null;
		
		// This code is for Handling arthmateToken  
		try {
			 jwt = "Bearer " + tokenProvider.generateToken();			
			
		} catch (UserDefinedException e) {

			LOGGER.error("Controller->>> handleException MethodArgumentNotValidException Details--> {}", e);
		} catch (Exception e) {

			LOGGER.error("Controller->>> handleException MethodArgumentNotValidException Details--> {}", e);
		}
		
		responseHeaders.add("Authentication-Token", jwt);
		mapError.put("arthmateToken", jwt);
		return ResponseEntity.ok().headers(responseHeaders).body(mapError);
	}

	public Map<String, Object> setCommonExceptionCode(Exception exception, HttpServletRequest request) {
		Map<String, Object> mapError = new HashMap<>();
		UserDefinedExceptionResponse error = new UserDefinedExceptionResponse();
		error.setErrorMessage("Internal Server Error");
		error.setRequestedURI(request.getRequestURI());
		mapError.put(STATUS, FAILED);
		mapError.put("error", error);
		mapError.put("arthmateToken", null);
		return mapError;

	}
}
