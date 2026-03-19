package com.hanaro.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record MemberRequest(
	@NotBlank(message = "이메일은 필수입니다")
	@Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
		message = "이메일 형식이 올바르지 않습니다")
	@Schema(description = "이메일", example = "user@hanaro.com")
	String email,

	@NotBlank(message = "비밀번호는 필수입니다")
	@Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
	@Schema(description = "비밀번호", example = "Password1!")
	String password,

	@NotBlank(message = "닉네임은 필수입니다")
	@Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다")
	@Schema(description = "닉네임", example = "홍길동")
	String nickname
) {}
