package com.hanaro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity  // 이 클래스가 DB 테이블과 연결됨
@Table(name = "member")  // 테이블명 지정
@Getter
@Builder  // 객체 생성할 때 builder 패턴 사용 가능
@NoArgsConstructor  // 파라미터 없는 기본 생성자 자동 생성
@AllArgsConstructor  // 모든 필드 파라미터 생성자 자동 생성
public class Member extends BaseEntity {

	@Id  // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // auto increment
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true, length = 50)
	private String nickname;

	@Column(nullable = false, length = 20)
	private String role;  // ROLE_ADMIN, ROLE_USER

	@Column(nullable = false)
	@ColumnDefault("false")
	private boolean deleted;  // 탈퇴 여부 (실제 삭제 안 하고 표시만)

	// 비밀번호 변경용 메서드
	public void changePassword(String encodedPassword) {
		this.password = encodedPassword;
	}

	// 탈퇴 처리용 메서드
	public void delete() {
		this.deleted = true;
	}
}
