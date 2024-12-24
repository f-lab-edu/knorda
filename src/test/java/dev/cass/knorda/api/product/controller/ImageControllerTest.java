package dev.cass.knorda.api.product.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.UrlResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import dev.cass.knorda.api.product.dto.ImageDto;
import dev.cass.knorda.api.product.exception.ImageNotExistException;
import dev.cass.knorda.api.product.service.ImageService;
import dev.cass.knorda.global.exception1.GlobalExceptionHandler;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ImageControllerTest {
	@InjectMocks
	private ImageController imageController;

	@Mock
	private ImageService imageService;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(imageController)
			.setControllerAdvice(GlobalExceptionHandler.class)
			.build();
	}

	@DisplayName("이미지 조회")
	@Test
	void getImage() throws Exception {
		String imageName = "image.png";
		URL fileDir = getClass().getClassLoader().getResource(imageName);
		assertNotNull(fileDir);

		UrlResource urlResource = new UrlResource("file:" + fileDir.getFile());

		ImageDto.ImageResponse imageResponse = new ImageDto.ImageResponse(urlResource,
			Files.probeContentType(urlResource.getFile().toPath()));

		doReturn(imageResponse).when(imageService).getImageResource(imageName);

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/images/{imageName}", imageName));

		// then
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(imageResponse.getContentType()));
	}

	@DisplayName("이미지 조회 이미지 없음")
	@Test
	void getImage_NotFound() throws Exception {
		String imageName = "image.png";

		doThrow(new ImageNotExistException()).when(imageService).getImageResource(imageName);

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/images/{imageName}", imageName));

		// then
		resultActions.andExpect(status().isNotFound());
	}
}