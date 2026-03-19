package com.hanaro.controller;

import com.hanaro.dto.account.AccountRequest;
import com.hanaro.dto.account.AccountResponse;
import com.hanaro.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "계좌", description = "계좌/상품 가입 API")
public class AccountController {

	private final AccountService accountService;

	// 내 계좌 전체 조회
	@GetMapping("/accounts")
	@Operation(summary = "내 계좌 목록 조회")
	public ResponseEntity<List<AccountResponse>> getMyAccounts(
		@AuthenticationPrincipal String email) {  // JWT에서 email 꺼냄
		return ResponseEntity.ok(accountService.getMyAccounts(email));
	}

	// 상품 가입
	@PostMapping("/accounts")
	@Operation(summary = "상품 가입")
	public ResponseEntity<AccountResponse> subscribe(
		@AuthenticationPrincipal String email,
		@Valid @RequestBody AccountRequest request) {
		return ResponseEntity.ok(accountService.subscribe(email, request));
	}

	// 중도 해지
	@DeleteMapping("/accounts/{id}")
	@Operation(summary = "중도 해지")
	public ResponseEntity<Map<String, String>> terminate(
		@AuthenticationPrincipal String email,
		@PathVariable Long id) {
		accountService.terminate(email, id);
		return ResponseEntity.ok(Map.of("message", "해지가 완료되었습니다"));
	}
}
