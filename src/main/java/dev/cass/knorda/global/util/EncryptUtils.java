package dev.cass.knorda.global.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Slf4j - Lombok의 어노테이션. 로깅을 위한 Logger를 자동으로 생성
 * Component - 스프링의 어노테이션. 해당 클래스가 Component임을 명시
 * 추후 실행 시, component-scan을 통해 해당하는 클래스를 찾아서 Bean으로 등록하게 되는데,
 * Service, Repository, Controller, Component 어노테이션이 붙은 클래스들이 Bean으로 등록되게 된다.
 */
@Slf4j
@Component
public final class EncryptUtils {
	private static String SALT;

	/**
	 * Value - 해당 필드에 주입할 값을 지정하는 어노테이션
	 * Value("${encrypt.salt}") - application.properties, 혹은 yaml에 정의된 encrypt.salt 값을 매개 변수로 주입
	 * 원래는 변수에 붙이면 되나, static 변수에 해당 값을 초기화시키기 위해 setter 메소드를 생성해서 메소드에 붙여줌
	 * 왜냐하면 @Value
	 */
	@Value("${encrypt.salt}")
	public void setSalt(String salt) {
		EncryptUtils.SALT = salt;
	}

	public static String encryptSHA256(String input) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
			md.update(SALT.getBytes());
			return hashing(input, md);
		} catch (java.security.NoSuchAlgorithmException e) {
			log.error("암호화를 실패했습니다. in {} message {}", input, e.getMessage());
			throw new IllegalArgumentException("암호화를 실패했습니다. 다시 시도해주세요");
		}
	}

	private static String hashing(String input, MessageDigest md) {
		byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
		BigInteger no = new BigInteger(1, messageDigest);
		StringBuilder encryptHash = new StringBuilder(no.toString(16));
		while (encryptHash.length() < 32) {
			encryptHash.insert(0, "0");
		}
		return encryptHash.toString();
	}
}
