package dev.cass.knorda.api.member.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.member.exception.MemberNotFoundException;
import dev.cass.knorda.api.member.dto.RegisterDto;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.member.MemberRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service - 해당 클래스가 Service임을 명시하는 마킹 어노테이션
 * RequiredArgsConstructor - Lombok의 어노테이션. final이나 @NonNull인 필드 값만 매개변수로 받는 생성자 자동 생성
 */
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	/**
	 * Transactional - 해당 메서드를 트랜잭션 처리하겠음을 선언하는 어노테이션
	 * JPA의 모든 메소드는 이미 Transational이 선언되어 있기 때문에, 하나의 JPA 메소드를 호출하여 사용하는 메소드에는 따로 선언하지 않아도 되고, (다른 속성을 정의하는 것이 아니라면)
	 * 두 개 이상의 작업을 하나의 단위로 묶는 경우에만 따로 Transactional을 선언해주면 된다.
	 * - readOnly : 읽기 전용으로 설정하면, 해당 트랜잭션 내에서 데이터 변경 작업을 할 수 없다.
	 */
	public RegisterDto.RegisterResponse saveMember(RegisterDto.RegisterRequest request) {
		return RegisterDto.RegisterResponse.of(memberRepository.save(request.toEntity()));
	}

	@Transactional(readOnly = true)
	public boolean isExistMemberName(String memberName) {
		return memberRepository.findFirstByMemberName(memberName).isPresent();
	}

	@Transactional(readOnly = true)
	public boolean isExistMember(String memberName) {
		return memberRepository.findFirstByMemberNameAndIsDeletedFalse(memberName).isPresent();
	}

	@Transactional(readOnly = true)
	public List<RegisterDto.GetMemberResponse> findAll(Pageable pageable) {
		return memberRepository.findAllByIsDeletedFalse(pageable)
			.stream()
			.map(RegisterDto.GetMemberResponse::of)
			.toList();
	}

	@Transactional(readOnly = true)
	public RegisterDto.GetMemberResponse findMemberByMemberId(int memberId) {
		Member member = memberRepository.findFirstByMemberId(memberId)
			.orElseThrow(MemberNotFoundException::new);
		return RegisterDto.GetMemberResponse.of(member);
	}

	@Transactional
	public boolean changePassword(int memberId, RegisterDto.PasswordChangeRequest request) {
		return memberRepository.findFirstByMemberId(memberId)
			.map(member -> {
				if (member.isPasswordMatch(request.getPassword())) {
					member.setPassword(request.getNewPassword());
					memberRepository.save(member);
					return true;
				}
				return false;
			})
			.orElse(false);
	}

	@Transactional
	public RegisterDto.UpdateMemberResponse updateMember(int memberId, RegisterDto.UpdateMemberRequest request,
		String modifier) {
		Member member = memberRepository.findFirstByMemberId(memberId)
			.orElseThrow(MemberNotFoundException::new);
		member.update(request.getDescription(), modifier);
		Member updatedMember = memberRepository.save(member);
		return RegisterDto.UpdateMemberResponse.of(updatedMember);
	}

	@Transactional
	public void deleteMember(int memberId, String modifier) {
		Member member = memberRepository.findFirstByMemberId(memberId)
			.orElseThrow(MemberNotFoundException::new);
		member.delete(modifier);
		memberRepository.save(member);
	}
}
