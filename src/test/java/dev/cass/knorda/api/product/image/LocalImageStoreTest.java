package dev.cass.knorda.api.product.image;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import dev.cass.knorda.api.product.exception.FileExtentionInvalidException;
import dev.cass.knorda.api.product.exception.FileHeaderInvalidException;
import dev.cass.knorda.api.product.exception.FileIsNullException;
import dev.cass.knorda.api.product.exception.FileNameInvalidException;
import dev.cass.knorda.api.product.exception.FileSizeInvalidException;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocalImageStoreTest {

	private final byte[] imageBytesPngSample = new byte[] {(byte)0x89, (byte)0x50, (byte)0x4E, (byte)0x47, (byte)0x0D,
		(byte)0x0A, (byte)0x1A,
		(byte)0x0A, (byte)0x00};

	@Autowired
	private LocalImageStore localImageStore;

	// 헤더 테스트를 위한 Argument Stream Method
	private static Stream<Arguments> provideExtension() {
		return Stream.of(
			arguments("test.png", new byte[] {(byte)0x89, (byte)0x50, (byte)0x4E, (byte)0x47, (byte)0x0D,
				(byte)0x0A, (byte)0x1A,
				(byte)0x0A, (byte)0x00}),
			arguments("test.webp", new byte[] {0x52, 0x49, 0x46, 0x46,
				0x00, 0x00, 0x00, 0x00,
				0x57, 0x45, 0x42, 0x50,
				0x00, 0x00, 0x00, 0x00})
		);
	}

	@DisplayName("Local 이미지 저장, 삭제 테스트")
	@Test
	void saveImage() throws IOException {
		// given

		// resource directory 의 test 이미지 읽어오기
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

	/**
	 * ParameterizedTest - 테스트를 여러 argument를 사용해서 수행할 수 있게 해 주는 테스트 어노테이션
	 * Test 어노테이션과 동일하게 사용하면 디ㅗ고, 최소 하나 이상의 argument를 제공해야 정상적으로 작동함
	 * argument는 stream이나 array로 제공하면 되고 Enum의 정규식이나 Stream을 리턴하는 메소드, arrau field 등을 사용할 수 있음
	 * 참고 - https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-consuming-arguments
	 * MethodSource - 위에서 언급한 argument를 명시한 method를 통해 제공하겠음을 선언하는 어노테이션
	 */
	@DisplayName("Local 이미지 저장 여러 확장자")
	@ParameterizedTest
	@MethodSource("provideExtension")
	void saveImageExtension(String fileName, byte[] image) throws IOException {
		// given
		// when
		String savedFileUrl = localImageStore.storeImage(image, fileName);
		String savedFileName = savedFileUrl.substring(savedFileUrl.lastIndexOf("/") + 1);

		// then
		File resultFile = new File("./image/" + savedFileName);
		assertTrue(resultFile.exists());
		assertArrayEquals(image, Files.readAllBytes(resultFile.toPath()));

		// delete
		assertTrue(resultFile.delete());
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

	@DisplayName("Local 이미지 저장 byte[] 크기 초과")
	@Test
	void saveImageSizeOver() {
		// given
		String fileName = "test.png";
		byte[] image = new byte[11 * 1024 * 1024];

		// when
		// then
		assertThrows(FileSizeInvalidException.class, () -> localImageStore.storeImage(image, fileName));
	}

	@DisplayName("Local 이미지 저장 유효하지 않은 헤더")
	@Test
	void saveImageInvalidHeader() {
		// given
		String fileName = "test.png";
		byte[] image = new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};

		// when
		// then
		assertThrows(FileHeaderInvalidException.class, () -> localImageStore.storeImage(image, fileName));
	}

	@DisplayName("Local 이미지 저장 파일명 null")
	@Test
	void saveImageNullFileName() {
		// given
		byte[] image = imageBytesPngSample.clone();

		// when
		// then
		assertThrows(FileNameInvalidException.class, () -> localImageStore.storeImage(image, null));
	}

	@DisplayName("Local 이미지 저장 유효하지 않은 확장자")
	@Test
	void saveImageInvalidExtension() {
		// given
		String fileName = "test.txt";
		byte[] image = imageBytesPngSample.clone();

		// when
		// then
		assertThrows(FileExtentionInvalidException.class, () -> localImageStore.storeImage(image, fileName));
	}

	@DisplayName("Local 이미지 수정")
	@Test
	void updateImage() throws IOException {
		// given
		String fileName = "test.png";
		byte[] image = imageBytesPngSample.clone();

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