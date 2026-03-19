package com.hanaro;

import com.hanaro.dto.member.LoginRequest;
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
import com.hanaro.repository.AccountRepository;
import com.hanaro.repository.TransactionLogRepository;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	private String userToken;
	private long productId;

	@Autowired
	AccountRepository accountRepository;

	@BeforeAll
	void setup() throws Exception {
		// 기존 계정 삭제
		memberRepository.findByEmail("accounttest@test.com")
			.ifPresent(memberRepository::delete);

		// ADMIN 계정 생성 (상품 등록용)
		memberRepository.findByEmail("accountsetup@test.com")
			.ifPresent(memberRepository::delete);
		Member admin = Member.builder()
			.email("accountsetup@test.com")
			.password(passwordEncoder.encode("Admin1234!"))
			.nickname("계좌셋업관리자")
			.role("ROLE_ADMIN")
			.build();
		memberRepository.save(admin);

		// USER 계정 생성
		Member user = Member.builder()
			.email("accounttest@test.com")
			.password(passwordEncoder.encode("Test1234!"))
			.nickname("계좌테스터_" + System.currentTimeMillis())
			.role("ROLE_USER")
			.build();
		memberRepository.save(user);

		// ADMIN 로그인
		LoginRequest adminLogin = new LoginRequest("accountsetup@test.com", "Admin1234!");
		MvcResult adminResult = mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(adminLogin)))
			.andReturn();
		JsonNode adminNode = objectMapper.readTree(adminResult.getResponse().getContentAsString());
		String adminToken = adminNode.get("accessToken").asText();

		// 상품 등록
		String productBody = """
        {
          "name": "테스트예금",
          "productType": "DEPOSIT",
          "depositAmount": 1000000,
          "subscriptionPeriod": 12,
          "maturityRate": 3.5,
          "earlyTerminationRate": 1.5
        }
        """;
		MvcResult productResult = mockMvc.perform(post("/api/admin/products")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(productBody))
			.andReturn();
		JsonNode productNode = objectMapper.readTree(productResult.getResponse().getContentAsString());
		productId = productNode.get("id").asLong();

		// USER 로그인
		LoginRequest login = new LoginRequest("accounttest@test.com", "Test1234!");
		MvcResult result = mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
			.andReturn();
		JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
		userToken = node.get("accessToken").asText();
	}

	@Autowired
	TransactionLogRepository transactionLogRepository;
	@AfterAll
	void cleanup() {
		memberRepository.findByEmail("accounttest@test.com").ifPresent(member -> {
			// transaction_log → account → member 순서로 삭제
			accountRepository.findByMember(member).forEach(account -> {
				transactionLogRepository.deleteAll(
					transactionLogRepository.findByAccountOrderByCreatedAtDesc(account)
				);
				accountRepository.delete(account);
			});
			memberRepository.delete(member);
		});
	}

	@Test
	@Order(1)
	@DisplayName("내 계좌 목록 조회")
	void getMyAccounts() throws Exception {
		mockMvc.perform(get("/api/user/accounts")
				.header("Authorization", "Bearer " + userToken))
			.andExpect(status().isOk());
	}

	@Test
	@Order(2)
	@DisplayName("상품 가입 - 상품 없으면 404")
	void subscribe() throws Exception {
		String body = """
        {
          "productId": 999,
          "desiredAccountNumber": "111-1111-1111"
        }
        """;

		mockMvc.perform(post("/api/user/accounts")
				.header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isNotFound());
	}

	@Test
	@Order(3)
	@DisplayName("인증 없이 계좌 조회 - 401")
	void getAccountsUnauthorized() throws Exception {
		mockMvc.perform(get("/api/user/accounts"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	@Order(4)
	@DisplayName("중도 해지")
	void terminate() throws Exception {
		// 먼저 상품 가입
		String body = """
    {
      "productId": %d,
      "desiredAccountNumber": "222-2222-2222"
    }
    """.formatted(productId);

		MvcResult result = mockMvc.perform(post("/api/user/accounts")
				.header("Authorization", "Bearer " + userToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isOk())
			.andReturn();

		JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
		long accountId = node.get("id").asLong();

		mockMvc.perform(delete("/api/user/accounts/" + accountId)
				.header("Authorization", "Bearer " + userToken))
			.andExpect(status().isOk());
	}
}
