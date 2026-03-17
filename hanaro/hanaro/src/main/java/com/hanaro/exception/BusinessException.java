package com.hanaro.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

	private final HttpStatus status;

	public BusinessException(String message, HttpStatus status) {
		super(message);  // 부모 클래스(RuntimeException)에 메시지 전달
		this.status = status;
	}

	// 자주 쓰는 에러 팩토리 메서드
	public static BusinessException notFound(String message) {
		return new BusinessException(message, HttpStatus.NOT_FOUND);  // 404
	}

	public static BusinessException badRequest(String message) {
		return new BusinessException(message, HttpStatus.BAD_REQUEST);  // 400
	}

	public static BusinessException unauthorized(String message) {
		return new BusinessException(message, HttpStatus.UNAUTHORIZED);  // 401
	}

	public static BusinessException forbidden(String message) {
		return new BusinessException(message, HttpStatus.FORBIDDEN);  // 403
	}
}
