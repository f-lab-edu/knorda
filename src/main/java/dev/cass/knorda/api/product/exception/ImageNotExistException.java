package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class ImageNotExistException extends BusinessException {
	public ImageNotExistException() {
		super(HttpStatus.NOT_FOUND, "존재하지 않는 이미지입니다.");
	}
}
