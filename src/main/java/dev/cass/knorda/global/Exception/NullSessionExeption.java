package dev.cass.knorda.global.Exception;

public class NullSessionExeption extends IllegalArgumentException{
	private final static String message = "세션이 존재하지 않습니다.";
	public NullSessionExeption(){
		super(message);
	}
}
