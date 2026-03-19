package com.hanaro;

import com.hanaro.dto.member.LoginRequest;
import com.hanaro.entity.*;
import com.hanaro.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminControllerTest {

	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	@Autowired MemberRepository memberRepository;
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired ProductRepository productRepository;
	@Autowired AccountRepository accountRepository;
	@Autowired TransactionLogRepository transactionLogRepository;

	private String adminToken;
	private Long accountId;

	@BeforeAll
	void setup() throws Exception {
		memberRepository.findByEmail("admintest2@test.com")
			.ifPresent(memberRepository::delete);

		Member admin = Member.builder()
			.email("admintest2@test.com")
			.password(passwordEncoder.encode("12345678"))
			.nickname("관리자테스트2")
			.role("ROLE_ADMIN")
			.build();
		memberRepository.save(admin);

		// 자유입출금 통장 생성
		Account freeAccount = Account.builder()
			.member(admin)
			.accountNumber("999-9999-9999")
			.accountType(Account.AccountType.FREE)
			.status(Account.AccountStatus.ACTIVE)
			.balance(0L)
			.interestRate(0.0)
			.build();
		accountRepository.save(freeAccount);

		// 상품 생성
		Product product = Product.builder()
			.name("테스트예금")
			.productType(Product.ProductType.DEPOSIT)
			.depositAmount(1000000L)
			.subscriptionPeriod(12)
			.maturityRate(3.5)
			.earlyTerminationRate(1.5)
			.deleted(false)
			.build();
		productRepository.save(product);

		// 예금 계좌 생성
		Account account = Account.builder()
			.member(admin)
			.product(product)
			.accountNumber("888-8888-8888")
			.accountType(Account.AccountType.DEPOSIT)
			.status(Account.AccountStatus.ACTIVE)
			.balance(1000000L)
			.interestRate(3.5)
			.maturityDate(LocalDate.now().plusMonths(12))
			.build();
		accountRepository.save(account);
		accountId = account.getId();

		LoginRequest login = new LoginRequest("admintest2@test.com", "12345678");
		MvcResult result = mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(login)))
			.andExpect(status().isOk())
			.andReturn();

		JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
		adminToken = node.get("accessToken").asText();
	}

	@AfterAll
	void cleanup() {
		memberRepository.findByEmail("admintest2@test.com")
			.ifPresent(m -> {
				accountRepository.findByMember(m).forEach(account -> {
					transactionLogRepository.findByAccountOrderByCreatedAtDesc(account)
						.forEach(transactionLogRepository::delete);
					accountRepository.delete(account);
				});
				memberRepository.delete(m);
			});
		productRepository.findAll().stream()
			.filter(p -> p.getName().equals("테스트예금"))
			.forEach(productRepository::delete);
	}

	@Test
	@Order(1)
	@DisplayName("회원 목록 조회")
	void getMembers() throws Exception {
		mockMvc.perform(get("/api/admin/members")
				.header("Authorization", "Bearer " + adminToken))
			.andExpect(status().isOk());
	}

	@Test
	@Order(2)
	@DisplayName("회원 목록 닉네임 검색")
	void getMembersSearch() throws Exception {
		mockMvc.perform(get("/api/admin/members")
				.param("nickname", "관리자")
				.header("Authorization", "Bearer " + adminToken))
			.andExpect(status().isOk());
	}

	@Test
	@Order(3)
	@DisplayName("존재하지 않는 회원 가입 내역 조회 - 404")
	void getMemberAccounts() throws Exception {
		mockMvc.perform(get("/api/admin/members/999/accounts")
				.header("Authorization", "Bearer " + adminToken))
			.andExpect(status().isNotFound());
	}

	@Test
	@Order(4)
	@DisplayName("만기 처리")
	void mature() throws Exception {
		mockMvc.perform(patch("/api/admin/accounts/" + accountId + "/mature")
				.header("Authorization", "Bearer " + adminToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("만기 처리되었습니다"));
	}

	@Test
	@Order(5)
	@DisplayName("인증 없이 관리자 API - 401")
	void adminUnauthorized() throws Exception {
		mockMvc.perform(get("/api/admin/members"))
			.andExpect(status().isUnauthorized());
	}
}
