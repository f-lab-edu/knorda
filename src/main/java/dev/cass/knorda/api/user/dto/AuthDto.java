package dev.cass.knorda.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthDto {

	@Getter
	@AllArgsConstructor
	public static class LoginResponse {
		private String username;
	}

	@Getter
	@AllArgsConstructor
	public static class LoginRequest {
		@NotBlank
		private String username;
		@NotBlank
		private String password;
	}
}
