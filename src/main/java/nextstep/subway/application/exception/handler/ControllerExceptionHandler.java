package nextstep.subway.application.exception.handler;

import nextstep.subway.application.error.ErrorResponse;
import nextstep.subway.application.exception.BadRequestException;
import nextstep.subway.application.exception.ConflictRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "nextstep.subway")
public class ControllerExceptionHandler {

	@ExceptionHandler(ConflictRequestException.class)
	public ResponseEntity<ErrorResponse> conflictRequest(Exception exception) {
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(ErrorResponse.from(exception.getMessage()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> badRequest(Exception exception) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.from(exception.getMessage()));
	}

}
