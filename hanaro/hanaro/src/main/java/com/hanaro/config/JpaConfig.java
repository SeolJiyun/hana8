package com.hanaro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration  // Spring 설정 클래스
@EnableJpaAuditing  // BaseEntity의 @CreatedDate, @LastModifiedDate 활성화
public class JpaConfig {
}
