package nextstep.subway.common.response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonControllerAdvice {

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<Void> badArgumentException(Exception e) {
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(value = IllegalStateException.class)
	public ResponseEntity<Void> badStatusException(Exception e) {
		return ResponseEntity.badRequest().build();
	}
}
