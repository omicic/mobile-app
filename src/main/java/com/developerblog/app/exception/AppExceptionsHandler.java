package com.developerblog.app.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.developerblog.app.ui.model.response.ErrorMessage;

@ControllerAdvice
public class AppExceptionsHandler {
	
	@ExceptionHandler(value = {UserServiceException.class})
	public ResponseEntity<ErrorMessage> handleUserServiceException(UserServiceException ex, WebRequest request)
	{
		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
		
		return new ResponseEntity<ErrorMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<ErrorMessage> handleOtherExceptions(Exception ex, WebRequest request)
	{
		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
		
		return new ResponseEntity<ErrorMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
