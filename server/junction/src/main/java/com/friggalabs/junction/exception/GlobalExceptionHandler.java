package com.friggalabs.junction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.friggalabs.junction.util.ResponseStructure;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ResponseStructure<String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {

//		ResponseStructure<String> structure = new ResponseStructure<>();
//		
//		structure.setData(ex.getMessage());
//		structure.setMessage("FAILURE");
//		structure.setStatusCode(HttpStatus.CONFLICT.value());

		return new ResponseEntity<>(
				new ResponseStructure<String>(ex.getMessage(), HttpStatus.CONFLICT.value(), "FAILURE"),
				HttpStatus.CONFLICT);

	}

	@ExceptionHandler(ServerErrorException.class)
	public ResponseEntity<ResponseStructure<String>> handleServerErrorException(ServerErrorException ex) {

		return new ResponseEntity<>(new ResponseStructure<String>(ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(), "FAILED TO CREATE THE USER"),
				HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleUserNotFoundException(UserNotFoundException ex) {

		return new ResponseEntity<>(
				new ResponseStructure<String>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "FAILED"),
				HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(DocumentNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleDocumentNotFoundException(DocumentNotFoundException ex) {
		return new ResponseEntity<>(
				new ResponseStructure<String>(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "FAILED"),
				HttpStatus.NOT_FOUND);
	}

}
