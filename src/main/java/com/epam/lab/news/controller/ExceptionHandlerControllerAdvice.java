package com.epam.lab.news.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.lab.news.model.ErrorInfo;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

	private static final Logger LOG = Logger
			.getLogger(ExceptionHandlerControllerAdvice.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorInfo handleValidationException(
			MethodArgumentNotValidException ex) {
		ExceptionHandlerControllerAdvice.LOG.error(
				MessageError.HANDLE_VALIDATION_EXCEPTION.getMessage(), ex);
		List<FieldError> fieldError = ex.getBindingResult().getFieldErrors();
		List<String> errorMessage = new ArrayList<>();
		for (FieldError error : fieldError) {
			errorMessage.add(error.getDefaultMessage());
		}
		return new ErrorInfo(
				MessageError.HANDLE_VALIDATION_EXCEPTION.getMessage(),
				errorMessage);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorInfo handleException(Exception ex) {
		ExceptionHandlerControllerAdvice.LOG.error(
				MessageError.HANDLE_EXCEPTION.getMessage(), ex);
		return new ErrorInfo(MessageError.HANDLE_EXCEPTION.getMessage());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorInfo handleEntityNotFoundException(EntityNotFoundException ex) {
		ExceptionHandlerControllerAdvice.LOG.error(
				MessageError.HANDLE_NOT_FOUND_EXCEPTION.getMessage(), ex);
		return new ErrorInfo(
				MessageError.HANDLE_NOT_FOUND_EXCEPTION.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	@ResponseBody
	public ErrorInfo handleMethodNotSupportedException(
			HttpRequestMethodNotSupportedException ex) {
		ExceptionHandlerControllerAdvice.LOG.error(
				MessageError.HANDLE_METHOD_NOT_SUPPORTED.getMessage(), ex);
		return new ErrorInfo(
				MessageError.HANDLE_METHOD_NOT_SUPPORTED.getMessage());
	}

	public enum MessageError {

		HANDLE_VALIDATION_EXCEPTION("Arguments of method isn't valid!"), HANDLE_EXCEPTION(
				"Internal application error!"), HANDLE_NOT_FOUND_EXCEPTION(
				"Such related entity doesn't exist!"), HANDLE_METHOD_NOT_SUPPORTED(
								"This http request method not supported!");

		private String message;

		MessageError(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
}
