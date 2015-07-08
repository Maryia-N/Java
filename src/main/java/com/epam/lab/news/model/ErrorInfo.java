package com.epam.lab.news.model;

import java.io.Serializable;
import java.util.List;

/**
 * Entity {@code ErrorInfo} represents a data about error occurred in
 * application
 *
 * @author Maryia_Nabzdorava
 */
public class ErrorInfo implements Serializable {

	private static final long serialVersionUID = -4074119428298792054L;

	private String message;

	private List<String> errors;

	public ErrorInfo() {
	}

	public ErrorInfo(String message) {
		this.message = message;
	}

	public ErrorInfo(String message, List<String> errors) {
		this.message = message;
		this.errors = errors;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
