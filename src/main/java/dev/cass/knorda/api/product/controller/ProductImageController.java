package dev.cass.knorda.api.product.controller;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.cass.knorda.api.product.dto.ProductImageDto;
import dev.cass.knorda.api.product.facade.ProductImageFacade;
import dev.cass.knorda.global.controller.V1Controller;
import dev.cass.knorda.global.util.SessionManageUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@V1Controller
public class ProductImageController {
	private final ProductImageFacade productImageFacade;
	private final ConversionService conversionService;

	@PostMapping("/products/{productId}/images")
	public ResponseEntity<ProductImageDto.ImageResponse> registerImage(@PathVariable int productId, MultipartFile image,
		HttpSession session) {
		String loggedInMember = SessionManageUtils.getMemberName(session);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(productImageFacade.registerImage(productId,
				new ProductImageDto.ImageRequest(conversionService.convert(image, byte[].class),
					image.getOriginalFilename()),
				loggedInMember));
	}

	@DeleteMapping("/products/{productId}/images")
	public ResponseEntity<Void> deleteImage(@PathVariable int productId, HttpSession session) {
		String loggedInMember = SessionManageUtils.getMemberName(session);
		productImageFacade.deleteImage(productId, loggedInMember);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
