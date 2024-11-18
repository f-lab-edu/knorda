package dev.cass.knorda.api.member.dto;

import dev.cass.knorda.domain.member.Member;
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

	@Getter
	@AllArgsConstructor
	public static class SessionDto {
		private String memberName;
		private int memberId;

		public static SessionDto of(Member member) {
			return new SessionDto(member.getMemberName(), member.getMemberId());
		}
	}
}
