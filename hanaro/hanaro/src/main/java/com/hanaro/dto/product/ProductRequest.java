package com.hanaro.dto.product;

import com.hanaro.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record ProductRequest(
	@NotBlank(message = "상품명은 필수입니다")
	@Size(max = 100, message = "상품명은 100자 이하여야 합니다")
	@Schema(description = "상품명", example = "하나 정기예금")
	String name,

	@NotNull(message = "상품 종류는 필수입니다")
	@Schema(description = "상품 종류", example = "DEPOSIT")
	Product.ProductType productType,

	@NotNull(message = "납입 금액은 필수입니다")
	@Min(value = 1000, message = "납입 금액은 1000원 이상이어야 합니다")
	@Schema(description = "납입 금액", example = "1000000")
	Long depositAmount,

	@Schema(description = "납입 주기 (적금만 해당)", example = "MONTHLY")
	Product.PaymentCycle paymentCycle,

	@NotNull(message = "가입 기간은 필수입니다")
	@Min(value = 1, message = "가입 기간은 1개월 이상이어야 합니다")
	@Schema(description = "가입 기간 (개월)", example = "12")
	Integer subscriptionPeriod,

	@NotNull(message = "만기 수익률은 필수입니다")
	@DecimalMin(value = "0.0", message = "수익률은 0 이상이어야 합니다")
	@Schema(description = "만기 수익률", example = "3.5")
	Double maturityRate,

	@NotNull(message = "해지 수익률은 필수입니다")
	@DecimalMin(value = "0.0", message = "수익률은 0 이상이어야 합니다")
	@Schema(description = "해지 수익률", example = "1.5")
	Double earlyTerminationRate
) {}
