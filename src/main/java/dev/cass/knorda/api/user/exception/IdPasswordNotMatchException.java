package dev.cass.knorda.api.user.exception;

public class IdPasswordNotMatchException extends IllegalArgumentException{
	private static final String message = "아이디와 비밀번호를 확인해주세요.";
	public IdPasswordNotMatchException(){
		super(message);
	}
}