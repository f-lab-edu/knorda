package dev.cass.knorda.global.exception;

public class NullSessionExeption extends IllegalArgumentException{
	private static final String message = "세션이 존재하지 않습니다.";
	public NullSessionExeption(){
		super(message);
	}
}
