package dev.cass.knorda.api.product.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.cass.knorda.api.product.dto.ProductImageDto;
import dev.cass.knorda.api.product.exception.FileDeleteFailedException;
import dev.cass.knorda.api.product.exception.ProductImageAlreadyExistException;
import dev.cass.knorda.api.product.exception.ProductImageNotExistException;
import dev.cass.knorda.api.product.exception.ProductNotOwnedByLoggedInMemberException;
import dev.cass.knorda.api.product.image.ImageStore;
import dev.cass.knorda.api.product.service.ProductService;
import dev.cass.knorda.domain.product.Product;
import dev.cass.knorda.global.util.NamedLockManager;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductImageFacade {
	private static final String LOCK_NAME = "product_image_";
	private final ProductService productService;
	private final ImageStore localImageStore;
	private final NamedLockManager namedLockManager;

	@Transactional
	public ProductImageDto.ImageResponse registerImage(int productId, ProductImageDto.ImageRequest imageRequest,
		String loggedInMember) {
		return namedLockManager.executeWithNamedLock(LOCK_NAME + productId, 20,
			() -> registerImageInternal(productId, imageRequest, loggedInMember));
	}

	public ProductImageDto.ImageResponse registerImageInternal(int productId,
		ProductImageDto.ImageRequest imageRequest,
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
		return ProductImageDto.ImageResponse.of(productService.save(product));
	}

	@Transactional
	public void deleteImage(int productId, String loggedInMember) {
		namedLockManager.executeWithNamedLock(LOCK_NAME + productId, 20, () -> {
			deleteImageInternal(productId, loggedInMember);
			return null;
		});
	}

	private void deleteImageInternal(int productId, String loggedInMember) {
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
