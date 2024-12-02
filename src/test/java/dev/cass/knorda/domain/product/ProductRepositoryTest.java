package dev.cass.knorda.domain.product;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.product.dto.ProductFindDto;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductRepositoryTest {
	@Autowired
	private ProductRepository productRepository;

	@DisplayName("동적 쿼리 - 회원으로 상품 조회")
	@Test
	void findAllByQueryMemberName() {
		// given
		ProductFindDto.GetProductQuery productQuery = new ProductFindDto.GetProductQuery(null, "admin", null, null);

		// when
		List<Product> products = productRepository.findAll(ProductSpecification.searchProductQuery(productQuery));

		// then
		assertEquals(2, products.size());
	}

	@DisplayName("동적 쿼리 - 상품명으로 상품 조회")
	@Test
	void findAllByQueryProductName() {
		// given
		ProductFindDto.GetProductQuery productQuery = new ProductFindDto.GetProductQuery("Product 3", null, null, null);

		// when
		List<Product> products = productRepository.findAll(ProductSpecification.searchProductQuery(productQuery));

		// then
		assertEquals(1, products.size());
	}

	@DisplayName("동적 쿼리 - 날짜로 상품 조회")
	@Test
	void findAllByQueryStartDateAndEndDate() {
		// given
		ProductFindDto.GetProductQuery productQuery = new ProductFindDto.GetProductQuery(null, null,
			LocalDateTime.of(2020, 10, 10, 0, 0, 0),
			LocalDateTime.of(2021, 10, 10, 0, 0, 0));

		// when
		List<Product> products = productRepository.findAll(ProductSpecification.searchProductQuery(productQuery));

		// then
		assertEquals(2, products.size());
	}

	@DisplayName("동적 쿼리 - 사용자와 날짜로 상품 조회")
	@Test
	void findAllByQueryMemberNameAndStartDateAndEndDate() {
		// given
		ProductFindDto.GetProductQuery productQuery = new ProductFindDto.GetProductQuery(null, "admin",
			LocalDateTime.of(2020, 10, 10, 0, 0, 0),
			LocalDateTime.of(2021, 10, 10, 0, 0, 0));

		// when
		List<Product> products = productRepository.findAll(ProductSpecification.searchProductQuery(productQuery));

		// then
		assertEquals(1, products.size());
	}

	@DisplayName("사용자 id로 상품 조회")
	@Test
	void findByUserId() {
		// given
		int memberId = 1;

		// when
		List<Product> product = productRepository.findAllByMemberMemberId(memberId);

		// then
		assertFalse(product.isEmpty());
	}

	@DisplayName("상품 이름으로 상품 존재 여부 확인")
	@Test
	void findByName() {
		// given
		String name = "Product 1";

		// when
		boolean product = productRepository.existsByName(name);

		// then
		assertTrue(product);
	}
}