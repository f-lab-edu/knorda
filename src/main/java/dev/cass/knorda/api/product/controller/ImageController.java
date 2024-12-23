package dev.cass.knorda.api.product.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dev.cass.knorda.api.product.dto.ImageDto;
import dev.cass.knorda.api.product.service.ImageService;
import dev.cass.knorda.global.controller.V1Controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@V1Controller
public class ImageController {
	private final ImageService imageService;

	/**
	 * 애플리케이션 서버에 저장된 이미지를 리턴해 주는 메소드
	 */
	@GetMapping(value = "/images/{imageName}")
	public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
		ImageDto.ImageResponse imageResponse = imageService.getImageResource(imageName);
		return ResponseEntity.status(HttpStatus.OK)
			.header(HttpHeaders.CONTENT_TYPE, imageResponse.getContentType())
			.body(imageResponse.getImageResource());
	}
}
