package com.hanaro.service;

import com.hanaro.dto.account.AccountRequest;
import com.hanaro.dto.account.AccountResponse;
import com.hanaro.entity.Account;
import com.hanaro.entity.Member;
import com.hanaro.entity.Product;
import com.hanaro.entity.TransactionLog;
import com.hanaro.exception.BusinessException;
import com.hanaro.repository.AccountRepository;
import com.hanaro.repository.MemberRepository;
import com.hanaro.repository.ProductRepository;
import com.hanaro.repository.TransactionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService {

	private final AccountRepository accountRepository;
	private final MemberRepository memberRepository;
	private final ProductRepository productRepository;
	private final TransactionLogRepository transactionLogRepository;

	// 내 계좌 전체 조회
	@Transactional(readOnly = true)
	public List<AccountResponse> getMyAccounts(String email) {
		Member member = getMember(email);
		return accountRepository.findByMember(member)
			.stream()
			.map(AccountResponse::new)
			.toList();
	}

	// 상품 가입
	public AccountResponse subscribe(String email, AccountRequest request) {
		Member member = getMember(email);
		Product product = productRepository.findByIdAndDeletedFalse(request.productId())
			.orElseThrow(() -> BusinessException.notFound("상품을 찾을 수 없습니다"));

		// 희망 계좌번호 있으면 중복 체크
		String accountNumber = request.desiredAccountNumber();
		if (accountNumber != null && !accountNumber.isBlank()) {
			if (accountRepository.existsByAccountNumber(accountNumber)) {
				throw BusinessException.badRequest("이미 사용 중인 계좌번호입니다");
			}
		} else {
			accountNumber = generateAccountNumber();
		}

		Account account = Account.builder()
			.member(member)
			.product(product)
			.accountNumber(accountNumber)
			.accountType(product.getProductType() == Product.ProductType.DEPOSIT
				? Account.AccountType.DEPOSIT : Account.AccountType.SAVINGS)
			.status(Account.AccountStatus.ACTIVE)
			.balance(product.getDepositAmount())
			.interestRate(product.getMaturityRate())
			.maturityDate(LocalDate.now().plusMonths(product.getSubscriptionPeriod()))
			.build();
		accountRepository.save(account);

		// 거래 내역 기록
		transactionLogRepository.save(TransactionLog.builder()
			.account(account)
			.transactionType(TransactionLog.TransactionType.DEPOSIT)
			.amount(product.getDepositAmount())
			.balanceAfter(product.getDepositAmount())
			.description("상품 가입")
			.build());

		log.info("상품 가입 - email: {}, product: {}", email, product.getName());
		return new AccountResponse(account);
	}

	// 중도 해지
	public void terminate(String email, Long accountId) {
		Member member = getMember(email);
		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> BusinessException.notFound("계좌를 찾을 수 없습니다"));

		// 본인 계좌인지 확인
		if (!account.getMember().getId().equals(member.getId())) {
			throw BusinessException.forbidden("본인 계좌만 해지할 수 있습니다");
		}

		// 자유입출금 통장은 해지 불가
		if (account.getAccountType() == Account.AccountType.FREE) {
			throw BusinessException.badRequest("자유입출금 통장은 해지할 수 없습니다");
		}

		// 해지 이자 계산 (중도해지율 적용)
		long interest = (long)(account.getBalance()
			* account.getProduct().getEarlyTerminationRate() / 100);

		account.terminate();

		// 거래 내역 기록
		transactionLogRepository.save(TransactionLog.builder()
			.account(account)
			.transactionType(TransactionLog.TransactionType.WITHDRAWAL)
			.amount(account.getBalance() + interest)
			.balanceAfter(0L)
			.description("중도 해지 (이자: " + interest + "원)")
			.build());

		log.info("중도 해지 - email: {}, accountId: {}", email, accountId);
	}

	// 이체 (자유입출금 → 다른 계좌)
	public void transfer(String email, Long fromAccountId, Long toAccountNumber, Long amount) {
		Member member = getMember(email);
		Account fromAccount = accountRepository.findById(fromAccountId)
			.orElseThrow(() -> BusinessException.notFound("출금 계좌를 찾을 수 없습니다"));

		if (!fromAccount.getMember().getId().equals(member.getId())) {
			throw BusinessException.forbidden("본인 계좌만 이체할 수 있습니다");
		}
		if (fromAccount.getBalance() < amount) {
			throw BusinessException.badRequest("잔액이 부족합니다");
		}

		fromAccount.updateBalance(-amount);

		transactionLogRepository.save(TransactionLog.builder()
			.account(fromAccount)
			.transactionType(TransactionLog.TransactionType.TRANSFER)
			.amount(amount)
			.balanceAfter(fromAccount.getBalance())
			.description("이체")
			.build());

		log.info("이체 - email: {}, amount: {}", email, amount);
	}

	// 만기 처리 - 원금+이자를 자유입출금 통장으로 이체
	public void mature(Long accountId) {
		Account account = accountRepository.findById(accountId)
			.orElseThrow(() -> BusinessException.notFound("계좌를 찾을 수 없습니다"));

		if (account.getStatus() != Account.AccountStatus.ACTIVE) {
			throw BusinessException.badRequest("활성 상태인 계좌만 만기 처리할 수 있습니다");
		}

		// 자유입출금 통장은 만기 처리 불가
		if (account.getAccountType() == Account.AccountType.FREE) {
			throw BusinessException.badRequest("자유입출금 통장은 만기 처리할 수 없습니다");
		}

		// 만기 이자 계산
		long interest = (long)(account.getBalance()
			* account.getProduct().getMaturityRate() / 100);
		long totalAmount = account.getBalance() + interest;

		// 계좌 만기 처리
		account.mature();

		// 자유입출금 통장 찾기
		Account freeAccount = accountRepository
			.findByMemberAndAccountType(account.getMember(), Account.AccountType.FREE)
			.orElseThrow(() -> BusinessException.notFound("자유입출금 통장을 찾을 수 없습니다"));

		// 자유입출금 통장에 이체
		freeAccount.updateBalance(totalAmount);

		// 거래 내역 기록
		transactionLogRepository.save(TransactionLog.builder()
			.account(freeAccount)
			.transactionType(TransactionLog.TransactionType.DEPOSIT)
			.amount(totalAmount)
			.balanceAfter(freeAccount.getBalance())
			.description("만기 처리 이체 (원금: " + account.getBalance() + "원, 이자: " + interest + "원)")
			.build());

		log.info("만기 처리 - accountId: {}, 이체금액: {}원", accountId, totalAmount);
	}

	private Member getMember(String email) {
		return memberRepository.findByEmailAndDeletedFalse(email)
			.orElseThrow(() -> BusinessException.notFound("회원을 찾을 수 없습니다"));
	}

	private String generateAccountNumber() {
		String raw = String.valueOf(System.currentTimeMillis()).substring(2, 13);
		return raw.substring(0, 3) + "-" + raw.substring(3, 7) + "-" + raw.substring(7, 11);
	}
}
