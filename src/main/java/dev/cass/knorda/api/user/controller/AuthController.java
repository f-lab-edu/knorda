package dev.cass.knorda.api.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.cass.knorda.api.user.dto.AuthDto;
import dev.cass.knorda.api.user.service.MemberService;
import dev.cass.knorda.global.controller.V1Controller;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@V1Controller
public class AuthController {

	private final MemberService memberService;

	@PostMapping("/login")
	public ResponseEntity<AuthDto.LoginResponse> login(HttpSession session,
		@RequestBody @Valid AuthDto.LoginRequest request) {
		AuthDto.LoginResponse response = memberService.login(request, session);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		SessionManageUtils.invalidate(session);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login-test")
	public ResponseEntity<String> loginTest(HttpSession session) {
		String logged_ln = (String)SessionManageUtils.getSession(session, "username");
		return ResponseEntity.ok(logged_ln);
	}
}
