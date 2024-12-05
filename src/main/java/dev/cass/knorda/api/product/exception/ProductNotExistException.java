package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class ProductNotExistException extends BusinessException {
	public ProductNotExistException() {
		super(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다.");
	}
}
