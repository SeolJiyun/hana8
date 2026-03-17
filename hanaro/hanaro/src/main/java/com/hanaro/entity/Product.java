package com.hanaro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;  // 상품명

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductType productType;  // 예금 / 적금

	@Column(nullable = false)
	private Long depositAmount;  // 납입 금액

	@Enumerated(EnumType.STRING)
	private PaymentCycle paymentCycle;  // 적금 납입 주기 (월/주) - 예금은 null

	@Column(nullable = false)
	private Integer subscriptionPeriod;  // 가입 기간 (개월)

	@Column(nullable = false)
	private Double maturityRate;  // 만기 수익률

	@Column(nullable = false)
	private Double earlyTerminationRate;  // 해지 수익률

	private String imageUrl;  // 대표 이미지 경로

	@Column(nullable = false)
	@Builder.Default
	private boolean deleted = false;

	// 상품 수정용 메서드
	public void update(String name, Long depositAmount, Integer subscriptionPeriod,
		Double maturityRate, Double earlyTerminationRate) {
		this.name = name;
		this.depositAmount = depositAmount;
		this.subscriptionPeriod = subscriptionPeriod;
		this.maturityRate = maturityRate;
		this.earlyTerminationRate = earlyTerminationRate;
	}

	public void updateImage(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void delete() {
		this.deleted = true;
	}

	// 열거형 (Enum) - 정해진 값만 허용
	public enum ProductType {
		DEPOSIT,  // 예금
		SAVINGS   // 적금
	}

	public enum PaymentCycle {
		MONTHLY,  // 월
		WEEKLY    // 주
	}
}
