package dev.cass.knorda.global.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class MemberSessionArgumentResolverTest {

	@InjectMocks
	private MemberSessionArgumentResolver memberSessionArgumentResolver;

	@Mock
	private MethodParameter methodParameter;

	@Mock
	private NativeWebRequest webRequest;

	@Mock
	private HttpServletRequest httpServletRequest;

	@Mock
	private HttpSession httpSession;

	@DisplayName("supportsParameter 테스트")
	@Test
	void supportsParameterTest() {
		// given
		doReturn(mock(MemberSession.class)).when(methodParameter).getParameterAnnotation(MemberSession.class);
		doReturn(AuthDto.SessionDto.class).when(methodParameter).getParameterType();

		// when
		boolean result = memberSessionArgumentResolver.supportsParameter(methodParameter);

		// then
		assertTrue(result);
	}

	@DisplayName("supportsParameter 테스트 - 어노테이션이 다름")
	@Test
	void supportsParameterTestDifferentAnnotation() {
		// given
		doReturn(null).when(methodParameter).getParameterAnnotation(MemberSession.class);
		doReturn(AuthDto.SessionDto.class).when(methodParameter).getParameterType();

		// when
		boolean result = memberSessionArgumentResolver.supportsParameter(methodParameter);

		// then
		assertFalse(result);
	}

	@DisplayName("supportsParameter 테스트 - 타입이 다름")
	@Test
	void supportsParameterTestDifferentType() {
		// given
		doReturn(mock(MemberSession.class)).when(methodParameter).getParameterAnnotation(MemberSession.class);
		doReturn(String.class).when(methodParameter).getParameterType();

		// when
		boolean result = memberSessionArgumentResolver.supportsParameter(methodParameter);

		// then
		assertFalse(result);
	}

	@DisplayName("resolveArgument 테스트")
	@Test
	void resolveArgumentTest() {
		// given
		AuthDto.SessionDto sessionDto = new AuthDto.SessionDto("admin", 1);
		doReturn(sessionDto).when(httpSession).getAttribute(SessionManageUtils.SESSION_MEMBER);

		doReturn(httpServletRequest).when(webRequest).getNativeRequest();
		doReturn(httpSession).when(httpServletRequest).getSession();

		// when
		AuthDto.SessionDto result = (AuthDto.SessionDto)memberSessionArgumentResolver.resolveArgument(methodParameter,
			mock(ModelAndViewContainer.class), webRequest, mock(WebDataBinderFactory.class));

		// then
		assertNotNull(result);
		assertEquals(result.getMemberId(), sessionDto.getMemberId());
		assertEquals(result.getMemberName(), sessionDto.getMemberName());
	}
}