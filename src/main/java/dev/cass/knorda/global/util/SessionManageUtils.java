package dev.cass.knorda.global.util;

import org.springframework.stereotype.Component;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.global.exception1.NullSessionException;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class SessionManageUtils {

	public static final String SESSION_MEMBER = "session_member";

	public static void invalidate(HttpSession session) {
		session.invalidate();
	}

	public static void addSession(HttpSession session, String key, Object value) {
		session.setAttribute(key, value);
	}

	public static Object getSession(HttpSession session, String key) {
		if (session == null) {
			throw new NullSessionException();
		}
		return session.getAttribute(key);
	}

	public static AuthDto.SessionDto getSessionMember(HttpSession session) {
		Object sessionObj = getSession(session, SESSION_MEMBER);
		if (!(sessionObj instanceof AuthDto.SessionDto)) {
			throw new NullSessionException();
		}
		return (AuthDto.SessionDto)sessionObj;
	}

	public static String getMemberName(HttpSession session) {
		return getSessionMember(session).getMemberName();
	}

	public static int getMemberId(HttpSession session) {
		return getSessionMember(session).getMemberId();
	}
}
