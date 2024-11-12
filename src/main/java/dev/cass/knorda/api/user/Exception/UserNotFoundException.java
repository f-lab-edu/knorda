package dev.cass.knorda.api.user.Exception;

public class UserNotFoundException extends IllegalArgumentException{
	private final static String message = "존재하지 않는 사용자입니다.";
	public UserNotFoundException(){
		super(message);
	}
}
