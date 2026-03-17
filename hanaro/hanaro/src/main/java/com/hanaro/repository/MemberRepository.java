package com.hanaro.repository;

import com.hanaro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository<엔티티, PK타입> 상속받으면 기본 CRUD 자동 생성
public interface MemberRepository extends JpaRepository<Member, Long> {

	// 메서드 이름으로 쿼리 자동 생성!
	// findBy + 필드명 → SELECT * FROM member WHERE email = ?
	Optional<Member> findByEmail(String email);

	// SELECT * FROM member WHERE email = ? AND deleted = false
	Optional<Member> findByEmailAndDeletedFalse(String email);

	// SELECT COUNT(*) FROM member WHERE email = ?
	boolean existsByEmail(String email);

	// SELECT COUNT(*) FROM member WHERE nickname = ?
	boolean existsByNickname(String nickname);
}
