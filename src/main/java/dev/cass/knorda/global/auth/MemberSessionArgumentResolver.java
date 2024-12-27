package dev.cass.knorda.global.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * MemberSession 어노테이션을 매개 변수에 붙이면, 해당 매개 변수에 세션에 저장된 회원 정보를 주입해 주도록 함
 */
@RequiredArgsConstructor
@Component
public class MemberSessionArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * 해당 매개 변수가 MemberSession 어노테이션을 가지고 있고, AuthDto.SessionDto 클래스인 경우에만 어노테이션을 적용시킴
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean isMemberSessionAnnotation = parameter.getParameterAnnotation(MemberSession.class) != null;
		boolean isDtoClass = AuthDto.SessionDto.class.equals(parameter.getParameterType());

		return isMemberSessionAnnotation && isDtoClass;
	}

	/**
	 * supportsParameter가 true일 시 실행되는 resolveArgument로,
	 * 세션에 저장된 회원 정보를 반환함
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = request.getSession();

		return SessionManageUtils.getSessionMember(session);
	}

}
