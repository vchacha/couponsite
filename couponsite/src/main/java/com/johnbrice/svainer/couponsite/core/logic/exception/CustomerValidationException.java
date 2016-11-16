package com.johnbrice.svainer.couponsite.core.logic.exception;

public class CustomerValidationException extends RuntimeException {

	private static final long serialVersionUID = 6479997524885282390L;

	public CustomerValidationException(String errorMessage) {
		super(errorMessage);
	}

}
