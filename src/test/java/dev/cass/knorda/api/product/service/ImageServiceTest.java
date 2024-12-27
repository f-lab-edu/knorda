package dev.cass.knorda.api.product.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.product.dto.ImageDto;
import dev.cass.knorda.api.product.exception.ImageNotExistException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ImageServiceTest {
	@InjectMocks
	private ImageService imageService;

	@DisplayName("이미지 Resource 조회")
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
		ImageDto.ImageResponse imageResponse = imageService.getImageResource(imageName);

		// then
		assertEquals(imageResponse.getContentType(), Files.probeContentType(newFile.toPath()));

		// delete
		assertTrue(newFile.delete());
	}

	@DisplayName("이미지 Resource 조회 - 이미지 없음")
	@Test
	void getImageNotExist() {
		// given
		String imageName = "image.png";

		// when, then
		assertThrows(ImageNotExistException.class, () -> imageService.getImageResource(imageName));
	}
}