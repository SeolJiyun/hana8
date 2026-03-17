package com.hanaro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;  // 계좌 소유자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;  // 가입한 상품 (자유입출금은 null)

	@Column(nullable = false, unique = true, length = 14)
	private String accountNumber;  // 계좌번호 ###-####-####

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountType accountType;  // 자유입출금 / 예금 / 적금

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus status;  // 정상 / 해지 / 만기

	@Column(nullable = false)
	@Builder.Default
	private Long balance = 0L;  // 잔액

	private Double interestRate;  // 적용 이자율

	private java.time.LocalDate maturityDate;  // 만기일

	// 잔액 변경
	public void updateBalance(Long amount) {
		this.balance += amount;
	}

	// 해지 처리
	public void terminate() {
		this.status = AccountStatus.TERMINATED;
	}

	// 만기 처리
	public void mature() {
		this.status = AccountStatus.MATURED;
	}

	public enum AccountType {
		FREE,     // 자유입출금
		DEPOSIT,  // 예금
		SAVINGS   // 적금
	}

	public enum AccountStatus {
		ACTIVE,      // 정상
		TERMINATED,  // 해지
		MATURED      // 만기
	}
}
