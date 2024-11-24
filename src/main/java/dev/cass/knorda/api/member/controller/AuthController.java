package dev.cass.knorda.api.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.api.member.service.AuthService;
import dev.cass.knorda.global.controller.V1Controller;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@V1Controller
public class AuthController {

	private final AuthService authService;

	/**
	 * Valid - 해당 객체를 Dispatcher Servlet을 탐색하는 과정에서 검증하겠다는 의미
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthDto.LoginResponse> login(HttpSession session,
		@RequestBody @Valid AuthDto.LoginRequest request) {
		AuthDto.LoginResponse response = authService.login(request, session);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		SessionManageUtils.invalidate(session);
		return ResponseEntity.ok().build();
	}
}
