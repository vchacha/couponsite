package com.johnbrice.svainer.couponsite.core.logic.validation;

public class ValidationResponse {

	private final boolean isOk;
	private final String errorMessage;

	public ValidationResponse(boolean isOk, String errorMessage) {
		this.isOk = isOk;
		this.errorMessage = errorMessage;
	}

	public boolean isOk() {
		return isOk;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "ValidationResponse [isOk=" + isOk + ", errorMessage=" + errorMessage + "]";
	}

}
