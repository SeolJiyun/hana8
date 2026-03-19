package com.hanaro;

import com.hanaro.dto.member.LoginRequest;
import com.hanaro.dto.member.MemberRequest;
import com.hanaro.entity.Member;
import com.hanaro.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.hanaro.security.JwtUtil;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class JwtRefreshControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired JwtUtil jwtUtil;

	private String accessToken;
	private String refreshToken;

	@BeforeAll
	void setup() throws Exception {
		memberRepository.findByEmail("refresh@test.com")
			.ifPresent(memberRepository::delete);

		Member user = Member.builder()
			.email("refresh@test.com")
			.password(passwordEncoder.encode("Test1234!"))
			.nickname("리프레시테스터")
			.role("ROLE_USER")
			.build();
		memberRepository.save(user);

		LoginRequest login = new LoginRequest("refresh@test.com", "Test1234!");

		MvcResult result = mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
			.andExpect(status().isOk())
			.andReturn();

		JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
		accessToken = node.get("accessToken").asText();
		refreshToken = node.get("refreshToken").asText();
	}

	@AfterAll
	void cleanup() {
		memberRepository.findByEmail("refresh@test.com")
			.ifPresent(memberRepository::delete);
	}

	@Test
	@Order(1)
	@DisplayName("유효한 토큰으로 refresh - 기존 토큰 반환")
	void refreshWithValidToken() throws Exception {
		mockMvc.perform(get("/api/auth/refresh")
				.header("Authorization", "Bearer " + accessToken)
				.param("refreshToken", refreshToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists())
			.andExpect(jsonPath("$.refreshToken").exists());
	}

	@Test
	@Order(2)
	@DisplayName("유효한 accessToken이면 refreshToken 무관하게 200 반환")
	void refreshWithValidAccessToken() throws Exception {
		mockMvc.perform(get("/api/auth/refresh")
				.header("Authorization", "Bearer " + accessToken)
				.param("refreshToken", "invalid_token"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists());
	}

	@Test
	@Order(3)
	@DisplayName("만료된 accessToken이면 refreshToken으로 갱신")

	void refreshWithExpiredAccessToken() throws Exception {
		// 만료된 토큰 직접 생성 (0분 = 즉시 만료)
		String expiredToken = jwtUtil.generateToken(
			java.util.Map.of("email", "refresh@test.com", "role", "ROLE_USER"), 0
		);

		mockMvc.perform(get("/api/auth/refresh")
				.header("Authorization", "Bearer " + expiredToken)
				.param("refreshToken", refreshToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists());
	}
}
