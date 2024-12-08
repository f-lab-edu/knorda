package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class FileSaveFailedException extends BusinessException {
	public FileSaveFailedException() {
		super(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다.");
	}
}
