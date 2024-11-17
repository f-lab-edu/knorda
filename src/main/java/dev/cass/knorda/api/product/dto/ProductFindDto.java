package dev.cass.knorda.api.product.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.cass.knorda.api.product.exception.InvalidQueryException;
import dev.cass.knorda.domain.product.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductFindDto {

	@Getter
	@AllArgsConstructor
	public static class GetProductResponse {
		private int productId;
		private String name;
		private String imageUrl;
		private String description;
		private String registerMemberName;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime registerDate;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		private LocalDateTime updateDate;

		public static GetProductResponse of(Product product) {
			return new GetProductResponse(product.getProductId(), product.getName(), product.getImageUrl(),
				product.getDescription(), product.getMember().getMemberName(), product.getCreatedAt(),
				product.getModifiedAt());
		}
	}

	@Getter
	public static class GetProductQuery {
		private final String productName;
		private final String memberName;
		private final LocalDateTime startDateTime;
		private final LocalDateTime endDateTime;

		@Builder
		public GetProductQuery(String productName, String memberName, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
			this.productName = productName;
			this.memberName = memberName;
			this.startDateTime = startDateTime;
			this.endDateTime = endDateTime;

			// query 값 중 적어도 한 개 이상은 null이 아니여야 함
			if (productName == null && memberName == null && startDateTime == null && endDateTime == null) {
				throw new InvalidQueryException("query 값 중 적어도 한 개 이상은 null이 아니여야 합니다.");
			}

			// startDateTime과 endDateTime은 모두 null이거나 모두 null이 아니여야 함
			if (startDateTime == null ^ endDateTime == null) {
				throw new InvalidQueryException("startDateTime과 endDateTime은 모두 null이거나 모두 null이 아니여야 합니다.");
			}

			// startDateTime이 null이 아니라면, endDateTime보다 앞이여야 함
			if (startDateTime != null && startDateTime.isAfter(endDateTime)) {
				throw new InvalidQueryException("startDateTime은 endDateTime보다 앞이여야 합니다.");
			}
		}
	}

}
