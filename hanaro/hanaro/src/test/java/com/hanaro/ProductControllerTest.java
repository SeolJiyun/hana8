package com.hanaro;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import com.hanaro.dto.member.LoginRequest;
import com.hanaro.entity.Member;
import com.hanaro.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	private String adminToken;

	@BeforeAll
	void setup() throws Exception {
		Member admin = Member.builder()
			.email("admin@test.com")
			.password(passwordEncoder.encode("12345678"))
			.nickname("관리자테스트")
			.role("ROLE_ADMIN")
			.build();
		memberRepository.save(admin);

		LoginRequest login = new LoginRequest("admin@test.com", "12345678");

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
		memberRepository.findByEmailAndDeletedFalse("admin@test.com")
			.ifPresent(memberRepository::delete);
	}

	@Test
	@Order(1)
	@DisplayName("상품 목록 조회 - 인증 없이 접근 가능")
	void getProductsPublic() throws Exception {
		mockMvc.perform(get("/api/public/products"))
			.andExpect(status().isOk());
	}

	@Test
	@Order(2)
	@DisplayName("존재하지 않는 상품 조회")
	void getProductNotFound() throws Exception {
		mockMvc.perform(get("/api/public/products/999"))
			.andExpect(status().isNotFound());
	}

	@Test
	@Order(3)
	@DisplayName("상품 등록 - ADMIN 권한")
	void createProduct() throws Exception {
		String body = """
            {
              "name": "테스트예금",
              "productType": "DEPOSIT",
              "depositAmount": 1000000,
              "subscriptionPeriod": 12,
              "maturityRate": 3.5,
              "earlyTerminationRate": 1.5
            }
            """;

		mockMvc.perform(post("/api/admin/products")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("테스트예금"));
	}

	@Test
	@Order(4)
	@DisplayName("상품 등록 - 인증 없으면 401")
	void createProductUnauthorized() throws Exception {
		String body = """
            {
              "name": "테스트예금",
              "productType": "DEPOSIT",
              "depositAmount": 1000000,
              "subscriptionPeriod": 12,
              "maturityRate": 3.5,
              "earlyTerminationRate": 1.5
            }
            """;

		mockMvc.perform(post("/api/admin/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isUnauthorized());
	}

	@Test
	@Order(5)
	@DisplayName("상품 수정 - ADMIN")
	void updateProduct() throws Exception {
		String createBody = """
        {
          "name": "수정전예금",
          "productType": "DEPOSIT",
          "depositAmount": 1000000,
          "subscriptionPeriod": 12,
          "maturityRate": 3.5,
          "earlyTerminationRate": 1.5
        }
        """;

		MvcResult result = mockMvc.perform(post("/api/admin/products")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createBody))
			.andExpect(status().isOk())
			.andReturn();

		JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
		long productId = node.get("id").asLong();

		String updateBody = """
        {
          "name": "수정후예금",
          "productType": "DEPOSIT",
          "depositAmount": 2000000,
          "subscriptionPeriod": 24,
          "maturityRate": 4.0,
          "earlyTerminationRate": 2.0
        }
        """;

		mockMvc.perform(put("/api/admin/products/" + productId)
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updateBody))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("수정후예금"));
	}

	@Test
	@Order(6)
	@DisplayName("상품 삭제 - ADMIN")
	void deleteProduct() throws Exception {
		String createBody = """
        {
          "name": "삭제예정예금",
          "productType": "DEPOSIT",
          "depositAmount": 1000000,
          "subscriptionPeriod": 12,
          "maturityRate": 3.5,
          "earlyTerminationRate": 1.5
        }
        """;

		MvcResult result = mockMvc.perform(post("/api/admin/products")
				.header("Authorization", "Bearer " + adminToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createBody))
			.andExpect(status().isOk())
			.andReturn();

		JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
		long productId = node.get("id").asLong();

		mockMvc.perform(delete("/api/admin/products/" + productId)
				.header("Authorization", "Bearer " + adminToken))
			.andExpect(status().isOk());
	}
}
