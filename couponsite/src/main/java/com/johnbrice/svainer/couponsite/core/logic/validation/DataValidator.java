package com.johnbrice.svainer.couponsite.core.logic.validation;

public interface DataValidator<T> {

	ValidationResponse validateData(T data);

}
