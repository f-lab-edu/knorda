package dev.cass.knorda.api.user.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.user.dto.AuthDto;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthServiceTest {
	@Autowired
	private AuthService authService;

	private MockHttpSession session = new MockHttpSession();

	@DisplayName("로그인")
	@Test
	void login() {
		// given
		AuthDto.LoginRequest request = new AuthDto.LoginRequest("admin", "admin");

		// when
		AuthDto.LoginResponse result = authService.login(request, session);

		// then
		assertEquals(request.getMemberName(), result.getMemberName());
	}

	@DisplayName("로그인 사용자 없음")
	@Test
	void loginNoUser() {
		// given
		AuthDto.LoginRequest request = new AuthDto.LoginRequest("test", "test");

		// when
		assertThrows(IllegalArgumentException.class, () -> authService.login(request, session));
	}

	@DisplayName("로그인 비밀번호 불일치")
	@Test
	void loginPasswordNotMatch() {
		// given
		AuthDto.LoginRequest request = new AuthDto.LoginRequest("admin", "admin1");

		// when
		assertThrows(IllegalArgumentException.class, () -> authService.login(request, session));
	}
}