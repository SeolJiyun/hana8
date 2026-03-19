package com.hanaro;

import com.hanaro.validator.AccountNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorTest {

	AccountNumberValidator validator;

	@BeforeEach
	void setup() {
		validator = new AccountNumberValidator();
		validator.initialize(null);
	}

	@Test
	@DisplayName("유효한 계좌번호 형식")
	void validAccountNumber() {
		assertThat(validator.isValid("123-4567-8901", null)).isTrue();
		assertThat(validator.isValid("000-0000-0000", null)).isTrue();
	}

	@Test
	@DisplayName("유효하지 않은 계좌번호 형식")
	void invalidAccountNumber() {
		assertThat(validator.isValid("1234567890", null)).isFalse();
		assertThat(validator.isValid("123-456-7890", null)).isFalse();
		assertThat(validator.isValid("abc-defg-hijk", null)).isFalse();
		assertThat(validator.isValid("123-4567-890", null)).isFalse();
	}

	@Test
	@DisplayName("null 또는 빈 값은 통과")
	void nullOrBlankAccountNumber() {
		assertThat(validator.isValid(null, null)).isTrue();
		assertThat(validator.isValid("", null)).isTrue();
		assertThat(validator.isValid("   ", null)).isTrue();
	}
}
