package dev.cass.knorda.api.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.user.dto.AuthDto;
import dev.cass.knorda.api.user.exception.IdPasswordNotMatchException;
import dev.cass.knorda.api.user.exception.MemberNotFoundException;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.member.MemberRepository;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public AuthDto.LoginResponse login(AuthDto.LoginRequest request, HttpSession session) {
		Member member = memberRepository.findFirstByMemberName(request.getMemberName())
			.orElseThrow(MemberNotFoundException::new);
		boolean isPasswordMatch = member.isPasswordMatch(request.getPassword());
		if (!isPasswordMatch) {
			throw new IdPasswordNotMatchException();
		}

		SessionManageUtils.addSession(session, "memberName", member.getMemberName());
		SessionManageUtils.addSession(session, "memberId", member.getMemberId());

		return new AuthDto.LoginResponse(member.getMemberName());
	}

}
