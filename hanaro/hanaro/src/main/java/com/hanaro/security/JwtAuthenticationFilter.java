package com.hanaro.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	// JWT 검증 안 해도 되는 경로들
	private static final String[] EXCLUDE_PATTERNS = {
		"/api/auth/**",     // 로그인, 회원가입
		"/api/public/**",   // 공개 API
		"/swagger-ui/**",   // Swagger UI
		"/hanaro/api-docs/**",
		"/actuator/**",
		"/favicon.ico"
	};

	private final JwtUtil jwtUtil;
	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final ObjectMapper objectMapper = new ObjectMapper();

	// 제외 경로면 필터 건너뜀
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return Arrays.stream(EXCLUDE_PATTERNS)
			.anyMatch(pattern -> pathMatcher.match(pattern, path));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain)
		throws ServletException, IOException {

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			sendError(response, "인증 토큰이 없습니다");
			return;
		}

		try {
			// "Bearer " 제거하고 토큰만 추출
			Map<String, Object> claims = jwtUtil.validateToken(authHeader.substring(7));
			String email = (String) claims.get("email");
			String role = (String) claims.get("role");

			// Spring Security에 인증 정보 등록
			UsernamePasswordAuthenticationToken auth =
				new UsernamePasswordAuthenticationToken(
					email, null,
					List.of(new SimpleGrantedAuthority(role))
				);
			SecurityContextHolder.getContext().setAuthentication(auth);

		} catch (Exception e) {
			sendError(response, "유효하지 않은 토큰입니다");
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void sendError(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(
			objectMapper.writeValueAsString(Map.of("status", 401, "message", message))
		);
	}
}
