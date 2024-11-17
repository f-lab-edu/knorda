package dev.cass.knorda.api.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.product.dto.ProductRegisterDto;
import dev.cass.knorda.api.product.exception.ProductNotExistException;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.product.Product;
import dev.cass.knorda.domain.product.ProductRepository;

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
		ProductRegisterDto.request productQuery = new ProductRegisterDto.request("Product 1", "Product 1 description");
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

		doReturn(product).when(productRepository).save(any(Product.class));

		// when
		Product product1 = productService.save(productQuery, member);

		// then
		assertEquals(product1.getName(), product.getName());
	}

	@DisplayName("상품 수정")
	@Test
	void update() {
		// given
		ProductRegisterDto.request productQuery = new ProductRegisterDto.request("Product 1 new",
			"Product 1 description new");
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.build();

		product.update(productQuery.getProductName(), productQuery.getDescription());

		doReturn(product).when(productRepository).save(product);

		// when
		Product product1 = productService.update(product, productQuery);

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
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.build();

		doReturn(Optional.of(product)).when(productRepository).findFirstByNameAndIsDeletedFalse(productName);

		// when
		boolean isExist = productService.isExistProductName(productName);

		// then
		assertTrue(isExist);
	}
}