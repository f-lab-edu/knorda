package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class FileIsNullException extends BusinessException {
	public FileIsNullException() {
		super(HttpStatus.BAD_REQUEST, "파일이 비어있습니다.");
	}
}
