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
import dev.cass.knorda.api.product.service.ProductService;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductRepositoryTest {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductService productService;

	/**
	 * 동적 쿼리 테스트를 해야 하는데, mocking하다 보면 data.sql에 있는 데이터를 가져오지 못한다.
	 * 그러면 beforeEach에서 데이터를 넣어주는 방법이 있지만, 그렇게 되면 테스트 코드가 너무 길어진다.
	 * 그래서 일단 repositoryTest 클래스에서 테스트를 진행시켰는데, productServiceTest 클래스를 두 개를 작성해야 할지?
	 * 동적 쿼리 부분을 따로 Repository 클래스로 분리하려고 해도, specification은 JpaSpecificationExecutor 인터페이스의 메소드를 호출해야 되기 때문에 순환참조가 일어날 것 같음?
	 * 우선 동적 쿼리에 대한 테스트는 임시로 repositoryTest에 작성하고, 어떻게 변경할지 정할 예정
	 */
	@DisplayName("동적 쿼리 - 회원으로 상품 조회")
	@Test
	void findAllByQueryMemberName() {
		// given
		ProductFindDto.GetProductQuery productQuery = new ProductFindDto.GetProductQuery(null, "admin", null, null);

		// when
		List<Product> products = productService.findAllByQuery(productQuery);

		// then
		assertEquals(2, products.size());
	}

	@DisplayName("동적 쿼리 - 상품명으로 상품 조회")
	@Test
	void findAllByQueryProductName() {
		// given
		ProductFindDto.GetProductQuery productQuery = new ProductFindDto.GetProductQuery("Product 3", null, null, null);

		// when
		List<Product> products = productService.findAllByQuery(productQuery);

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
		List<Product> products = productService.findAllByQuery(productQuery);

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
		List<Product> products = productService.findAllByQuery(productQuery);

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

	@DisplayName("상품 이름으로 상품 조회")
	@Test
	void findByName() {
		// given
		String name = "Product 1";

		// when
		Product product = productRepository.findFirstByNameAndIsDeletedFalse(name).orElse(null);

		// then
		assertNotNull(product);
	}
}