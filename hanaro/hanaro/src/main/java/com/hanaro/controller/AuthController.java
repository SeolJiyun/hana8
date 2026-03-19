package com.hanaro.controller;

import com.hanaro.dto.member.LoginRequest;
import com.hanaro.dto.member.MemberRequest;
import com.hanaro.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController  // JSON 응답하는 Controller
@RequestMapping("/api/auth")  // 공통 URL prefix
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입/로그인 API")  // Swagger 그룹
public class AuthController {

	private final MemberService memberService;

	@PostMapping("/register")
	@Operation(summary = "회원가입")  // Swagger 설명
	public ResponseEntity<Map<String, String>> register(
		@Valid @RequestBody MemberRequest request) {  // @Valid = Validation 실행
		memberService.register(request);
		return ResponseEntity.ok(Map.of("message", "회원가입이 완료되었습니다"));
	}

	@PostMapping("/login")
	@Operation(summary = "로그인 - JWT 토큰 발급")
	public ResponseEntity<Map<String, Object>> login(
		@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(memberService.login(request));
	}
}
