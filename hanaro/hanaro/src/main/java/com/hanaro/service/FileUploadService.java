package com.hanaro.service;

import com.hanaro.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadService {

	@Value("${upload.path}")
	private String uploadPath;

	private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
	private static final long MAX_TOTAL_SIZE = 10 * 1024 * 1024; // 10MB

	// 단일 파일 업로드 — 날짜별 디렉토리 자동 생성, UUID로 파일명 중복 방지
	public String upload(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw BusinessException.badRequest("파일이 없습니다");
		}
		if (file.getSize() > MAX_FILE_SIZE) {
			throw BusinessException.badRequest("파일 크기는 2MB를 초과할 수 없습니다");
		}

		// 날짜별 디렉토리 생성 (예: upload/20260312)
		String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String dirPath = uploadPath + "/" + dateDir;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// UUID로 파일명 중복 방지
		String originalFilename = file.getOriginalFilename();
		String ext = originalFilename != null && originalFilename.contains(".")
			? originalFilename.substring(originalFilename.lastIndexOf("."))
			: "";
		String savedFilename = UUID.randomUUID().toString() + ext;
		String savedPath = dirPath + "/" + savedFilename;

		try {
			file.transferTo(new File(savedPath));
			log.info("파일 업로드 완료 - path: {}", savedPath);
		} catch (IOException e) {
			throw BusinessException.badRequest("파일 업로드에 실패했습니다");
		}

		return "/upload/" + dateDir + "/" + savedFilename;
	}
}
