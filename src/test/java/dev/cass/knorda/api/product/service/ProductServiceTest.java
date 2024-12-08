package dev.cass.knorda.api.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.product.dto.ProductFindDto;
import dev.cass.knorda.api.product.dto.ProductRegisterDto;
import dev.cass.knorda.api.product.exception.ProductNotExistException;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.product.Product;
import dev.cass.knorda.domain.product.ProductRepository;
import dev.cass.knorda.domain.product.ProductSpecification;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@DisplayName("ID로 상품 조회")
	@Test
	void findById() {
		// given
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.build();

		doReturn(Optional.of(product)).when(productRepository).findFirstByProductId(1);

		// when
		Product product1 = productService.findById(1);

		// then
		assertEquals(product1.getName(), product.getName());
	}

	@DisplayName("ID로 상품 조회 - 상품 없음")
	@Test
	void findByIdNotFound() {
		// given
		doReturn(Optional.empty()).when(productRepository).findFirstByProductId(1);

		// when
		assertThrows(ProductNotExistException.class, () -> productService.findById(1));
	}

	@DisplayName("상품 저장")
	@Test
	void save() {
		// given
		ProductRegisterDto.RegisterRequest productQuery = new ProductRegisterDto.RegisterRequest("Product 1",
			"Product 1 description");
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.build();

		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.build();

		product.setMember(member);

		Product resultProduct = productQuery.toEntity();
		resultProduct.setMember(member);

		doReturn(product).when(productRepository).save(any(Product.class));

		// when
		Product product1 = productService.save(resultProduct);

		// then
		assertEquals(product1.getName(), product.getName());
	}

	@DisplayName("상품 수정")
	@Test
	void update() {
		// given
		ProductRegisterDto.RegisterRequest productQuery = new ProductRegisterDto.RegisterRequest("Product 1 new",
			"Product 1 description new");
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.build();

		product.update(productQuery.getProductName(), productQuery.getDescription());

		doReturn(product).when(productRepository).save(product);

		product.update(productQuery.getProductName(), productQuery.getDescription());

		// when
		Product product1 = productService.save(product);

		// then
		assertEquals(product1.getName(), productQuery.getProductName());
	}

	@DisplayName("상품 삭제")
	@Test
	void delete() {
		// given
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.build();

		product.delete();

		doReturn(product).when(productRepository).save(product);

		// when
		productService.delete(product);

		// then
		verify(productRepository, times(1)).save(product);
		doReturn(Optional.empty()).when(productRepository).findFirstByProductId(1);
		assertThrows(ProductNotExistException.class, () -> productService.findById(1));
	}

	@DisplayName("상품 이름 중복 확인")
	@Test
	void isExistProductName() {
		// given
		String productName = "Product 1";

		doReturn(true).when(productRepository).existsByName(productName);

		// when
		boolean isExist = productService.isExistProductName(productName);

		// then
		assertTrue(isExist);
	}

	@DisplayName("상품 쿼리 조회")
	@Test
	void findAllByQuery() {
		// given
		ProductFindDto.GetProductQuery productQuery = new ProductFindDto.GetProductQuery("Product 3", null, null, null);
		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.password("admin")
			.build();
		Product product = Product.builder()
			.productId(3)
			.name("Product 3")
			.description("Product 3 description")
			.member(member)
			.build();

		Specification<Product> specification = ProductSpecification.searchProductQuery(productQuery);

		// when
		doReturn(List.of(product)).when(productRepository).findAll(specification);

		// then
		assertEquals(product.getName(), productService.findAllByQuery(specification).getFirst().getName());
	}
}