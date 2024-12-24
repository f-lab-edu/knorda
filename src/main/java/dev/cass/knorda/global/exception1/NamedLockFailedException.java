package dev.cass.knorda.global.exception1;

import org.springframework.http.HttpStatus;

public class NamedLockFailedException extends BusinessException {
	public NamedLockFailedException() {
		super(HttpStatus.INTERNAL_SERVER_ERROR, "NamedLock Failed");
	}
}
