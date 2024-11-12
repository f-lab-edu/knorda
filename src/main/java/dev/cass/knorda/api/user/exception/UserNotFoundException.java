package dev.cass.knorda.api.user.exception;

public class UserNotFoundException extends IllegalArgumentException{
	private static final String message = "존재하지 않는 사용자입니다.";
	public UserNotFoundException(){
		super(message);
	}
}
