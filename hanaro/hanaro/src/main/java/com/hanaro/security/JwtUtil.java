package com.hanaro.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {

	private final SecretKey secretKey;

	// security.yml의 jwt.secret 값을 자동으로 주입받음
	public JwtUtil(@Value("${jwt.secret}") String secret) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	// 토큰 생성 (min = 유효시간(분))
	public String generateToken(Map<String, Object> claims, int min) {
		return Jwts.builder()
			.header().add("typ", "JWT").and()
			.claims().add(claims).and()
			.issuedAt(Date.from(ZonedDateTime.now().toInstant()))
			.expiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
			.signWith(secretKey)
			.compact();
	}

	// Access Token (10분) + Refresh Token (24시간) 발급
	public Map<String, Object> generateTokens(String email, String role) {
		Map<String, Object> claims = Map.of("email", email, "role", role);
		String accessToken = generateToken(claims, 10);
		String refreshToken = generateToken(claims, 60 * 24);
		return Map.of(
			"email", email,
			"role", role,
			"accessToken", accessToken,
			"refreshToken", refreshToken
		);
	}

	// 토큰 검증 후 claims(payload) 반환
	public Map<String, Object> validateToken(String token) {
		try {
			return (Map<String, Object>) Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (ExpiredJwtException e) {
			throw new RuntimeException("Token Expired");
		} catch (JwtException e) {
			throw new RuntimeException("Invalid Token");
		}
	}
}
