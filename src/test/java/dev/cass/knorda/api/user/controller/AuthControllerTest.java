package dev.cass.knorda.api.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.cass.knorda.api.user.dto.AuthDto;
import dev.cass.knorda.api.user.service.MemberService;
import dev.cass.knorda.global.util.SessionManageUtils;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
	@InjectMocks
	private AuthController authController;

	@Mock
	private MemberService memberService;

	private MockMvc mockMvc;
	ObjectMapper objectMapper = new ObjectMapper();
	private MockHttpSession session = new MockHttpSession();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@DisplayName("로그인")
	@Test
	void login() throws Exception {
		// Given
		AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("admin", "admin");

		doReturn(new AuthDto.LoginResponse(loginRequest.getUsername())).when(memberService).login(any(), any());

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(loginRequest)));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.username").value(loginRequest.getUsername()));
	}

	@DisplayName("로그인 비밀번호 입력안함")
	@Test
	void loginWithoutPassword() throws Exception {
		// Given
		AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("admin", "");

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(loginRequest)));

		// Then
		resultActions
			.andExpect(status().isBadRequest());
	}

	@DisplayName("로그아웃")
	@Test
	void logout() throws Exception {
		// Given
		SessionManageUtils.addSession(session, "username", "admin");

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/v1/logout")
			.session(session));

		// Then
		resultActions
			.andExpect(status().isOk());
		assertTrue(session.isInvalid());
	}

	@DisplayName("로그인 테스트")
	@Test
	void loginTest() throws Exception {
		// Given
		SessionManageUtils.addSession(session, "username", "admin");

		// When
		ResultActions resultActions = mockMvc.perform(get("/api/v1/login-test")
			.session(session));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().string("admin"));
	}

}