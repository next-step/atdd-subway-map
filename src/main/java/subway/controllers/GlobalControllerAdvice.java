package subway.controllers;

import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler({EntityNotFoundException.class})
	public ResponseEntity<Void> exceptionHandler(EntityNotFoundException e) {
		logger.error(e.toString());
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<Void> exceptionHandler(IllegalArgumentException e) {
		logger.error(e.toString());
		return ResponseEntity.badRequest().build();
	}
}
