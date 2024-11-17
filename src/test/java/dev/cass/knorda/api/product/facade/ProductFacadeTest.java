package dev.cass.knorda.api.product.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.member.service.MemberService;
import dev.cass.knorda.api.product.dto.ProductFindDto;
import dev.cass.knorda.api.product.dto.ProductRegisterDto;
import dev.cass.knorda.api.product.exception.AlreadyExistProductNameException;
import dev.cass.knorda.api.product.exception.NotYourProductException;
import dev.cass.knorda.api.product.service.ProductService;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.product.Product;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductFacadeTest {
	@InjectMocks
	private ProductFacade productFacade;

	@Mock
	private ProductService productService;

	@Mock
	private MemberService memberService;

	@DisplayName("상품번호로 상품 조회")
	@Test
	void findById() {
		// given
		int productId = 1;
		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.password("admin")
			.build();
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);

		// when
		ProductFindDto.GetProductResponse productResponse = productFacade.getProductById(productId);

		// then
		assertEquals(productResponse.getName(), product.getName());
		assertEquals(productResponse.getDescription(), product.getDescription());
		assertEquals(productResponse.getRegisterMemberName(), product.getMember().getMemberName());
	}

	@DisplayName("상품 등록 - 이름 중복")
	@Test
	void registerProductDuplicateName() {
		// given
		String memberName = "admin";
		ProductRegisterDto.request registerRequest = new ProductRegisterDto.request("Product 1",
			"Product 1 description");

		doReturn(true).when(productService).isExistProductName(registerRequest.getProductName());

		// when
		assertThrows(
			AlreadyExistProductNameException.class, () -> productFacade.registerProduct(registerRequest, memberName));
	}

	@DisplayName("상품 등록")
	@Test
	void registerProduct() {
		// given
		String memberName = "admin";
		Member member = Member.builder()
			.memberId(1)
			.memberName(memberName)
			.password("admin")
			.build();
		ProductRegisterDto.request registerRequest = new ProductRegisterDto.request("Product 1",
			"Product 1 description");
		Product product = Product.builder()
			.productId(1)
			.name(registerRequest.getProductName())
			.description(registerRequest.getDescription())
			.build();

		doReturn(member).when(memberService).findMemberByMemberName(memberName);
		doReturn(false).when(productService).isExistProductName(registerRequest.getProductName());
		doReturn(product).when(productService).save(registerRequest, member);

		// when
		ProductRegisterDto.response productResponse = productFacade.registerProduct(registerRequest, memberName);

		// then
		assertEquals(productResponse.getProductName(), product.getName());
		assertEquals(productResponse.getDescription(), product.getDescription());
	}

	@DisplayName("상품 조회 - 쿼리")
	@Test
	void getProductsByQuery() {
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

		doReturn(List.of(product)).when(productService).findAllByQuery(productQuery);

		// when
		List<ProductFindDto.GetProductResponse> productResponse = productFacade.getProductListByQuery(productQuery);

		// then
		assertEquals(1, productResponse.size());
		assertEquals(product.getName(), productResponse.getFirst().getName());
		assertEquals(product.getDescription(), productResponse.getFirst().getDescription());
	}

	@DisplayName("상품 삭제")
	@Test
	void deleteProduct() {
		// given
		int productId = 1;
		String memberName = "admin";
		Member member = Member.builder()
			.memberId(1)
			.memberName(memberName)
			.password("admin")
			.build();
		Product product = Product.builder()
			.productId(productId)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);
		doNothing().when(productService).delete(product);

		// when
		productFacade.deleteProduct(productId, memberName);

		// then
		verify(productService).delete(product);
	}

	@DisplayName("상품 삭제 - 다른 사용자")
	@Test
	void deleteProductDifferentMember() {
		// given
		int productId = 1;
		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.password("admin")
			.build();
		Product product = Product.builder()
			.productId(productId)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);

		// when
		assertThrows(NotYourProductException.class, () -> productFacade.deleteProduct(productId, "user"));

		// then
		verify(productService, never()).delete(product);
	}

	@DisplayName("상품 수정")
	@Test
	void updateProduct() {
		// given
		int productId = 1;
		String memberName = "admin";
		Member member = Member.builder()
			.memberId(1)
			.memberName(memberName)
			.password("admin")
			.build();
		ProductRegisterDto.request updateRequest = new ProductRegisterDto.request("Product 1 new",
			"Product 1 description new");
		Product product = Product.builder()
			.productId(productId)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);

		product.update(updateRequest.getProductName(), updateRequest.getDescription());

		doReturn(product).when(productService).update(product, updateRequest);

		// when
		ProductRegisterDto.response productResponse = productFacade.updateProduct(productId, updateRequest, memberName);

		// then
		assertEquals(productResponse.getProductName(), product.getName());
		assertEquals(productResponse.getDescription(), product.getDescription());
	}

	@DisplayName("상품 수정 - 다른 사용자")
	@Test
	void updateProductDifferentMember() {
		// given
		int productId = 1;
		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.password("admin")
			.build();
		ProductRegisterDto.request updateRequest = new ProductRegisterDto.request("Product 1 new",
			"Product 1 description new");
		Product product = Product.builder()
			.productId(productId)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);

		// when
		assertThrows(NotYourProductException.class,
			() -> productFacade.updateProduct(productId, updateRequest, "user"));

		// then
		verify(productService, never()).update(product, updateRequest);
	}

	@DisplayName("상품 수정 - 이름 중복")
	@Test
	void updateProductDuplicateName() {
		// given
		int productId = 1;
		String memberName = "admin";
		Member member = Member.builder()
			.memberId(1)
			.memberName(memberName)
			.password("admin")
			.build();
		ProductRegisterDto.request updateRequest = new ProductRegisterDto.request("Product 1 new",
			"Product 1 description new");
		Product product = Product.builder()
			.productId(productId)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);
		doReturn(true).when(productService).isExistProductName(updateRequest.getProductName());

		// when
		assertThrows(AlreadyExistProductNameException.class,
			() -> productFacade.updateProduct(productId, updateRequest, memberName));

		// then
		verify(productService, never()).update(product, updateRequest);
	}

	@DisplayName("상품 수정 - 현재 상품명과 동일")
	@Test
	void updateProductSameName() {
		// given
		int productId = 1;
		String memberName = "admin";
		Member member = Member.builder()
			.memberId(1)
			.memberName(memberName)
			.password("admin")
			.build();
		ProductRegisterDto.request updateRequest = new ProductRegisterDto.request("Product 1",
			"Product 1 description");
		Product product = Product.builder()
			.productId(productId)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);

		product.update(updateRequest.getProductName(), updateRequest.getDescription());

		doReturn(product).when(productService).update(product, updateRequest);

		// when
		ProductRegisterDto.response productResponse = productFacade.updateProduct(productId, updateRequest, memberName);

		// then
		assertEquals(productResponse.getProductName(), product.getName());
		assertEquals(productResponse.getDescription(), product.getDescription());
	}
}