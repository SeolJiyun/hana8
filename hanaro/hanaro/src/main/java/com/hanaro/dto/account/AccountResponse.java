package com.hanaro.dto.account;

import com.hanaro.entity.Account;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class AccountResponse {

	private Long id;
	private String accountNumber;
	private Account.AccountType accountType;
	private Account.AccountStatus status;
	private Long balance;
	private Double interestRate;
	private LocalDate maturityDate;
	private Long accruedInterest;  // 지금까지 쌓인 이자

	public AccountResponse(Account account) {
		this.id = account.getId();
		this.accountNumber = account.getAccountNumber();
		this.accountType = account.getAccountType();
		this.status = account.getStatus();
		this.balance = account.getBalance();
		this.interestRate = account.getInterestRate();
		this.maturityDate = account.getMaturityDate();
		// 쌓인 이자 계산 (잔액 * 이자율 * 경과일수 / 365)
		this.accruedInterest = calculateInterest(account);
	}

	private Long calculateInterest(Account account) {
		if (account.getInterestRate() == null || account.getBalance() == 0) return 0L;
		long days = ChronoUnit.DAYS.between(
			account.getCreatedAt().toLocalDate(),
			LocalDate.now()
		);
		return (long)(account.getBalance() * account.getInterestRate() / 100 * days / 365);
	}
}
