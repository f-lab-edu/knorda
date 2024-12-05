package dev.cass.knorda.api.product.dto;

import dev.cass.knorda.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ImageDto {
	@Getter
	@AllArgsConstructor
	public static class ImageResponse {
		private int productId;
		private String imageUrl;

		public static ImageResponse of(Product product) {
			return new ImageResponse(
				product.getProductId(),
				product.getImageUrl()
			);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class ImageRequest {
		private byte[] image;
		private String imageName;
	}
}
