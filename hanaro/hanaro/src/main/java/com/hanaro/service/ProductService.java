package com.hanaro.service;

import com.hanaro.dto.product.ProductRequest;
import com.hanaro.dto.product.ProductResponse;
import com.hanaro.entity.Product;
import com.hanaro.exception.BusinessException;
import com.hanaro.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

	private final ProductRepository productRepository;

	// 상품 전체 조회 (삭제 안 된 것만)
	@Transactional(readOnly = true)  // 조회만 할 때는 readOnly = true (성능 최적화)
	public List<ProductResponse> getProducts() {
		return productRepository.findByDeletedFalse()
			.stream()
			.map(ProductResponse::new)  // Entity → DTO 변환
			.toList();
	}

	// 상품 단건 조회
	@Transactional(readOnly = true)
	public ProductResponse getProduct(Long id) {
		Product product = productRepository.findByIdAndDeletedFalse(id)
			.orElseThrow(() -> BusinessException.notFound("상품을 찾을 수 없습니다"));
		return new ProductResponse(product);
	}

	// 상품 등록 (ADMIN)
	public ProductResponse createProduct(ProductRequest request) {
		Product product = Product.builder()
			.name(request.name())
			.productType(request.productType())
			.depositAmount(request.depositAmount())
			.paymentCycle(request.paymentCycle())
			.subscriptionPeriod(request.subscriptionPeriod())
			.maturityRate(request.maturityRate())
			.earlyTerminationRate(request.earlyTerminationRate())
			.build();
		productRepository.save(product);

		log.info("상품 등록 - name: {}", product.getName());
		return new ProductResponse(product);
	}

	// 상품 수정 (ADMIN)
	public ProductResponse updateProduct(Long id, ProductRequest request) {
		Product product = productRepository.findByIdAndDeletedFalse(id)
			.orElseThrow(() -> BusinessException.notFound("상품을 찾을 수 없습니다"));

		product.update(
			request.name(),
			request.depositAmount(),
			request.subscriptionPeriod(),
			request.maturityRate(),
			request.earlyTerminationRate()
		);

		log.info("상품 수정 - id: {}, name: {}", id, product.getName());
		return new ProductResponse(product);
	}

	// 상품 삭제 (ADMIN) - 실제 삭제 안 하고 deleted = true
	public void deleteProduct(Long id) {
		Product product = productRepository.findByIdAndDeletedFalse(id)
			.orElseThrow(() -> BusinessException.notFound("상품을 찾을 수 없습니다"));
		product.delete();
		log.info("상품 삭제 - id: {}", id);
	}

	// 상품 이미지 URL 업데이트
	public void updateProductImage(Long id, String imageUrl) {
		Product product = productRepository.findByIdAndDeletedFalse(id)
			.orElseThrow(() -> BusinessException.notFound("상품을 찾을 수 없습니다"));
		product.updateImage(imageUrl);
		log.info("상품 이미지 업로드 - id: {}, imageUrl: {}", id, imageUrl);
	}
}
