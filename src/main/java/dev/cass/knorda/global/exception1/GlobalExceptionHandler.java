package dev.cass.knorda.global.exception1;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * RestControllerAdvice - @ControllerAdvice + @ResponseBody
 * ControllerAdvice - 컨트롤러에서 발생하는 예외를 잡아서 처리해주는 역할
 * ResponseBody - 메소드의 반환값을 HTTP Response Body로 전달
 * 즉, 모든 컨트롤러에서 throw하는 예외를 잡아서 처리해 주는데, 그 return 값을 JSON으로 만들어서 응답해준다는 뜻
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		log.error("BusinessException : {}", e.getMessage());
		return response(e.getStatus(), e.getMessage());
	}

	/**
	 * Valid 어노테이션을 통한 검증 실패 시 발생하는 예외를 처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("MethodArgumentNotValidException : {}", e.getMessage());

		StringBuilder message = new StringBuilder();

		// 모든 필드의 validation 메시지를 리턴하기 위해서, FieldErrors 를 순회하며 메시지를 append
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			message.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage()).append(" ");
		}

		return response(HttpStatus.BAD_REQUEST, message.toString());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		log.error("DataIntegrityViolationException : {}", e.getMessage());
		return response(HttpStatus.BAD_REQUEST, "저장 시 오류가 발생하였습니다.");
	}

	private ResponseEntity<ErrorResponse> response(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(new ErrorResponse(status, message));
	}
}
