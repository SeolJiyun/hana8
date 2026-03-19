package com.hanaro.controller;

import com.hanaro.dto.product.ProductRequest;
import com.hanaro.dto.product.ProductResponse;
import com.hanaro.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.hanaro.service.FileUploadService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "상품", description = "예적금 상품 API")
public class ProductController {

	private final ProductService productService;
	private final FileUploadService fileUploadService;

	// 상품 목록 조회 - 누구나 가능
	@GetMapping("/public/products")
	@Operation(summary = "상품 목록 조회")
	public ResponseEntity<List<ProductResponse>> getProducts() {
		return ResponseEntity.ok(productService.getProducts());
	}

	// 상품 상세 조회 - 누구나 가능
	@GetMapping("/public/products/{id}")
	@Operation(summary = "상품 상세 조회")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProduct(id));
	}

	// 상품 등록 - ADMIN만
	@PostMapping("/admin/products")
	@PreAuthorize("hasRole('ADMIN')")  // ADMIN만 접근 가능
	@Operation(summary = "상품 등록 (관리자)")
	public ResponseEntity<ProductResponse> createProduct(
		@Valid @RequestBody ProductRequest request) {
		return ResponseEntity.ok(productService.createProduct(request));
	}

	// 상품 수정 - ADMIN만
	@PutMapping("/admin/products/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "상품 수정 (관리자)")
	public ResponseEntity<ProductResponse> updateProduct(
		@PathVariable Long id,
		@Valid @RequestBody ProductRequest request) {
		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	// 상품 삭제 - ADMIN만
	@DeleteMapping("/admin/products/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "상품 삭제 (관리자)")
	public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok(Map.of("message", "상품이 삭제되었습니다"));
	}

	// 상품 이미지 업로드 - ADMIN만
	@PostMapping(value = "/admin/products/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "상품 이미지 업로드 (관리자)")
	public ResponseEntity<Map<String, String>> uploadProductImage(
		@PathVariable Long id,
		@RequestParam("file") MultipartFile file) {
		String imageUrl = fileUploadService.upload(file);
		productService.updateProductImage(id, imageUrl);
		return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
	}
}
