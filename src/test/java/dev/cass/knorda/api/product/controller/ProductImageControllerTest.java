package dev.cass.knorda.api.product.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.api.product.dto.ProductImageDto;
import dev.cass.knorda.api.product.facade.ProductImageFacade;
import dev.cass.knorda.global.exception1.GlobalExceptionHandler;
import dev.cass.knorda.global.util.SessionManageUtils;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProductImageControllerTest {
	@InjectMocks
	private ProductImageController productImageController;

	@Mock
	private ConversionService conversionService;

	@Mock
	private ProductImageFacade productImageFacade;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(productImageController)
			.setControllerAdvice(GlobalExceptionHandler.class)
			.build();
	}

	@DisplayName("이미지 등록")
	@Test
	void registerImage() throws Exception {
		// given
		int productId = 1;
		String imageName = "1.png";
		String name = "admin";
		int userId = 1;
		byte[] bytes = new byte[1];

		MockMultipartFile image = new MockMultipartFile("image", imageName, "image/png", bytes);

		ProductImageDto.ImageResponse imageResponse = new ProductImageDto.ImageResponse(1,
			"http://localhost:8080/api/v1/images/1.png");

		doReturn(imageResponse).when(productImageFacade)
			.registerImage(eq(productId), any(ProductImageDto.ImageRequest.class), eq(name));

		doReturn(bytes).when(conversionService).convert(image, byte[].class);

		// when
		ResultActions resultActions = mockMvc.perform(
			multipart(HttpMethod.POST, "/api/v1/products/{productId}/images", productId)
				.file(image)
				.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, userId)));

		// then
		resultActions.andExpect(status().isCreated());
	}

	@DisplayName("이미지 수정")
	@Test
	void updateImage() throws Exception {
		// given
		int productId = 1;
		String imageName = "1.png";
		String name = "admin";
		int userId = 1;
		byte[] bytes = new byte[1];

		MockMultipartFile image = new MockMultipartFile("image", imageName, "image/png", bytes);

		ProductImageDto.ImageResponse imageResponse = new ProductImageDto.ImageResponse(1,
			"http://localhost:8080/api/v1/images/1.png");

		doReturn(imageResponse).when(productImageFacade)
			.updateImage(eq(productId), any(ProductImageDto.ImageRequest.class), eq(name));

		doReturn(bytes).when(conversionService).convert(image, byte[].class);

		// when
		ResultActions resultActions = mockMvc.perform(
			multipart(HttpMethod.PUT, "/api/v1/products/{productId}/images", productId)
				.file(image)
				.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, userId)));

		// then
		resultActions.andExpect(status().isOk());
	}

	@DisplayName("이미지 삭제")
	@Test
	void deleteImage() throws Exception {
		// given
		int productId = 1;
		String name = "admin";
		int userId = 1;

		// when
		ResultActions resultActions = mockMvc.perform(
			delete("/api/v1/products/{productId}/images", productId)
				.sessionAttr(SessionManageUtils.SESSION_MEMBER, new AuthDto.SessionDto(name, userId)));

		// then
		resultActions.andExpect(status().isNoContent());
	}
}