package nextstep.subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.DuplicateStoreException;
import nextstep.subway.exception.NotFoundException;

@RestControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(DuplicateStoreException.class)
	public ResponseEntity<Void> duplicationExceptionHandler() {
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Void> notFoundExceptionHandler() {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
