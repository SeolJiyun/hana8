package com.hanaro;

import com.hanaro.entity.Member;
import com.hanaro.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Test
	@DisplayName("회원 저장 및 이메일로 조회")
	void saveAndFindByEmail() {
		Member member = Member.builder()
			.email("test@hanaro.com")
			.password("encoded_password")
			.nickname("테스터")
			.role("ROLE_USER")
			.build();
		memberRepository.save(member);

		Optional<Member> found = memberRepository.findByEmailAndDeletedFalse("test@hanaro.com");
		assertThat(found).isPresent();
		assertThat(found.get().getNickname()).isEqualTo("테스터");
	}

	@Test
	@DisplayName("이메일 중복 체크")
	void existsByEmail() {
		Member member = Member.builder()
			.email("dup@hanaro.com")
			.password("pw")
			.nickname("중복테스터")
			.role("ROLE_USER")
			.build();
		memberRepository.save(member);

		assertThat(memberRepository.existsByEmail("dup@hanaro.com")).isTrue();
		assertThat(memberRepository.existsByEmail("none@hanaro.com")).isFalse();
	}

	@Test
	@DisplayName("닉네임 중복 체크")
	void existsByNickname() {
		Member member = Member.builder()
			.email("nick@hanaro.com")
			.password("pw")
			.nickname("닉네임중복")
			.role("ROLE_USER")
			.build();
		memberRepository.save(member);

		assertThat(memberRepository.existsByNickname("닉네임중복")).isTrue();
		assertThat(memberRepository.existsByNickname("없는닉네임")).isFalse();
	}

	@Test
	@DisplayName("삭제된 회원은 조회 안됨")
	void deletedMemberNotFound() {
		Member member = Member.builder()
			.email("deleted@hanaro.com")
			.password("pw")
			.nickname("삭제회원")
			.role("ROLE_USER")
			.deleted(true)
			.build();
		memberRepository.save(member);

		Optional<Member> found = memberRepository.findByEmailAndDeletedFalse("deleted@hanaro.com");
		assertThat(found).isEmpty();
	}

	@Test
	@DisplayName("닉네임으로 회원 조회")
	void findByNickname() {
		Member member = Member.builder()
			.email("nick2@hanaro.com")
			.password("pw")
			.nickname("닉네임조회")
			.role("ROLE_USER")
			.build();
		memberRepository.save(member);

		assertThat(memberRepository.findByNickname("닉네임조회")).isPresent();
		assertThat(memberRepository.findByNickname("없는닉네임")).isEmpty();
	}

	@Test
	@DisplayName("삭제되지 않은 전체 회원 조회")
	void findAllActive() {
		Member member = Member.builder()
			.email("active@hanaro.com")
			.password("pw")
			.nickname("활성회원")
			.role("ROLE_USER")
			.build();
		memberRepository.save(member);

		assertThat(memberRepository.findAllActive()).isNotEmpty();
	}

	@Test
	@DisplayName("커스텀 쿼리 - 활성 회원 조회")
	void findAllActiveMembers() {
		Member member = Member.builder()
			.email("custom@hanaro.com")
			.password("pw")
			.nickname("커스텀조회")
			.role("ROLE_USER")
			.build();
		memberRepository.save(member);

		assertThat(memberRepository.findAllActiveMembers()).isNotEmpty();
	}
}
