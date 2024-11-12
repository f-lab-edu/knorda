package dev.cass.knorda.api.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.cass.knorda.api.user.dto.RegisterDto;
import dev.cass.knorda.api.user.service.MemberService;
import dev.cass.knorda.global.controller.V1Controller;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * RestController - 해당 클래스가 RestController임을 명시하는 어노테이션
 * Controller와 ResponseBody를 합친 어노테이션이라 생각할 수 있음
 * Controller - 해당 클래스가 Controller임을 명시하는 어노테이션
 * Controller는 Spring MVC의 컨트롤러로, View를 반환하게 됨
 * 메소드 내에서 model에 전달할 attribute를 정리하고, 메소드의 return 값으로 view의 이름을 반환하여
 * DispatcherServlet이 해당 view를 찾아서 렌더링한 후 응답을 반환하게 된다
 * ResponseBody - 해당 메서드의 반환값을 HTTP Response Body에 직접 작성하겠다는 의미
 * 컨트롤러가 요청을 처리한 후에 응답 데이터로 보낼 객체를 반환하고, 이를 JSON (또는 XML 등등...) 형태로 직렬화한 뒤에 응답을 반환하게 된다
 * 즉 DispatcherServlet이 뷰를 찾아서 랜더링하는 과정이 생략된다
 * RequestMapping - 요청 URL을 어떤 메서드가 처리할지 매핑해주는 어노테이션
 * 클래스에 선언할 경우, 클래스 내의 모든 메서드에 prefix로 적용된다
 * - PostMapping - POST 요청을 처리하는 메서드로 매핑해주는 어노테이션
 * - GetMapping - GET 요청을 처리하는 메서드로 매핑해주는 어노테이션
 */
@RequiredArgsConstructor
@RestController
@V1Controller
public class MemberController {
	private final MemberService memberService;

	/**
	 * RequestBody - HTTP 요청의 body 내용을 자바 객체로 매핑하는 역할을 하는 어노테이션
	 */
	@PostMapping("/users")
	public ResponseEntity<RegisterDto.RegisterResponse> saveUser(
		@RequestBody @Valid RegisterDto.RegisterRequest request) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(memberService.saveUser(request));
	}

	@GetMapping("/users")
	public ResponseEntity<RegisterDto.GetMemberResponse> getLoggedInUser(HttpSession session) {
		String username = SessionManageUtils.getUsername(session);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.findMemberByUsername(username));
	}

	@GetMapping("/users/{username}")
	public ResponseEntity<RegisterDto.GetMemberResponse> getMember(@PathVariable String username) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.findMemberByUsername(username));
	}

	@PutMapping("/users")
	public ResponseEntity<RegisterDto.UpdateMemberResponse> updateMember(
		HttpSession session,
		@RequestBody @Valid RegisterDto.UpdateMemberRequest request) {
		String username = SessionManageUtils.getUsername(session);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.updateMember(username, request, username));
	}

	@PutMapping("/users/{username}")
	public ResponseEntity<RegisterDto.UpdateMemberResponse> updateMember(
		HttpSession session,
		@PathVariable String username,
		@RequestBody @Valid RegisterDto.UpdateMemberRequest request) {
		String loggedInUser = SessionManageUtils.getUsername(session);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.updateMember(username, request, loggedInUser));
	}

	@DeleteMapping("/users")
	public ResponseEntity<Void> deleteMember(HttpSession session) {
		String username = SessionManageUtils.getUsername(session);
		memberService.deleteMember(username, username);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@DeleteMapping("/users/{username}")
	public ResponseEntity<Void> deleteMember(
		HttpSession session,
		@PathVariable String username) {
		String loggedInUser = SessionManageUtils.getUsername(session);
		memberService.deleteMember(username, loggedInUser);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	/**
	 * PathVariable - URL 경로의 일부를 매개변수로 사용할 수 있게 해주는 어노테이션
	 */
	@GetMapping("/users/duplicate-id/{username}")
	public ResponseEntity<RegisterDto.IsExistResponse> isExistUser(@PathVariable String username) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(new RegisterDto.IsExistResponse(memberService.isExistUsername(username)));
	}

}
