package dev.cass.knorda.api.member.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.api.member.service.AuthService;
import dev.cass.knorda.global.util.SessionManageUtils;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
	ObjectMapper objectMapper = new ObjectMapper();
	@InjectMocks
	private AuthController authController;
	@Mock
	private AuthService authService;
	private MockMvc mockMvc;
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

		doReturn(new AuthDto.LoginResponse(loginRequest.getMemberName())).when(authService).login(any(), any());

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(loginRequest)));

		// Then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.memberName").value(loginRequest.getMemberName()));
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
		SessionManageUtils.addSession(session, SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto("admin", 1));

		// When
		ResultActions resultActions = mockMvc.perform(post("/api/v1/logout")
			.session(session));

		// Then
		resultActions
			.andExpect(status().isOk());
		assertTrue(session.isInvalid());
	}
}