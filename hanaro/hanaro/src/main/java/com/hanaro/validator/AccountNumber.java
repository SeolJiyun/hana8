package com.hanaro.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AccountNumberValidator.class)
public @interface AccountNumber {
	String message() default "계좌번호 형식이 올바르지 않습니다 (###-####-#### 형식의 숫자 11자리)";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
