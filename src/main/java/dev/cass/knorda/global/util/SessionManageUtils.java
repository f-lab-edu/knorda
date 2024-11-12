package dev.cass.knorda.global.util;

import org.springframework.stereotype.Component;

import dev.cass.knorda.global.Exception.NullSessionExeption;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionManageUtils {

	private SessionManageUtils() {
	}

	public static void invalidate(HttpSession session) {
		session.invalidate();
	}

	public static void addSession(HttpSession session, String key, Object value) {
		session.setAttribute(key, value);
	}

	public static Object getSession(HttpSession session, String key) {
		if(session == null){
			throw new NullSessionExeption();
		}
		return session.getAttribute(key);
	}

	public static String getUsername(HttpSession session) {
		return (String) getSession(session, "username");
	}
}
