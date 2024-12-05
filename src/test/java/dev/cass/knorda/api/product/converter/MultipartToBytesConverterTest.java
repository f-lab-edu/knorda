package dev.cass.knorda.api.product.converter;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@ActiveProfiles("test")
@SpringBootTest
class MultipartToBytesConverterTest {

	@DisplayName("MultipartFile을 byte[]로 변환한다.")
	@Test
	void convert() {
		// given
		MultipartFile multipartFile = new MockMultipartFile("image", "image.png", "image/png", new byte[1]);
		MultipartToBytesConverter converter = new MultipartToBytesConverter();

		// when
		byte[] bytes = converter.convert(multipartFile);

		// then
		assertThat(bytes).isEqualTo(new byte[1]);
	}
}