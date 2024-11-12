package dev.cass.knorda.api.user.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.cass.knorda.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RegisterDto {

	private RegisterDto() {
	}

	@Getter
	@AllArgsConstructor
	public static class RegisterRequest {
		@NotNull
		@Size(min = 4, max = 20)
		private String username;

		@NotNull
		@Size(min = 5)
		private String password;

		@NotNull
		private String description;

		public Member toEntity() {
			return Member.builder()
				.username(username)
				.password(password)
				.description(description)
				.build();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class PasswordChangeRequest {
		@NotNull
		@Size(min = 4, max = 20)
		private String username;

		@NotNull
		@Size(min = 5)
		private String password;

		@NotNull
		@Size(min = 5)
		private String newPassword;
	}

	@Getter
	@AllArgsConstructor
	public static class IsExistResponse {
		private boolean isExist;
	}

	@Getter
	@AllArgsConstructor
	public static class RegisterResponse {
		private String username;

		public static RegisterResponse of(Member member) {
			return new RegisterResponse(member.getUsername());
		}
	}

	@Getter
	@AllArgsConstructor
	public static class GetMemberResponse {
		private String username;
		private String description;
		/**
		 * JsonFormat - Jackson 라이브러리의 어노테이션. JSON으로 변환될 때, 날짜 형식을 어떻게 직렬화할지 지정
		 * String 형식으로, pattern의 "yyyy-MM-dd'T'HH:mm:ss" 형식으로 직렬화하고, timezone을 "Asia/Seoul"로 설정
		 */
		@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime lastLoggedAt;
		@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime createdAt;
		@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime modifiedAt;

		public static GetMemberResponse of(Member member) {
			return new GetMemberResponse(member.getUsername(), member.getDescription(), member.getLastLoggedAt(), member.getCreatedAt(), member.getModifiedAt());
		}
	}

	@Getter
	@AllArgsConstructor
	public static class UpdateMemberResponse {
		private String username;
		private String description;

		public static UpdateMemberResponse of(Member member) {
			return new UpdateMemberResponse(member.getUsername(), member.getDescription());
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UpdateMemberRequest {
		@NotNull
		private String description;
	}
}
