package com.hanaro.controller;

import com.hanaro.dto.member.MemberResponse;
import com.hanaro.dto.account.AccountResponse;
import com.hanaro.entity.Member;
import com.hanaro.exception.BusinessException;
import com.hanaro.repository.AccountRepository;
import com.hanaro.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.hanaro.service.AccountService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "관리자", description = "관리자 전용 API")
public class AdminController {

	private final MemberRepository memberRepository;
	private final AccountRepository accountRepository;
	private final AccountService accountService;

	@GetMapping("/members")
	@Operation(summary = "회원 목록 조회 (닉네임 검색 가능)")
	public ResponseEntity<List<MemberResponse>> getMembers(
		@RequestParam(required = false) String nickname) {
		if (nickname != null && !nickname.isBlank()) {
			return ResponseEntity.ok(
				memberRepository.findByNicknameContaining(nickname)
					.stream()
					.map(MemberResponse::new)
					.toList()
			);
		}
		return ResponseEntity.ok(
			memberRepository.findAll()
				.stream()
				.map(MemberResponse::new)
				.toList()
		);
	}

	@GetMapping("/members/{memberId}/accounts")
	@Operation(summary = "회원별 가입 내역 조회")
	public ResponseEntity<List<AccountResponse>> getMemberAccounts(
		@PathVariable Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> BusinessException.notFound("회원을 찾을 수 없습니다"));
		return ResponseEntity.ok(
			accountRepository.findByMember(member)
				.stream()
				.map(AccountResponse::new)
				.toList()
		);
	}

	// mature 만기 처리
	@PatchMapping("/accounts/{accountId}/mature")
	@Operation(summary = "만기 처리")
	public ResponseEntity<Map<String, String>> mature(@PathVariable Long accountId) {
		accountService.mature(accountId);
		return ResponseEntity.ok(Map.of("message", "만기 처리되었습니다"));
	}
}
