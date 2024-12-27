package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class FileExtentionInvalidException extends BusinessException {
	public FileExtentionInvalidException() {
		super(HttpStatus.BAD_REQUEST, "파일 확장자가 유효하지 않습니다.");
	}
}
