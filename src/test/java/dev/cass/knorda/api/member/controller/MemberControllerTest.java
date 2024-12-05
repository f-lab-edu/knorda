package dev.cass.knorda.api.member.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.api.member.dto.RegisterDto;
import dev.cass.knorda.api.member.service.MemberService;
import dev.cass.knorda.global.exception1.GlobalExceptionHandler;
import dev.cass.knorda.global.util.SessionManageUtils;

/**
 * ExtendWith - JUnit5에서 테스트 확장을 지정하는 어노테이션
 * MockitoExtension - Mockito를 사용하기 위한 확장 클래스
 * 즉, 해당 테스트 클래스에서 MockitoExtension을 사용하겠다는 의미
 */
@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

	ObjectMapper objectMapper = new ObjectMapper();
	/**
	 * InjectMocks - 테스트 대상이 되는 객체에 Mock 객체를 주입시키는 어노테이션
	 * MemberController 객체를 생성하고, Mock 객체로 정의된 MemberService 객체를 주입시킨다
	 */
	@InjectMocks
	private MemberController memberController;
	/**
	 * Mock - 해당 객체를 Mock 객체로 만들어주는 어노테이션
	 */
	@Mock
	private MemberService memberService;
	private MockMvc mockMvc;

	/**
	 * BeforeEach - JUnit5에서 각 테스트 메소드가 실행되기 전에 실행되는 메소드를 지정하는 어노테이션
	 */
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(memberController)
			.setControllerAdvice(GlobalExceptionHandler.class).build();
	}

	@DisplayName("회원가입")
	@Test
	void saveMember() throws Exception {
		// Given
		RegisterDto.RegisterRequest registerRequest = new RegisterDto.RegisterRequest("test", "test1234", "test");
		RegisterDto.RegisterResponse registerResponse = new RegisterDto.RegisterResponse(1, "test", "test");

		doReturn(registerResponse).when(memberService).saveMember(any(RegisterDto.RegisterRequest.class));

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/v1/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerRequest)));

		// Then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(registerResponse)));
	}

	@DisplayName("회원가입 validation 틀렸을때")
	@Test
	void saveMemberValidation() throws Exception {
		// Given
		RegisterDto.RegisterRequest registerRequest = new RegisterDto.RegisterRequest("", "test", "test");

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/v1/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(registerRequest)));

		// Then
		resultActions
			.andExpect(status().isBadRequest());
	}

	@DisplayName("이름 중복 체크")
	@Test
	void isExistMember() throws Exception {
		// Given
		String name = "admin";
		RegisterDto.IsExistResponse isExistResponse = new RegisterDto.IsExistResponse(true);

		doReturn(true).when(memberService).isExistMemberName(name);

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/members/duplicate-id/{memberName}", name));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(isExistResponse)));
	}

	@DisplayName("자신 조회")
	@Test
	void getMember() throws Exception {
		// Given
		String name = "admin";
		int id = 1;
		RegisterDto.GetMemberResponse registerResponse = new RegisterDto.GetMemberResponse(id, name, "admin", null,
			null,
			null);

		doReturn(registerResponse).when(memberService).findMemberResponseByMemberId(id);

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/members/me")
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(registerResponse)));
	}

	@DisplayName("사용자 목록 조회")
	@Test
	void getMembers() throws Exception {
		// Given
		String name = "admin";
		int id = 1;
		RegisterDto.GetMemberResponse registerResponse = new RegisterDto.GetMemberResponse(id, name, "admin", null,
			null,
			null);

		List<RegisterDto.GetMemberResponse> registerResponses = List.of(registerResponse);

		doReturn(registerResponses).when(memberService).findAll(any(Pageable.class));

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/members")
			.param("page", "0")
			.param("size", "10")
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(registerResponses)));
	}

	@DisplayName("사용자 조회")
	@Test
	void getMemberByMemberName() throws Exception {
		// Given
		String name = "admin";
		int id = 1;
		RegisterDto.GetMemberResponse registerResponse = new RegisterDto.GetMemberResponse(id, name, "admin", null,
			null,
			null);

		doReturn(registerResponse).when(memberService).findMemberResponseByMemberId(id);

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/members/{memberId}", id)
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(registerResponse)));
	}

	@DisplayName("자신 수정")
	@Test
	void updateMember() throws Exception {
		// Given
		String name = "admin";
		int id = 1;
		RegisterDto.UpdateMemberRequest updateMemberRequest = new RegisterDto.UpdateMemberRequest("description change");
		RegisterDto.UpdateMemberResponse updateMemberResponse = new RegisterDto.UpdateMemberResponse("admin",
			"description change");

		doReturn(updateMemberResponse).when(memberService)
			.updateMember(eq(id), any(RegisterDto.UpdateMemberRequest.class), eq(name));

		// When
		ResultActions resultActions = mockMvc.perform(put("/api/v1/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(updateMemberRequest))
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(updateMemberResponse)));
	}

	@DisplayName("사용자 수정")
	@Test
	void updateMemberByMemberName() throws Exception {
		// Given
		String name = "admin";
		int id = 1;
		RegisterDto.UpdateMemberRequest updateMemberRequest = new RegisterDto.UpdateMemberRequest("description change");
		RegisterDto.UpdateMemberResponse updateMemberResponse = new RegisterDto.UpdateMemberResponse("admin",
			"description change");

		doReturn(updateMemberResponse).when(memberService)
			.updateMember(eq(id), any(RegisterDto.UpdateMemberRequest.class), eq(name));

		// When
		ResultActions resultActions = mockMvc.perform(put("/api/v1/members/{memberId}", id)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(updateMemberRequest))
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(updateMemberResponse)));
	}

	@DisplayName("자신 삭제")
	@Test
	void deleteMember() throws Exception {
		// Given
		String name = "admin";
		int id = 1;

		// When
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/members")
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isNoContent());
	}

	@DisplayName("사용자 삭제")
	@Test
	void deleteMemberByMemberName() throws Exception {
		// Given
		String name = "admin";
		int id = 1;

		// When
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/members/{memberId}", id)
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isNoContent());
	}

	@DisplayName("세션 없을 때 세션 데이터 호출")
	@Test
	void getMemberWhenSessionNotExist() throws Exception {
		// When
		mockMvc.perform(get("/api/v1/members/me"))
			.andExpect(status().isUnauthorized());
	}

	@DisplayName("비밀번호 변경")
	@Test
	void changePassword() throws Exception {
		// Given
		String name = "admin";
		int id = 1;
		RegisterDto.PasswordChangeRequest changePasswordRequest = new RegisterDto.PasswordChangeRequest("test1234",
			"test12345");

		doReturn(true).when(memberService).changePassword(eq(id), any(RegisterDto.PasswordChangeRequest.class));

		// When
		ResultActions resultActions = mockMvc.perform(put("/api/v1/members/password")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(changePasswordRequest))
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isNoContent());
	}

	@DisplayName("비밀번호 변경 실패")
	@Test
	void changePasswordFail() throws Exception {
		// Given
		String name = "admin";
		int id = 1;
		RegisterDto.PasswordChangeRequest changePasswordRequest = new RegisterDto.PasswordChangeRequest("test1234",
			"test12345");

		doReturn(false).when(memberService).changePassword(eq(id), any(RegisterDto.PasswordChangeRequest.class));

		// When
		ResultActions resultActions = mockMvc.perform(put("/api/v1/members/password")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(changePasswordRequest))
			.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, id)));

		// Then
		resultActions
			.andExpect(status().isBadRequest());
	}

}