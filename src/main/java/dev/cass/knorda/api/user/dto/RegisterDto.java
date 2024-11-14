package dev.cass.knorda.api.user.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.cass.knorda.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterDto {

	@Getter
	@AllArgsConstructor
	public static class RegisterRequest {
		/**
		 * NotNull - Bean Validation의 어노테이션. 해당 변수가 null이 아님을 확인할 것을 의미
		 * Size - Bean Validation의 어노테이션. 해당 변수의 길이를 확인할 것을 의미
		 * Bean Validation - Java EE 6부터 추가된 기능. 객체의 유효성을 검사하는 기능을 제공
		 * Spring에서는 Controller에서 @Valid 어노테이션을 사용하여 Bean Validation을 사용할 수 있다
		 * 클라이언트에서 request를 보내면 dispatcher servlet이 해당 request를 받아서 controller에 전달하기 위해 탐색하게 되는데,
		 * 이 때, RequestResponseBodyMethodProcessor 클래스에서 resolverArgument 메소드를 호출하게 되고,
		 * 거기에서 Valid, Validated 어노테이션이 붙은 객체를 찾아서 검증하게 되고, Error가 있을 경우 MethodArgumentNotValidException을 발생시킨다
		 */
		@NotNull
		@Size(min = 4, max = 20)
		private String memberName;

		@NotNull
		@Size(min = 5)
		private String password;

		@NotNull
		private String description;

		public Member toEntity() {
			return Member.builder()
				.memberName(memberName)
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
		private String memberName;

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
		private String memberName;

		public static RegisterResponse of(Member member) {
			return new RegisterResponse(member.getMemberName());
		}
	}

	@Getter
	@AllArgsConstructor
	public static class GetMemberResponse {
		private String memberName;
		private String description;
		/**
		 * JsonFormat - Jackson 라이브러리의 어노테이션. JSON으로 변환될 때, 날짜 형식을 어떻게 직렬화할지 지정
		 * String 형식으로, pattern의 "yyyy-MM-dd'T'HH:mm:ss" 형식으로 직렬화하고, timezone을 "Asia/Seoul"로 설정
		 */
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime lastLoggedAt;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime createdAt;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime modifiedAt;

		public static GetMemberResponse of(Member member) {
			return new GetMemberResponse(member.getMemberName(), member.getDescription(), member.getLastLoggedAt(),
				member.getCreatedAt(), member.getModifiedAt());
		}
	}

	@Getter
	@AllArgsConstructor
	public static class UpdateMemberResponse {
		private String memberName;
		private String description;

		public static UpdateMemberResponse of(Member member) {
			return new UpdateMemberResponse(member.getMemberName(), member.getDescription());
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
