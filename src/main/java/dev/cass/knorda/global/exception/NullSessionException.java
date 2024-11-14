package dev.cass.knorda.global.exception;

public class NullSessionException extends IllegalArgumentException {
	private static final String MESSAGE = "세션이 존재하지 않습니다.";

	public NullSessionException() {
		super(MESSAGE);
	}
}
