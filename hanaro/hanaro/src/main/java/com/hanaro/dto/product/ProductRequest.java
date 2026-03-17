package com.hanaro.dto.product;

import com.hanaro.entity.Product;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequest {

	@NotBlank(message = "상품명은 필수입니다")
	@Size(max = 100, message = "상품명은 100자 이하여야 합니다")
	private String name;

	@NotNull(message = "상품 종류는 필수입니다")
	private Product.ProductType productType;  // DEPOSIT / SAVINGS

	@NotNull(message = "납입 금액은 필수입니다")
	@Min(value = 1000, message = "납입 금액은 1000원 이상이어야 합니다")
	private Long depositAmount;

	// 적금일 때만 필요 (예금은 null 가능)
	private Product.PaymentCycle paymentCycle;

	@NotNull(message = "가입 기간은 필수입니다")
	@Min(value = 1, message = "가입 기간은 1개월 이상이어야 합니다")
	private Integer subscriptionPeriod;

	@NotNull(message = "만기 수익률은 필수입니다")
	@DecimalMin(value = "0.0", message = "수익률은 0 이상이어야 합니다")
	private Double maturityRate;

	@NotNull(message = "해지 수익률은 필수입니다")
	@DecimalMin(value = "0.0", message = "수익률은 0 이상이어야 합니다")
	private Double earlyTerminationRate;
}
