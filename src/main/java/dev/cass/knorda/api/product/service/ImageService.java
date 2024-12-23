package dev.cass.knorda.api.product.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import dev.cass.knorda.api.product.dto.ImageDto;
import dev.cass.knorda.api.product.exception.ImageNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
	private final static String IMAGE_DIR = "./image/";

	public ImageDto.ImageResponse getImageResource(String imageName) {
		String encodedImageName = URLEncoder.encode(imageName, StandardCharsets.UTF_8);
		try {
			UrlResource urlResource = new UrlResource("file:" + IMAGE_DIR + encodedImageName);
			String contentType = Files.probeContentType(urlResource.getFile().toPath());

			if (!urlResource.exists()) {
				throw new ImageNotExistException();
			}

			return new ImageDto.ImageResponse(urlResource, contentType);

		} catch (Exception e) {
			log.error("이미지를 찾을 수 없습니다. 이미지 이름: {}", imageName, e);
			throw new ImageNotExistException();
		}
	}
}
