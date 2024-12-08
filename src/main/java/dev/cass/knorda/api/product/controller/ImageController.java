package dev.cass.knorda.api.product.controller;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.cass.knorda.api.product.converter.MultipartToBytesConverter;
import dev.cass.knorda.api.product.dto.ImageDto;
import dev.cass.knorda.api.product.facade.ImageFacade;
import dev.cass.knorda.global.controller.V1Controller;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@V1Controller
public class ImageController {
	private final ImageFacade imageFacade;
	private final MultipartToBytesConverter multipartToBytesConverter;

	/**
	 * 애플리케이션 서버에 저장된 이미지를 리턴해 주는 메소드
	 */
	@GetMapping(value = "/images/{imageName}",
		produces = {"image/jpg", "image/jpeg", "image/png", "image/gif"})
	public UrlResource getImage(@PathVariable String imageName) throws MalformedURLException {
		String encodedImageName = URLEncoder.encode(imageName, StandardCharsets.UTF_8);
		String imageDir = "./image/";
		return new UrlResource("file:" + imageDir + encodedImageName);
	}

	@PostMapping("/products/{productId}/images")
	public ResponseEntity<ImageDto.ImageResponse> registerImage(@PathVariable int productId, MultipartFile image,
		HttpSession session) {
		String loggedInMember = SessionManageUtils.getMemberName(session);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(imageFacade.registerImage(productId,
				new ImageDto.ImageRequest(multipartToBytesConverter.convert(image), image.getOriginalFilename()),
				loggedInMember));
	}

	@PutMapping("/products/{productId}/images")
	public ResponseEntity<ImageDto.ImageResponse> updateImage(@PathVariable int productId, MultipartFile image,
		HttpSession session) {
		String loggedInMember = SessionManageUtils.getMemberName(session);
		return ResponseEntity.status(HttpStatus.OK)
			.body(imageFacade.updateImage(productId,
				new ImageDto.ImageRequest(multipartToBytesConverter.convert(image), image.getOriginalFilename()),
				loggedInMember));
	}

	@DeleteMapping("/products/{productId}/images")
	public ResponseEntity<Void> deleteImage(@PathVariable int productId, HttpSession session) {
		String loggedInMember = SessionManageUtils.getMemberName(session);
		imageFacade.deleteImage(productId, loggedInMember);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
