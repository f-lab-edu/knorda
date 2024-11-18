package dev.cass.knorda.api.member.exception;

import dev.cass.knorda.global.exception1.BusinessException;

public class IdPasswordNotMatchException extends BusinessException {
	private static final String MESSAGE = "아이디와 비밀번호를 확인해주세요.";
	private static final int STATUS = 400;

	public IdPasswordNotMatchException() {
		super(STATUS, MESSAGE);
	}
}
