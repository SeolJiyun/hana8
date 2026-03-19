package com.hanaro;

import com.hanaro.entity.Product;
import com.hanaro.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductRepositoryTest {

	@Autowired
	ProductRepository productRepository;

	@Test
	@DisplayName("상품 저장 및 전체 조회 (삭제 안된 것만)")
	void saveAndFindAll() {
		Product p1 = Product.builder()
			.name("정기예금")
			.productType(Product.ProductType.DEPOSIT)
			.depositAmount(1000000L)
			.subscriptionPeriod(12)
			.maturityRate(3.5)
			.earlyTerminationRate(1.5)
			.deleted(false)
			.build();
		Product p2 = Product.builder()
			.name("삭제된상품")
			.productType(Product.ProductType.SAVINGS)
			.depositAmount(100000L)
			.subscriptionPeriod(24)
			.maturityRate(4.0)
			.earlyTerminationRate(2.0)
			.deleted(true)
			.build();
		productRepository.save(p1);
		productRepository.save(p2);

		List<Product> products = productRepository.findByDeletedFalse();
		assertThat(products.stream()
			.filter(p -> p.getName().equals("정기예금"))
			.count()).isEqualTo(1);
	}

	@Test
	@DisplayName("ID로 상품 조회 (삭제 안된 것만)")
	void findByIdAndDeletedFalse() {
		Product product = Product.builder()
			.name("자유적금")
			.productType(Product.ProductType.SAVINGS)
			.depositAmount(100000L)
			.subscriptionPeriod(24)
			.maturityRate(4.0)
			.earlyTerminationRate(2.0)
			.deleted(false)
			.build();
		Product saved = productRepository.save(product);

		Optional<Product> found = productRepository.findByIdAndDeletedFalse(saved.getId());
		assertThat(found).isPresent();

		Optional<Product> notFound = productRepository.findByIdAndDeletedFalse(999L);
		assertThat(notFound).isEmpty();
	}
}
