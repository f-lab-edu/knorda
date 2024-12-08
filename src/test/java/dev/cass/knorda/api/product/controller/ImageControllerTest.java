package dev.cass.knorda.api.product.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import dev.cass.knorda.api.member.dto.AuthDto;
import dev.cass.knorda.api.product.converter.MultipartToBytesConverter;
import dev.cass.knorda.api.product.dto.ImageDto;
import dev.cass.knorda.api.product.facade.ImageFacade;
import dev.cass.knorda.global.exception1.GlobalExceptionHandler;
import dev.cass.knorda.global.util.SessionManageUtils;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {
	@InjectMocks
	private ImageController imageController;

	@Mock
	private ImageFacade imageFacade;

	private MockMvc mockMvc;

	@Mock
	private MultipartToBytesConverter multipartToBytesConverter;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(imageController)
			.setControllerAdvice(GlobalExceptionHandler.class)
			.build();
	}

	@DisplayName("이미지 조회")
	@Test
	void getImage() throws Exception {
		// given
		String imageName = "image.png";
		String imageDir = "./image/";
		URL fileDir = getClass().getClassLoader().getResource(imageName);
		assertNotNull(fileDir);

		File file = new File(fileDir.getFile());
		File newDir = new File(imageDir);
		if (!newDir.exists()) {
			assertTrue(newDir.mkdir());
		}
		File newFile = new File(imageDir, imageName);
		FileOutputStream fos = new FileOutputStream(newFile);
		fos.write(Files.readAllBytes(file.toPath()));
		fos.close();

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/images/{imageName}", imageName));

		// then
		resultActions.andExpect(status().isOk());

		// delete
		assertTrue(newFile.delete());
	}

	@DisplayName("이미지 조회 - 이미지가 없음")
	@Test
	void getImage_NotFound() throws Exception {
		// given
		String imageName = "image.png";

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/images/{imageName}", imageName));

		// then
		resultActions.andExpect(status().isNotFound());
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

		ImageDto.ImageResponse imageResponse = new ImageDto.ImageResponse(1,
			"http://localhost:8080/api/v1/images/1.png");

		doReturn(imageResponse).when(imageFacade)
			.registerImage(eq(productId), any(ImageDto.ImageRequest.class), eq(name));

		doReturn(bytes).when(multipartToBytesConverter).convert(image);

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

		ImageDto.ImageResponse imageResponse = new ImageDto.ImageResponse(1,
			"http://localhost:8080/api/v1/images/1.png");

		doReturn(imageResponse).when(imageFacade)
			.updateImage(eq(productId), any(ImageDto.ImageRequest.class), eq(name));

		doReturn(bytes).when(multipartToBytesConverter).convert(image);

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