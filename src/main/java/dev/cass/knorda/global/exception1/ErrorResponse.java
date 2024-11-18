package dev.cass.knorda.global.exception1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ErrorResponse {
	private int status;
	private String message;
}
