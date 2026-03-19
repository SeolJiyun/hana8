package com.hanaro.repository;

import com.hanaro.entity.Member;
import com.hanaro.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public MemberRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<Member> findAllActiveMembers() {
		QMember member = QMember.member;
		return queryFactory
			.selectFrom(member)
			.where(member.deleted.eq(false))
			.fetch();
	}
}
