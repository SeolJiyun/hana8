package com.hanaro.repository;

import com.hanaro.entity.Member;
import java.util.List;

public interface MemberRepositoryCustom {
	List<Member> findAllActiveMembers();
}
