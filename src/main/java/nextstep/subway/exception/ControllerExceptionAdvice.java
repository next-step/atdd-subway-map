package nextstep.subway.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleException(BusinessException businessException) {
		return ResponseEntity.status(businessException.getStatus())
			.contentType(MediaType.APPLICATION_JSON)
			.body(ErrorResponse.of(businessException.getStatus(),
				businessException.getErrorMessage()));
	}

}

