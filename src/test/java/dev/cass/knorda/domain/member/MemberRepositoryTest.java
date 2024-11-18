package dev.cass.knorda.domain.member;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

/**
 * DataJpaTest - JPA 관련 설정만 로드하여 테스트
 * AutoConfigureTestDatabase - 테스트용 데이터베이스 설정 (Replace.NONE: property를 통해 설정한 DB 사용)
 */
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	void findAllByIsDeletedFalse() {
		// when
		Pageable pageable = Pageable.ofSize(10);
		assertEquals(2, memberRepository.findAllByIsDeletedFalse(pageable).size());
	}
}