package com.hanaro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaction_log")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;  // 거래 계좌

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType transactionType;  // 입금 / 출금 / 이체

	@Column(nullable = false)
	private Long amount;  // 거래 금액

	@Column(nullable = false)
	private Long balanceAfter;  // 거래 후 잔액

	private String description;  // 거래 내용

	public enum TransactionType {
		DEPOSIT,    // 입금
		WITHDRAWAL, // 출금
		TRANSFER    // 이체
	}
}
