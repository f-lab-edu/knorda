package dev.cass.knorda.global.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import dev.cass.knorda.global.exception1.NamedLockFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Update query만 다르게 사용되고, 그 전후로는 동일한 로직이 반복되므로(락 획득, 락 해제)
 * 비즈니스 로직에서 흐름이 동일한데 그 중 일부만 바뀔 때 사용하기 적합한 템플릿 콜백 패턴을 사용
 *
 * 일부만 바뀔 로직은 네임드 락으로 감쌀 작업이므로, 일반 객체가 아니라 함수형 객체를 받아야 할 필요성이 있음
 * abstract method가 단 하나만 존재하는 함수형 인터페이스를 정의하여 이를 콜백 매개변수로서 받도록 하고,
 * 실제 사용 시에는 매개 변수로 람다 표현식을 전달하여 사용
 *
 * 최종적으로 executeWithNamedLock 매소드를 호출하면
 * 1. 락을 획득
 * 2. 람다 표현식으로 넘겨준 콜백 메소드를 실행
 * 3. 락을 해제
 * 과정을 거치게 된다
 *
 * FunctionalInterface - 추상 메서드가 하나만 있는 함수형 인터페이스임을 명시
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NamedLockManager {
	private final JdbcTemplate jdbcTemplate;

	public <T> T executeWithNamedLock(String lockName, long waitTime, NamedLockCallback<T> callback) {
		boolean locked = false;
		try {
			locked = Boolean.TRUE.equals(
				jdbcTemplate.queryForObject("SELECT GET_LOCK(?, ?)", Boolean.class, lockName, waitTime));
			if (!locked) {
				log.error("Failed to get NamedLock, lockName: {}", lockName);
				throw new NamedLockFailedException();
			}
			return callback.doInLock();
		} catch (Exception e) {
			// callback method에서 error가 발생할 수도 있으므로, catch한 error를 그대로 던져줌
			log.error("NamedLock Failed, lockName: {}", lockName, e);
			throw e;
		} finally {
			if (locked) {
				jdbcTemplate.queryForObject("SELECT RELEASE_LOCK(?)", Boolean.class, lockName);
			}
		}
	}

	@FunctionalInterface
	public interface NamedLockCallback<T> {
		T doInLock();
	}
}
