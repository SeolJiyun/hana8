package com.hanaro.repository;

import com.hanaro.entity.Account;
import com.hanaro.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

	// 특정 계좌의 거래 내역 최신순 조회
	List<TransactionLog> findByAccountOrderByCreatedAtDesc(Account account);
}
