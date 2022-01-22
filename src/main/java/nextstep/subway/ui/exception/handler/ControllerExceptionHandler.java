package nextstep.subway.ui.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.ui.error.ErrorResponse;
import nextstep.subway.ui.exception.UniqueKeyExistsException;

@RestControllerAdvice(basePackages = "nextstep.subway")
public class ControllerExceptionHandler {

	@ExceptionHandler(UniqueKeyExistsException.class)
	public ResponseEntity<ErrorResponse> uniqueKeyExists(Exception exception) {
		return ResponseEntity
			.status(HttpStatus.CONFLICT)
			.body(ErrorResponse.from(exception.getMessage()));
	}

}
