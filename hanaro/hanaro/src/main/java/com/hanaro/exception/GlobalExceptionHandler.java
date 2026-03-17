package com.hanaro.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// 모든 Controller에서 발생하는 예외를 여기서 잡음
@RestControllerAdvice
@Slf4j  // log.error() 쓸 수 있게 해주는 Lombok
public class GlobalExceptionHandler {

	// 우리가 만든 BusinessException 처리
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
		log.error("BusinessException: {}", e.getMessage());
		return ResponseEntity
			.status(e.getStatus())
			.body(Map.of(
				"status", e.getStatus().value(),
				"message", e.getMessage()
			));
	}

	// Validation 실패 처리 (@NotBlank, @Email 등)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(
		MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();
		// 어떤 필드가 왜 실패했는지 다 담아서 응답
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(Map.of(
				"status", 400,
				"message", "입력값이 올바르지 않습니다",
				"errors", errors
			));
	}

	// 그 외 모든 예외 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception e) {
		log.error("Unexpected error: {}", e.getMessage());
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(Map.of(
				"status", 500,
				"message", "서버 오류가 발생했습니다"
			));
	}
}
