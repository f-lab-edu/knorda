package dev.cass.knorda.api.product.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.cass.knorda.domain.product.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductRegisterDto {
	@Getter
	@AllArgsConstructor
	public static class request {
		@NotNull
		private String productName;
		@NotNull
		private String description;

		public Product toEntity() {
			return Product.builder()
				.name(productName)
				.description(description)
				.build();
		}
	}

	@Getter
	@AllArgsConstructor
	public static class response {
		private int productId;
		private String productName;
		private String description;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime registerDate;

		public static response of(Product product) {
			return new response(
				product.getProductId(),
				product.getName(),
				product.getDescription(),
				product.getCreatedAt()
			);
		}
	}
}
