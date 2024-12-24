package dev.cass.knorda.api.product.image;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class ImageCheckUtils {
	private static final List<Byte[]> IMAGE_HEADERS = List.of(
		new Byte[] {(byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xE0}, // JPEG
		new Byte[] {(byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xE1}, // JPEG
		new Byte[] {(byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xE8}, // JPEG
		new Byte[] {(byte)0x89, (byte)0x50, (byte)0x4E, (byte)0x47, (byte)0x0D, (byte)0x0A, (byte)0x1A, (byte)0x0A},
		// PNG
		new Byte[] {(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x37, (byte)0x61}, // GIF
		new Byte[] {(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x39, (byte)0x61} // GIF
	);

	private static final byte[] WEBP_HEADER = new byte[] {
		0x52, 0x49, 0x46, 0x46,
		0x00, 0x00, 0x00, 0x00,
		0x57, 0x45, 0x42, 0x50
	};

	private static final List<String> IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "webp");

	private static boolean isHeaderMatch(Byte[] header, byte[] target) {
		int headerLength = header.length;
		int targetLength = target.length;

		if (headerLength > targetLength) {
			return false;
		}

		for (int i = 0; i < headerLength; i++) {
			if (header[i] != target[i]) {
				return false;
			}
		}

		return true;
	}

	// webp 헤더는 일부 바이트를 처리하지 않으므로 따로 확인
	private static boolean isWebpHeaderMatch(byte[] target) {
		int targetLength = target.length;

		if (12 > targetLength) {
			return false;
		}

		for (int i = 0; i < 4; i++) {
			if (WEBP_HEADER[i] != target[i]) {
				return false;
			}
		}

		for (int i = 8; i < 12; i++) {
			if (WEBP_HEADER[i] != target[i]) {
				return false;
			}
		}

		return true;
	}

	public static boolean isImageHeaderMatch(byte[] imageBytes) {
		// 파일이 존재하지 않으면 이미지가 아님
		if (imageBytes == null) {
			return false;
		}

		// 이미지 헤더별로 확인
		for (Byte[] header : IMAGE_HEADERS) {
			if (isHeaderMatch(header, imageBytes)) {
				return true;
			}
		}

		// webp 확인
		return isWebpHeaderMatch(imageBytes);
	}

	public static boolean isImageExtensionMatch(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		return IMAGE_EXTENSIONS.contains(extension);
	}
}
