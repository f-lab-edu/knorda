package dev.cass.knorda.api.product.exception;

import org.springframework.http.HttpStatus;

import dev.cass.knorda.global.exception1.BusinessException;

public class NotYourProductException extends BusinessException {
	public NotYourProductException() {
		super(HttpStatus.UNAUTHORIZED, "자신의 상품이 아니라서 변경할 수 없습니다.");
	}
}
