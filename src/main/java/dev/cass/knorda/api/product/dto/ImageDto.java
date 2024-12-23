package dev.cass.knorda.api.product.dto;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ImageDto {
	@Getter
	@AllArgsConstructor
	public static class ImageResponse {
		private Resource imageResource;
		private String contentType;
	}
}
