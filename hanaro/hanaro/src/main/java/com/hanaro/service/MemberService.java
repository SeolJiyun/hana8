package com.hanaro.service;

import com.hanaro.dto.member.LoginRequest;
import com.hanaro.dto.member.MemberRequest;
import com.hanaro.entity.Account;
import com.hanaro.entity.Member;
import com.hanaro.exception.BusinessException;
import com.hanaro.repository.AccountRepository;
import com.hanaro.repository.MemberRepository;
import com.hanaro.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j  // log.info() 쓸 수 있게 — logs/user.log에 기록됨
@Transactional  // 메서드 실행 중 에러나면 DB 롤백
public class MemberService {

	private final MemberRepository memberRepository;
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	// 회원가입
	public void register(MemberRequest request) {
		// 이메일 중복 체크
		if (memberRepository.existsByEmail(request.email())) {
			throw BusinessException.badRequest("이미 사용 중인 이메일입니다");
		}
		// 닉네임 중복 체크
		if (memberRepository.existsByNickname(request.nickname())) {
			throw BusinessException.badRequest("이미 사용 중인 닉네임입니다");
		}

		// 회원 저장
		Member member = Member.builder()
			.email(request.email())
			.password(passwordEncoder.encode(request.password()))  // 비밀번호 암호화
			.nickname(request.nickname())
			.role("ROLE_USER")
			.build();
		memberRepository.save(member);

		// 자유입출금 통장 자동 생성
		Account freeAccount = Account.builder()
			.member(member)
			.accountNumber(generateAccountNumber())
			.accountType(Account.AccountType.FREE)
			.status(Account.AccountStatus.ACTIVE)
			.balance(0L)
			.build();
		accountRepository.save(freeAccount);

		log.info("회원가입 완료 - email: {}", member.getEmail());
	}

	// 로그인
	public Map<String, Object> login(LoginRequest request) {
		Member member = memberRepository.findByEmailAndDeletedFalse(request.email())
			.orElseThrow(() -> BusinessException.badRequest("이메일 또는 비밀번호가 올바르지 않습니다"));

		// 비밀번호 검증
		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw BusinessException.badRequest("이메일 또는 비밀번호가 올바르지 않습니다");
		}

		log.info("로그인 성공 - email: {}", member.getEmail());
		return jwtUtil.generateTokens(member.getEmail(), member.getRole());
	}

	// 계좌번호 자동 생성 (###-####-####)
	private String generateAccountNumber() {
		String raw = String.valueOf(System.currentTimeMillis()).substring(2, 13);
		return raw.substring(0, 3) + "-" + raw.substring(3, 7) + "-" + raw.substring(7, 11);
	}
}
