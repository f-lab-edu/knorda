package dev.cass.knorda.global.exception1;

public class NullSessionException extends BusinessException {
	private static final String MESSAGE = "세션이 존재하지 않습니다.";
	private static final int STATUS = 401;

	public NullSessionException() {
		super(STATUS, MESSAGE);
	}
}
