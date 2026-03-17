package com.hanaro.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class AccountNumberValidator
	implements ConstraintValidator<AccountNumber, String> {

	// ###-####-#### 형식 (숫자 11자리, 하이픈 포함)
	private static final Pattern PATTERN =
		Pattern.compile("^\\d{3}-\\d{4}-\\d{4}$");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext ctx) {
		// null이나 빈 값은 통과 (필수 여부는 @NotBlank가 담당)
		if (value == null || value.isBlank()) return true;
		return PATTERN.matcher(value).matches();
	}
}
