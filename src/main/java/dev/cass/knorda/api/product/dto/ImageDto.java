package dev.cass.knorda.api.product.dto;

import org.springframework.core.io.Resource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageDto {
	@Getter
	@AllArgsConstructor
	public static class ImageResponse {
		private Resource imageResource;
		private String contentType;
	}
}
