package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class FileNameInvalidException extends BusinessException {
	public FileNameInvalidException() {
		super(HttpStatus.BAD_REQUEST, "파일 이름이 유효하지 않습니다.");
	}
}
