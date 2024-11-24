package dev.cass.knorda.api.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.api.member.exception.IdPasswordNotMatchException;
import dev.cass.knorda.api.member.exception.MemberNotFoundException;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.member.MemberRepository;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final MemberRepository memberRepository;

	@Transactional
	public AuthDto.LoginResponse login(AuthDto.LoginRequest request, HttpSession session) {
		Member member = memberRepository.findFirstByMemberName(request.getMemberName())
			.orElseThrow(MemberNotFoundException::new);
		boolean isPasswordMatch = member.isPasswordMatch(request.getPassword());
		if (!isPasswordMatch) {
			throw new IdPasswordNotMatchException();
		}

		member.login();

		memberRepository.save(member);

		SessionManageUtils.addSession(session, SessionManageUtils.SESSION_USER, AuthDto.SessionDto.of(member));

		return new AuthDto.LoginResponse(member.getMemberName());
	}

}
