package dev.cass.knorda.api.product.image;

import java.util.UUID;

public interface ImageStore {

	/**
	 * 이미지를 저장하고 저장된 이미지의 URL을 반환한다.
	 * @param imageBytes 이미지 바이트 배열
	 * @param imageName 이미지 이름
	 * @return 이미지 URL
	 */
	String storeImage(byte[] imageBytes, String imageName);

	/**
	 * 이미지를 삭제한다.
	 * @param imageUrl 이미지 URL
	 * @return 삭제 성공 여부
	 */
	boolean deleteImage(String imageUrl);

	/**
	 * 이미지를 수정한다.
	 * @param imageBytes 이미지 바이트 배열
	 * @param imageName 이미지 이름
	 * @param imageUrl 기존 이미지 URL
	 * @return 새 이미지 URL
	 */
	String updateImage(byte[] imageBytes, String imageName, String imageUrl);

	// 새로운 이미지 이름을 생성
	default String createImageName(String fileName) {
		return UUID.randomUUID() + getImageExtension(fileName);
	}

	default String getImageExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
