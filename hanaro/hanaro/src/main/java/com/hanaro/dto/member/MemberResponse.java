package com.hanaro.dto.member;

import com.hanaro.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponse {

	private Long id;
	private String email;
	private String nickname;
	private String role;

	// Entity → DTO 변환 생성자
	public MemberResponse(Member member) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.nickname = member.getNickname();
		this.role = member.getRole();
	}
}
