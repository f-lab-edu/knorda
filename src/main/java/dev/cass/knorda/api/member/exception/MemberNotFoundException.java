package dev.cass.knorda.api.member.exception;

import dev.cass.knorda.global.exception1.BusinessException;

public class MemberNotFoundException extends BusinessException {
	private static final String MESSAGE = "존재하지 않는 사용자입니다.";
	private static final int STATUS = 404;

	public MemberNotFoundException() {
		super(STATUS, MESSAGE);
	}
}
