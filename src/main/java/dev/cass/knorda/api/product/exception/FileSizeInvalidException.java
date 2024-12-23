package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class FileSizeInvalidException extends BusinessException {
	public FileSizeInvalidException() {
		super(HttpStatus.BAD_REQUEST, "파일 크기가 유효하지 않습니다.");
	}
}
