package com.hanaro;

import com.hanaro.dto.member.LoginRequest;
import com.hanaro.dto.member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

	@Autowired
	WebApplicationContext context;

	@Autowired
	ObjectMapper objectMapper;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders
			.webAppContextSetup(context)
			.apply(springSecurity())
			.build();
	}

	@Test
	@DisplayName("회원가입 성공")
	void registerSuccess() throws Exception {

		MemberRequest request =
			new MemberRequest("newuser@hanaro.com", "Password1!", "신규유저");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다"));
	}

	@Test
	@DisplayName("이메일 중복 회원가입 실패")
	void registerDuplicateEmail() throws Exception {

		MemberRequest first =
			new MemberRequest("dup@hanaro.com", "Password1!", "첫번째");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(first)))
			.andExpect(status().isOk());


		MemberRequest second =
			new MemberRequest("dup@hanaro.com", "Password1!", "두번째");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(second)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("로그인 성공 - accessToken 반환")
	void loginSuccess() throws Exception {

		MemberRequest request =
			new MemberRequest("login@hanaro.com", "Password1!", "로그인테스터");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());


		LoginRequest login =
			new LoginRequest("login@hanaro.com", "Password1!");

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists());
	}

	@Test
	@DisplayName("Validation 실패 - 400")
	void validationFail() throws Exception {
		MemberRequest request = new MemberRequest("잘못된이메일", "123", "a");

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors").exists());
	}
}
