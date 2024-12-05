package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class AlreadyExistProductNameException extends BusinessException {
	public AlreadyExistProductNameException() {
		super(HttpStatus.BAD_REQUEST, "이미 존재하는 상품명입니다.");
	}
}
