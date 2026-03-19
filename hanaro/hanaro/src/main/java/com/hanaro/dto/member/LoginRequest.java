package com.hanaro.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
	@NotBlank(message = "이메일은 필수입니다")
	@Schema(description = "이메일", example = "admin@hanaro.com")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다")
	@Schema(description = "비밀번호", example = "12345678")
	String password
) {}
