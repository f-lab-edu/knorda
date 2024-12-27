package dev.cass.knorda.api.product.image;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.cass.knorda.api.product.exception.FileExtentionInvalidException;
import dev.cass.knorda.api.product.exception.FileHeaderInvalidException;
import dev.cass.knorda.api.product.exception.FileIsNullException;
import dev.cass.knorda.api.product.exception.FileNameInvalidException;
import dev.cass.knorda.api.product.exception.FileSaveFailedException;
import dev.cass.knorda.api.product.exception.FileSizeInvalidException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LocalImageStore implements ImageStore {
	private static final String IMAGE_PATH = "./image/";
	private static final long MAX_FILE_SIZE = 10L * 1024 * 1024; // 10MB

	private static String IMAGE_URL;

	@Value("${image.url}")
	public void setIMAGE_URL(String IMAGE_URL) {
		LocalImageStore.IMAGE_URL = IMAGE_URL;
	}

	private void validateImageBytes(byte[] imageBytes) {
		if (imageBytes == null) {
			throw new FileIsNullException();
		}

		if (imageBytes.length > MAX_FILE_SIZE) {
			throw new FileSizeInvalidException();
		}

		// byte check
		if (!ImageCheckUtils.isImageHeaderMatch(imageBytes)) {
			throw new FileHeaderInvalidException();
		}
	}

	private void validateImageName(String imageName) {
		if (imageName == null || imageName.isEmpty()) {
			throw new FileNameInvalidException();
		}

		if (!ImageCheckUtils.isImageExtensionMatch(imageName)) {
			throw new FileExtentionInvalidException();
		}
	}

	// 이미지를 저장하고 저장된 이미지의 URL을 반환한다.
	@Override
	public String storeImage(byte[] imageBytes, String imageName) {

		validateImageBytes(imageBytes);

		validateImageName(imageName);

		String newImageName = createImageName(imageName);
		File newDir = new File(IMAGE_PATH);

		if (!newDir.exists()) {
			newDir.mkdir();
		}

		File file = new File(newDir, newImageName);
		log.info(file.getAbsolutePath());

		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(imageBytes);

			log.info(newImageName);
			return IMAGE_URL + newImageName;
		} catch (Exception e) {
			// 이미지 저장에 실패하면 예외를 던진다.
			log.error("이미지 저장에 실패했습니다. 이미지 이름: {}", newImageName, e);
			throw new FileSaveFailedException();
		}
	}

	@Override
	public boolean deleteImage(String imageUrl) {
		// 이미지를 삭제한다.
		String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		File file = new File(IMAGE_PATH + imageName);
		try {
			if (file.exists()) {
				Files.delete(file.toPath());
			}
		} catch (Exception e) {
			log.error("이미지 삭제에 실패했습니다. 이미지 이름: {}", imageName, e);
			return false;
		}

		return true;
	}
}
