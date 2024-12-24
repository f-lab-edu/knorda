package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class ProductImageAlreadyExistException extends BusinessException {
	public ProductImageAlreadyExistException() {
		super(HttpStatus.BAD_REQUEST, "이미지가 등록되어 있습니다.");
	}
}
