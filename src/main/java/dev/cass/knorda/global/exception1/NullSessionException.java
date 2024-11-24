package dev.cass.knorda.global.exception1;

import org.springframework.http.HttpStatus;

public class NullSessionException extends BusinessException {
	public NullSessionException() {
		super(HttpStatus.UNAUTHORIZED, "세션이 존재하지 않습니다.");
	}
}
