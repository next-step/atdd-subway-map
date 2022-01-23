package nextstep.subway.ui.exception.handler;

import nextstep.subway.ui.error.ErrorResponse;
import nextstep.subway.ui.exception.BadRequestException;
import nextstep.subway.ui.exception.ConflictRequestException;
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
