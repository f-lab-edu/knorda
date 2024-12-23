package dev.cass.knorda.api.product.converter;

import java.io.IOException;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

import dev.cass.knorda.api.product.exception.FileSaveFailedException;

public class MultipartToBytesConverter implements Converter<MultipartFile, byte[]> {
	@Override
	public byte[] convert(MultipartFile source) {
		try {
			return source.getBytes();
		} catch (IOException e) {
			throw new FileSaveFailedException();
		}
	}
}
