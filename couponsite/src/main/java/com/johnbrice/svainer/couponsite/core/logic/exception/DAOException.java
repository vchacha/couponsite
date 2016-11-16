package com.johnbrice.svainer.couponsite.core.logic.exception;

 
/**
 * Generic {@code RuntimeException} for all thrown {@code SQLException}
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 * 
 */

public class DAOException extends RuntimeException {
	
	private static final long serialVersionUID = 5551520704492009506L;

	public DAOException(String errorMessage) {
		super(errorMessage);
	}

}
