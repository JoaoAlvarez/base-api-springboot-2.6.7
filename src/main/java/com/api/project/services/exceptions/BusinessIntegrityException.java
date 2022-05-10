package com.api.project.services.exceptions;

public class BusinessIntegrityException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BusinessIntegrityException(String msg) {
		super(msg);
	}

	public BusinessIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
