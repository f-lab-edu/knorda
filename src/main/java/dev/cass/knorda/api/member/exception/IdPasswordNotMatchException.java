package dev.cass.knorda.api.member.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class IdPasswordNotMatchException extends BusinessException {
	public IdPasswordNotMatchException() {
		super(HttpStatus.BAD_REQUEST, "아이디와 비밀번호를 확인해주세요.");
	}
}
