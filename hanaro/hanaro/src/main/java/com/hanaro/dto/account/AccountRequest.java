package com.hanaro.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
	@NotNull(message = "상품 ID는 필수입니다")
	@Schema(description = "상품 ID", example = "1")
	Long productId,

	@Schema(description = "희망 계좌번호 (없으면 자동 생성)", example = "123-4567-8901")
	String desiredAccountNumber
) {}
