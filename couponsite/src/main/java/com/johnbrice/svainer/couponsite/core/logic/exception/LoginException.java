package com.johnbrice.svainer.couponsite.core.logic.exception;

public class LoginException extends RuntimeException {

	private static final long serialVersionUID = 7754116371172077852L;

	public LoginException(String errorMessage) {
		super(errorMessage);
	}

}
