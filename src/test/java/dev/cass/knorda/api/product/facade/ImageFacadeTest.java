package dev.cass.knorda.api.product.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.product.dto.ImageDto;
import dev.cass.knorda.api.product.exception.FileDeleteFailedException;
import dev.cass.knorda.api.product.exception.ProductImageAlreadyExistException;
import dev.cass.knorda.api.product.exception.ProductImageNotExistException;
import dev.cass.knorda.api.product.exception.ProductNotOwnedByLoggedInMemberException;
import dev.cass.knorda.api.product.image.ImageStore;
import dev.cass.knorda.api.product.service.ProductService;
import dev.cass.knorda.domain.member.Member;
import dev.cass.knorda.domain.product.Product;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ImageFacadeTest {
	@InjectMocks
	private ImageFacade imageFacade;

	@Mock
	private ProductService productService;

	@Mock
	private ImageStore localImageStore;

	@DisplayName("이미지 등록")
	@Test
	void registerImage() {
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
		ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest(new byte[1], "1.png");

		String imageUrl = "http://localhost:8080/api/v1/images/1.png";

		Product productResult = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.imageUrl(imageUrl)
			.build();

		doReturn(product).when(productService).findById(productId);
		doReturn(imageUrl).when(localImageStore).storeImage(any(), any());

		doReturn(productResult).when(productService).save(any(Product.class));
		// when
		ImageDto.ImageResponse imageResponse = imageFacade.registerImage(productId, imageRequest, "admin");

		// then
		assertEquals(imageResponse.getImageUrl(), imageUrl);
	}

	@DisplayName("이미지 등록 - 자신의 상품이 아님")
	@Test
	void registerImageNotOwnedByLoggedInMember() {
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
		ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest(new byte[1], "1.png");

		doReturn(product).when(productService).findById(productId);

		// when then
		assertThrows(ProductNotOwnedByLoggedInMemberException.class,
			() -> imageFacade.registerImage(productId, imageRequest, "user"));
	}

	@DisplayName("이미지 등록 - 이미지가 존재함")
	@Test
	void registerImageImageAlreadyExist() {
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
			.imageUrl("http://localhost:8080/api/v1/images/1.png")
			.build();
		ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest(new byte[1], "1.png");

		doReturn(product).when(productService).findById(productId);

		// when then
		assertThrows(ProductImageAlreadyExistException.class,
			() -> imageFacade.registerImage(productId, imageRequest, "admin"));
	}

	@DisplayName("이미지 수정")
	@Test
	void updateImage() {
		// given
		int productId = 1;
		String imageUrl = "http://localhost:8080/api/v1/images/1.png";

		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.password("admin")
			.build();
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.imageUrl(imageUrl)
			.member(member)
			.build();
		ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest(new byte[1], "2.png");

		String newImageUrl = "http://localhost:8080/api/v1/images/2.png";

		Product productResult = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.member(member)
			.imageUrl(newImageUrl)
			.build();

		doReturn(product).when(productService).findById(productId);
		doReturn(newImageUrl).when(localImageStore).updateImage(any(), eq(imageRequest.getImageName()), eq(imageUrl));

		doReturn(productResult).when(productService).save(any(Product.class));

		// when
		ImageDto.ImageResponse imageResponse = imageFacade.updateImage(productId, imageRequest, "admin");

		// then
		assertEquals(imageResponse.getImageUrl(), newImageUrl);
	}

	@DisplayName("이미지 수정 - 자신의 상품이 아님")
	@Test
	void updateImageNotOwnedByLoggedInMember() {
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
			.imageUrl("http://localhost:8080/api/v1/images/1.png")
			.build();
		ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest(new byte[1], "2.png");

		doReturn(product).when(productService).findById(productId);

		// when then
		assertThrows(ProductNotOwnedByLoggedInMemberException.class,
			() -> imageFacade.updateImage(productId, imageRequest, "user"));
	}

	@DisplayName("이미지 수정 - 등록된 이미지가 없음")
	@Test
	void updateImageImageNotExist() {
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
		ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest(new byte[1], "2.png");

		doReturn(product).when(productService).findById(productId);

		// when then
		assertThrows(ProductImageNotExistException.class,
			() -> imageFacade.updateImage(productId, imageRequest, "admin"));
	}

	@DisplayName("이미지 삭제")
	@Test
	void deleteImage() {
		// given
		int productId = 1;
		String imageUrl = "http://localhost:8080/api/v1/images/1.png";

		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.password("admin")
			.build();
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.imageUrl(imageUrl)
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);
		doReturn(true).when(localImageStore).deleteImage(imageUrl);

		// when
		imageFacade.deleteImage(productId, "admin");

		// then
		assertNull(product.getImageUrl());
		verify(productService).save(product);
	}

	@DisplayName("이미지 삭제 - 자신의 상품이 아님")
	@Test
	void deleteImageNotOwnedByLoggedInMember() {
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
			.imageUrl("http://localhost:8080/api/v1/images/1.png")
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);

		// when then
		assertThrows(ProductNotOwnedByLoggedInMemberException.class, () -> imageFacade.deleteImage(productId, "user"));
	}

	@DisplayName("이미지 삭제 - 이미지가 존재하지 않음")
	@Test
	void deleteImageImageNotExist() {
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

		// when then
		assertThrows(ProductImageNotExistException.class, () -> imageFacade.deleteImage(productId, "admin"));
	}

	@DisplayName("이미지 삭제 - 파일 삭제 실패")
	@Test
	void deleteImageFileDeleteFailed() {
		// given
		int productId = 1;
		String imageUrl = "http://localhost:8080/api/v1/images/1.png";

		Member member = Member.builder()
			.memberId(1)
			.memberName("admin")
			.password("admin")
			.build();
		Product product = Product.builder()
			.productId(1)
			.name("Product 1")
			.description("Product 1 description")
			.imageUrl(imageUrl)
			.member(member)
			.build();

		doReturn(product).when(productService).findById(productId);
		doReturn(false).when(localImageStore).deleteImage(imageUrl);

		// when then
		assertThrows(FileDeleteFailedException.class, () -> imageFacade.deleteImage(productId, "admin"));
	}

}