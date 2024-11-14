package dev.cass.knorda.api.user.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.user.dto.RegisterDto;
import dev.cass.knorda.domain.member.Member;

/**
 * TestInstance - JUnit5에서 테스트 인스턴스를 생성하는 방식을 지정하는 어노테이션
 * - PER_CLASS : 테스트 클래스당 인스턴스를 하나씩 생성한다.
 * - PER_METHOD : 테스트 메소드마다 인스턴스를 생성한다.
 */
@Transactional
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberServiceTest {
	@Autowired
	private MemberService memberService;

	@DisplayName("회원 저장")
	@Test
	void saveMember() {
		// given
		RegisterDto.RegisterRequest request = new RegisterDto.RegisterRequest("test", "test", "test");

		// when
		RegisterDto.RegisterResponse result = memberService.saveMember(request);

		// then
		assertEquals(request.getMemberName(), result.getMemberName());
	}

	@DisplayName("회원 존재 여부")
	@Test
	void isExistMemberName() {
		// given
		String memberName = "admin";

		// when
		boolean result = memberService.isExistMemberName(memberName);

		// then
		assertTrue(result);
	}

	@DisplayName("모든 회원 조회")
	@Test
	void findAll() {
		// when
		List<Member> result = memberService.findAll();

		System.out.println(result.getFirst().getMemberId());

		// then
		assertEquals(1, result.getFirst().getMemberId());
	}

	@DisplayName("비밀번호 변경")
	@Test
	void changePassword() {
		// given
		RegisterDto.PasswordChangeRequest request = new RegisterDto.PasswordChangeRequest("admin", "admin", "admin");

		// when
		boolean result = memberService.changePassword(request);

		// then
		assertTrue(result);
	}

	@DisplayName("비밀번호 변경 사용자 없음")
	@Test
	void changePasswordNoUser() {
		// given
		RegisterDto.PasswordChangeRequest request = new RegisterDto.PasswordChangeRequest("test", "test", "test");

		// when
		boolean result = memberService.changePassword(request);

		// then
		assertFalse(result);
	}

	@DisplayName("비밀번호 변경 비밀번호 불일치")
	@Test
	void changePasswordNotMatch() {
		// given
		RegisterDto.PasswordChangeRequest request = new RegisterDto.PasswordChangeRequest("admin", "admin1", "test");

		// when
		boolean result = memberService.changePassword(request);

		// then
		assertFalse(result);
	}

	@DisplayName("사용자 갱신")
	@Test
	void updateMember() {
		// given
		RegisterDto.UpdateMemberRequest request = new RegisterDto.UpdateMemberRequest("description test");

		// when
		RegisterDto.UpdateMemberResponse result = memberService.updateMember(1, request, "admin");

		// then
		assertEquals(request.getDescription(), result.getDescription());
	}

	@DisplayName("사용자 갱신 사용자 없음")
	@Test
	void updateMemberNoUser() {
		// given
		RegisterDto.UpdateMemberRequest request = new RegisterDto.UpdateMemberRequest("description test");

		// when
		assertThrows(IllegalArgumentException.class, () -> memberService.updateMember(2, request, "admin"));
	}

	@DisplayName("사용자 삭제")
	@Test
	void deleteMember() {
		// when
		memberService.deleteMember("admin", "admin");

		// then
		assertFalse(memberService.isExistMember("admin"));
	}

	@DisplayName("사용자 삭제 사용자 없음")
	@Test
	void deleteMemberNoUser() {
		// when
		assertThrows(IllegalArgumentException.class, () -> memberService.deleteMember("test", "admin"));
	}

	@DisplayName("사용자 id로 사용자 조회")
	@Test
	void findMemberByMemberName() {
		// when
		RegisterDto.GetMemberResponse result = memberService.findMemberByMemberId(1);

		// then
		assertEquals("admin", result.getMemberName());
	}

	@DisplayName("사용자명으로 사용자 조회 사용자 없음")
	@Test
	void findMemberByMemberNameNoUser() {
		// when
		assertThrows(IllegalArgumentException.class, () -> memberService.findMemberByMemberId(10));
	}
}