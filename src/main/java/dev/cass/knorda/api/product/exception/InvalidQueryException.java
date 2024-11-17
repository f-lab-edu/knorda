package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class InvalidQueryException extends BusinessException {
	public InvalidQueryException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
