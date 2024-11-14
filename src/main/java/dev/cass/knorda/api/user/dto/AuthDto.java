package dev.cass.knorda.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthDto {

	@Getter
	@AllArgsConstructor
	public static class LoginResponse {
		private String memberName;
	}

	@Getter
	@AllArgsConstructor
	public static class LoginRequest {
		@NotBlank
		private String memberName;
		@NotBlank
		private String password;
	}
}
