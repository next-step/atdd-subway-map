package nextstep.subway.ui.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandleControllerAdvice {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Void> handleNotFoundException() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> handleException() {
		return ResponseEntity.internalServerError().build();
	}
}
