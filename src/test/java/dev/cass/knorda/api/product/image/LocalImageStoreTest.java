package dev.cass.knorda.api.product.image;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.product.exception.FileIsNullException;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocalImageStoreTest {

	@Autowired
	private LocalImageStore localImageStore;

	@DisplayName("Local 이미지 저장, 삭제 테스트")
	@Test
	void saveImage() throws IOException {
		// given

		// resource directory의 test 이미지 읽어오기
		URL fileDir = getClass().getClassLoader().getResource("image.png");
		assertNotNull(fileDir);

		File file = new File(fileDir.getFile());
		String fileName = file.getName();
		byte[] image = Files.readAllBytes(file.toPath());

		// when
		String savedFileUrl = localImageStore.storeImage(image, fileName);
		String savedFileName = savedFileUrl.substring(savedFileUrl.lastIndexOf("/") + 1);

		// then
		File resultFile = new File("./image/" + savedFileName);
		assertTrue(resultFile.exists());
		assertArrayEquals(image, Files.readAllBytes(resultFile.toPath()));

		// delete

		// when
		boolean isDeleted = localImageStore.deleteImage(savedFileUrl);
		assertFalse(resultFile.exists());
		assertTrue(isDeleted);
	}

	@DisplayName("Local 이미지 저장 byte[] null")
	@Test
	void saveImageNull() {
		// given
		String fileName = "test.png";

		// when
		// then
		assertThrows(FileIsNullException.class, () -> localImageStore.storeImage(null, fileName));
	}

	@DisplayName("Local 이미지 수정")
	@Test
	void updateImage() throws IOException {
		// given
		String fileName = "test.png";
		byte[] image = new byte[1];

		String savedFileUrl = localImageStore.storeImage(image, fileName);

		// resource directory의 test2 이미지 읽어오기
		URL fileDir2 = getClass().getClassLoader().getResource("image.png");
		assertNotNull(fileDir2);

		File file2 = new File(fileDir2.getFile());
		String fileName2 = file2.getName();
		byte[] image2 = Files.readAllBytes(file2.toPath());

		// when
		String updatedFileUrl = localImageStore.updateImage(image2, fileName2, savedFileUrl);
		String updatedFileName = updatedFileUrl.substring(updatedFileUrl.lastIndexOf("/") + 1);

		// then
		File resultFile = new File("./image/" + updatedFileName);
		assertTrue(resultFile.exists());
		assertArrayEquals(image2, Files.readAllBytes(resultFile.toPath()));

		// delete
		assertTrue(resultFile.delete());
	}

}