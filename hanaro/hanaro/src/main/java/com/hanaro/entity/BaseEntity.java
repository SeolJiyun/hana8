package com.hanaro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass  // 이 클래스 자체는 테이블 안 만들고, 상속받은 클래스에 컬럼 추가
@EntityListeners(AuditingEntityListener.class)  // 생성/수정 시간 자동 기록
public abstract class BaseEntity {

	@CreatedDate  // INSERT 시 자동으로 현재 시간 저장
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate  // UPDATE 시 자동으로 현재 시간 갱신
	private LocalDateTime updatedAt;
}
