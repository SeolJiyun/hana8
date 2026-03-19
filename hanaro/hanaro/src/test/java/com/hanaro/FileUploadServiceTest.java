package com.hanaro;

import com.hanaro.service.FileUploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class FileUploadServiceTest {

	@Autowired
	FileUploadService fileUploadService;

	@Test
	@DisplayName("파일 업로드 성공")
	void uploadSuccess() {
		MockMultipartFile file = new MockMultipartFile(
			"file",
			"test.jpg",
			"image/jpeg",
			"test image content".getBytes()
		);

		String result = fileUploadService.upload(file);

		assertThat(result).contains("/upload/");
		assertThat(result).endsWith(".jpg");
	}

	@Test
	@DisplayName("빈 파일 업로드 실패")
	void uploadEmptyFile() {
		MockMultipartFile emptyFile = new MockMultipartFile(
			"file",
			"empty.jpg",
			"image/jpeg",
			new byte[0]
		);

		assertThatThrownBy(() -> fileUploadService.upload(emptyFile))
			.isInstanceOf(Exception.class);
	}

	@Test
	@DisplayName("null 파일 업로드 실패")
	void uploadNullFile() {
		assertThatThrownBy(() -> fileUploadService.upload(null))
			.isInstanceOf(Exception.class);
	}
}
