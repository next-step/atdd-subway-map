package subway.controllers;

import javax.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
	@ExceptionHandler({EntityNotFoundException.class})
	public ResponseEntity<Void> exceptionHandler(EntityNotFoundException e) {
		e.printStackTrace();
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<Void> exceptionHandler(IllegalArgumentException e) {
		e.printStackTrace();
		return ResponseEntity.badRequest().build();
	}
}
