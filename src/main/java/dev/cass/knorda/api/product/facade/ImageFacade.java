package dev.cass.knorda.api.product.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.product.dto.ImageDto;
import dev.cass.knorda.api.product.exception.FileDeleteFailedException;
import dev.cass.knorda.api.product.exception.ProductImageAlreadyExistException;
import dev.cass.knorda.api.product.exception.ProductImageNotExistException;
import dev.cass.knorda.api.product.exception.ProductNotOwnedByLoggedInMemberException;
import dev.cass.knorda.api.product.image.ImageStore;
import dev.cass.knorda.api.product.service.ProductService;
import dev.cass.knorda.domain.product.Product;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageFacade {
	private final ProductService productService;
	private final ImageStore localImageStore;

	@Transactional
	public ImageDto.ImageResponse registerImage(int productId, ImageDto.ImageRequest imageRequest,
		String loggedInMember) {
		Product product = productService.findById(productId);
		if (!product.getMember().getMemberName().equals(loggedInMember)) {
			throw new ProductNotOwnedByLoggedInMemberException();
		}
		if (product.getImageUrl() != null) {
			throw new ProductImageAlreadyExistException();
		}

		String imageUrl = localImageStore.storeImage(imageRequest.getImage(), imageRequest.getImageName());

		product.updateImage(imageUrl);
		return ImageDto.ImageResponse.of(productService.save(product));
	}

	@Transactional
	public ImageDto.ImageResponse updateImage(int productId, ImageDto.ImageRequest imageRequest,
		String loggedInMember) {
		Product product = productService.findById(productId);
		if (!product.getMember().getMemberName().equals(loggedInMember)) {
			throw new ProductNotOwnedByLoggedInMemberException();
		}
		if (product.getImageUrl() == null) {
			throw new ProductImageNotExistException();
		}

		String imageUrl = localImageStore.updateImage(imageRequest.getImage(), imageRequest.getImageName(),
			product.getImageUrl());

		product.updateImage(imageUrl);
		return ImageDto.ImageResponse.of(productService.save(product));
	}

	@Transactional
	public void deleteImage(int productId, String loggedInMember) {
		Product product = productService.findById(productId);
		if (!product.getMember().getMemberName().equals(loggedInMember)) {
			throw new ProductNotOwnedByLoggedInMemberException();
		}
		if (product.getImageUrl() == null) {
			throw new ProductImageNotExistException();
		}

		if (!localImageStore.deleteImage(product.getImageUrl())) {
			throw new FileDeleteFailedException();
		}
		product.updateImage(null);
		productService.save(product);
	}
}
