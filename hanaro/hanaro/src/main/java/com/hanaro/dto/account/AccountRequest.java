package com.hanaro.dto.account;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountRequest {

	@NotNull(message = "상품 ID는 필수입니다")
	private Long productId;

	// 희망 계좌번호 (없으면 자동 생성)
	private String desiredAccountNumber;
}
