package dev.cass.knorda.api.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.api.member.exception.IdPasswordNotMatchException;
import dev.cass.knorda.api.member.exception.MemberNotFoundException;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.member.MemberRepository;
import dev.cass.knorda.global.util.EncryptUtils;
import dev.cass.knorda.global.util.SessionManageUtils;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	@InjectMocks
	private AuthService authService;

	@Mock
	private MemberRepository memberRepository;

	private MockHttpSession session = new MockHttpSession();

	@BeforeEach
	void setUp() {
		// 간헐적으로 setSalt 메소드에서 NPE 발생해서, 우선 수동으로 BeforeEach에서 salt 설정
		EncryptUtils encryptUtils = new EncryptUtils();
		encryptUtils.setSalt("123");
	}

	@DisplayName("로그인")
	@Test
	void login() {
		// given
		AuthDto.LoginRequest request = new AuthDto.LoginRequest("admin", "admin");
		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.password(EncryptUtils.encryptSHA256("admin"))
			.build();

		doReturn(Optional.of(member)).when(memberRepository).findFirstByMemberName(request.getMemberName());
		doReturn(member).when(memberRepository).save(any(Member.class));

		// when
		AuthDto.LoginResponse result = authService.login(request, session);

		// then
		assertEquals(request.getMemberName(), result.getMemberName());
		assertEquals(SessionManageUtils.getMemberName(session), request.getMemberName());
	}

	@DisplayName("로그인 사용자 없음")
	@Test
	void loginNoUser() {
		// given
		AuthDto.LoginRequest request = new AuthDto.LoginRequest("test", "test");

		doReturn(Optional.empty()).when(memberRepository).findFirstByMemberName(request.getMemberName());

		// when
		assertThrows(MemberNotFoundException.class, () -> authService.login(request, session));
	}

	@DisplayName("로그인 비밀번호 불일치")
	@Test
	void loginPasswordNotMatch() {
		// given
		AuthDto.LoginRequest request = new AuthDto.LoginRequest("admin", "admin1");

		doReturn(Optional.of(Member.builder().password(EncryptUtils.encryptSHA256("admin")).build()))
			.when(memberRepository).findFirstByMemberName(request.getMemberName());

		// when
		assertThrows(IdPasswordNotMatchException.class, () -> authService.login(request, session));
	}
}