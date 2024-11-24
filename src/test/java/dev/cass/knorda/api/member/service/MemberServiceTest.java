package dev.cass.knorda.api.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.member.dto.RegisterDto;
import dev.cass.knorda.api.member.exception.MemberNotFoundException;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.member.MemberRepository;
import dev.cass.knorda.global.util.EncryptUtils;

/**
 * TestInstance - JUnit5에서 테스트 인스턴스를 생성하는 방식을 지정하는 어노테이션
 * - PER_CLASS : 테스트 클래스당 인스턴스를 하나씩 생성한다.
 * - PER_METHOD : 테스트 메소드마다 인스턴스를 생성한다.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@BeforeEach
	void setUp() {
		// 간헐적으로 setSalt 메소드에서 NPE 발생해서, 우선 수동으로 BeforeEach에서 salt 설정
		EncryptUtils encryptUtils = new EncryptUtils();
		encryptUtils.setSalt("123");
	}

	@DisplayName("회원 저장")
	@Test
	void saveMember() {
		// given
		RegisterDto.RegisterRequest request = new RegisterDto.RegisterRequest("test", "test", "test");
		Member member = Member.builder()
			.memberId(1)
			.memberName(request.getMemberName())
			.password(request.getPassword())
			.description(request.getDescription())
			.build();

		doReturn(member).when(memberRepository).save(any(Member.class));

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
		Optional<Member> member = Optional.ofNullable(Member.builder().memberName(memberName).build());
		doReturn(member).when(memberRepository).findFirstByMemberName(memberName);

		// when
		boolean result = memberService.isExistMemberName(memberName);

		// then
		assertTrue(result);
	}

	@DisplayName("모든 회원 조회")
	@Test
	void findAll() {
		// given
		Member member = Member.builder().memberId(1).memberName("admin").build();
		Member member2 = Member.builder().memberId(2).memberName("test").build();
		List<Member> members = List.of(member, member2);

		doReturn(members).when(memberRepository).findAllByIsDeletedFalse(any(Pageable.class));

		// when
		List<RegisterDto.GetMemberResponse> result = memberService.findAll(PageRequest.of(0, 10));

		// then
		assertEquals(1, result.getFirst().getMemberId());
	}

	@DisplayName("비밀번호 변경")
	@Test
	void changePassword() {
		// given
		int memberId = 1;
		String memberName = "admin";
		String password = "admin";
		String newPassword = "admin1";
		RegisterDto.PasswordChangeRequest request = new RegisterDto.PasswordChangeRequest(password, newPassword);

		doReturn(Optional.of(
			Member.builder().memberName(memberName).password(EncryptUtils.encryptSHA256(password)).build())).when(
				memberRepository)
			.findFirstByMemberId(memberId);
		doReturn(Member.builder().memberName(memberName).password(newPassword).build()).when(memberRepository)
			.save(any(Member.class));

		// when
		boolean result = memberService.changePassword(memberId, request);

		// then
		assertTrue(result);
	}

	@DisplayName("비밀번호 변경 사용자 없음")
	@Test
	void changePasswordNoUser() {
		// given
		int memberId = 2;
		RegisterDto.PasswordChangeRequest request = new RegisterDto.PasswordChangeRequest("test", "test");

		doReturn(Optional.empty()).when(memberRepository).findFirstByMemberId(memberId);

		// when
		boolean result = memberService.changePassword(memberId, request);

		// then
		assertFalse(result);
	}

	@DisplayName("비밀번호 변경 비밀번호 불일치")
	@Test
	void changePasswordNotMatch() {
		// given
		int memberId = 1;
		String memberName = "admin";
		String password = "admin";
		String wrongPassword = "admin123";
		String newPassword = "admin1";
		RegisterDto.PasswordChangeRequest request = new RegisterDto.PasswordChangeRequest(wrongPassword, newPassword);

		doReturn(
			Optional.of(Member.builder().memberName(memberName).password(EncryptUtils.encryptSHA256(password)).build()))
			.when(memberRepository)
			.findFirstByMemberId(memberId);

		// when
		boolean result = memberService.changePassword(memberId, request);

		// then
		assertFalse(result);
	}

	@DisplayName("사용자 갱신")
	@Test
	void updateMember() {
		// given
		RegisterDto.UpdateMemberRequest request = new RegisterDto.UpdateMemberRequest("description test");

		doReturn(Optional.of(Member.builder().memberId(1).memberName("admin").description("test").build())).when(
				memberRepository)
			.findFirstByMemberId(1);
		doReturn(Member.builder().memberId(1).memberName("admin").description("description test").build()).when(
			memberRepository).save(any(Member.class));

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
		assertThrows(MemberNotFoundException.class, () -> memberService.updateMember(2, request, "admin"));
	}

	@DisplayName("사용자 삭제")
	@Test
	void deleteMember() {
		doReturn(Optional.of(Member.builder().memberName("admin").build())).when(memberRepository)
			.findFirstByMemberId(1);
		doReturn(Member.builder().memberName("admin").isDeleted(true).build()).when(memberRepository)
			.save(any(Member.class));

		// when
		memberService.deleteMember(1, "admin");

		// then
		doReturn(Optional.empty()).when(memberRepository).findFirstByMemberNameAndIsDeletedFalse("admin");
		assertFalse(memberService.isExistMember("admin"));
	}

	@DisplayName("사용자 삭제 사용자 없음")
	@Test
	void deleteMemberNoUser() {
		// given
		doReturn(Optional.empty()).when(memberRepository).findFirstByMemberId(2);

		// when
		assertThrows(MemberNotFoundException.class, () -> memberService.deleteMember(2, "admin"));
	}

	@DisplayName("사용자 id로 사용자 조회")
	@Test
	void findMemberByMemberName() {
		// given
		Member member = Member.builder().memberId(1).memberName("admin").build();
		doReturn(Optional.of(member)).when(memberRepository).findFirstByMemberId(1);

		// when
		RegisterDto.GetMemberResponse result = memberService.findMemberByMemberId(1);

		// then
		assertEquals("admin", result.getMemberName());
	}

	@DisplayName("사용자명으로 사용자 조회 사용자 없음")
	@Test
	void findMemberByMemberNameNoUser() {
		// given
		doReturn(Optional.empty()).when(memberRepository).findFirstByMemberId(10);

		// when
		assertThrows(MemberNotFoundException.class, () -> memberService.findMemberByMemberId(10));
	}
}