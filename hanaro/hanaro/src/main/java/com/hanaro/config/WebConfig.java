package com.hanaro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${upload.path}")
	private String uploadPath;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// /upload/** 경로로 접근하면 실제 업로드 폴더에서 파일 찾아줌
		registry.addResourceHandler("/upload/**")
			.addResourceLocations("file:" + uploadPath + "/");
	}
}
