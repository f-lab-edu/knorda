package dev.cass.knorda.api.member.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class MemberNotFoundException extends BusinessException {
	public MemberNotFoundException() {
		super(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");
	}
}
