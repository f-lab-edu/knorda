package dev.cass.knorda.api.product.image;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.cass.knorda.api.product.exception.FileIsNullException;
import dev.cass.knorda.api.product.exception.FileSaveFailedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LocalImageStore implements ImageStore {
	private final String IMAGE_PATH = "./image/";

	@Value("${image.url}")
	private String IMAGE_URL;

	@Override
	public String storeImage(byte[] imageBytes, String imageName) {
		// 이미지를 저장하고 저장된 이미지의 URL을 반환한다.
		if (imageBytes == null) {
			throw new FileIsNullException();
		}

		try {
			// 이미지를 저장한다.
			String newImageName = createImageName(imageName);
			File newDir = new File(IMAGE_PATH);

			if (!newDir.exists()) {
				newDir.mkdir();
			}

			File file = new File(newDir, newImageName);
			log.info(file.getAbsolutePath());

			FileOutputStream fos = new FileOutputStream(file);
			fos.write(imageBytes);
			fos.close();

			log.info(newImageName);
			return IMAGE_URL + newImageName;
		} catch (Exception e) {
			// 이미지 저장에 실패하면 예외를 던진다.
			log.error(e.getMessage());
			throw new FileSaveFailedException();
		}
	}

	@Override
	public boolean deleteImage(String imageUrl) {
		// 이미지를 삭제한다.
		String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		File file = new File(IMAGE_PATH + imageName);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	@Override
	public String updateImage(byte[] imageBytes, String imageName, String imageUrl) {
		// 이미지를 수정한다.
		deleteImage(imageUrl);
		return storeImage(imageBytes, imageName);
	}
}
