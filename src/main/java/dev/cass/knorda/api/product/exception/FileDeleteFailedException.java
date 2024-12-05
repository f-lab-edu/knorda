package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class FileDeleteFailedException extends BusinessException {
	public FileDeleteFailedException() {
		super(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다.");
	}
}
