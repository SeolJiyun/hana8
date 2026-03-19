package com.hanaro.repository;

import com.hanaro.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

// JpaRepository<엔티티, PK타입> 상속받으면 기본 CRUD 자동 생성
public interface MemberRepository
	extends JpaRepository<Member, Long>, MemberRepositoryCustom {

	// 메서드 이름으로 쿼리 자동 생성!
	// findBy + 필드명 → SELECT * FROM member WHERE email = ?
	Optional<Member> findByEmail(String email);

	// SELECT * FROM member WHERE email = ? AND deleted = false
	Optional<Member> findByEmailAndDeletedFalse(String email);

	// SELECT COUNT(*) FROM member WHERE email = ?
	boolean existsByEmail(String email);

	// SELECT COUNT(*) FROM member WHERE nickname = ?
	boolean existsByNickname(String nickname);

	// 닉네임으로 검색 (포함 검색)
	List<Member> findByNicknameContaining(String nickname);

	// 닉네임으로 정확히 조회
	Optional<Member> findByNickname(String nickname);

	// 삭제되지 않은 전체 회원 조회 (커스텀 쿼리)
	@Query("SELECT m FROM Member m WHERE m.deleted = false")
	List<Member> findAllActive();
}
