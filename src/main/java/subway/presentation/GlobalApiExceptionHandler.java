package subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import subway.exception.ErrorCode;
import subway.exception.SubwayException;

@Slf4j
@RestControllerAdvice
public class GlobalApiExceptionHandler {

	@ExceptionHandler(SubwayException.class)
	public ResponseEntity<ApiErrorResponse> handleSubwayException(SubwayException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		log.error("message: {} code: {}", exception.getMessage(), errorCode.getCode());
		return ResponseEntity.status(errorCode.getStatusCode())
			.body(ApiErrorResponse.of(errorCode));
	}
}
