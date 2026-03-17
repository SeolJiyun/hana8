package com.hanaro.repository;

import com.hanaro.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

	// 삭제 안 된 상품만 전체 조회
	List<Product> findByDeletedFalse();

	// 삭제 안 된 상품 단건 조회
	Optional<Product> findByIdAndDeletedFalse(Long id);
}
