package dev.cass.knorda.global.util;

import org.springframework.stereotype.Component;

import dev.cass.knorda.global.exception.NullSessionException;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class SessionManageUtils {

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

	public static String getMemberName(HttpSession session) {
		return (String)getSession(session, "memberName");
	}

	public static int getMemberId(HttpSession session) {
		return (int)getSession(session, "memberId");
	}
}
