package com.hanaro.repository;

import com.hanaro.entity.Account;
import com.hanaro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

	// 특정 회원의 전체 계좌 조회
	List<Account> findByMember(Member member);

	// 계좌번호로 조회
	Optional<Account> findByAccountNumber(String accountNumber);

	// 계좌번호 중복 체크
	boolean existsByAccountNumber(String accountNumber);

	// 특정 회원의 자유입출금 계좌 조회
	Optional<Account> findByMemberAndAccountType(
		Member member,
		Account.AccountType accountType
	);
}
