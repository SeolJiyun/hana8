package com.hanaro.controller;

import com.hanaro.exception.BusinessException;
import com.hanaro.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "JWT 토큰 갱신 API")
public class JwtRefreshController {

	private final JwtUtil jwtUtil;

	@GetMapping("/refresh")
	@Operation(summary = "Access Token 갱신")
	public ResponseEntity<Map<String, Object>> refresh(
		@RequestHeader("Authorization") String authHeader,
		@RequestParam String refreshToken) {

		if (refreshToken == null) throw BusinessException.badRequest("NULL_REFRESHTOKEN");
		if (authHeader == null || authHeader.length() < 7) throw BusinessException.badRequest("INVALID_TOKEN_STRING");

		String accessToken = authHeader.substring(7);

		// Access Token 아직 유효하면 그대로 반환
		if (!isExpired(accessToken)) {
			return ResponseEntity.ok(Map.of(
				"accessToken", accessToken,
				"refreshToken", refreshToken
			));
		}

		// Refresh Token 검증 후 새 토큰 발급
		Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
		String newAccessToken = jwtUtil.generateToken(claims, 10);
		String newRefreshToken = hasTimeLeft((long) claims.get("exp"))
			? jwtUtil.generateToken(claims, 60 * 24)
			: refreshToken;

		return ResponseEntity.ok(Map.of(
			"accessToken", newAccessToken,
			"refreshToken", newRefreshToken
		));
	}

	private boolean isExpired(String token) {
		try {
			jwtUtil.validateToken(token);
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	private boolean hasTimeLeft(long exp) {
		long now = System.currentTimeMillis() / 1000;
		return (exp - now) < 60 * 60;
	}
}
