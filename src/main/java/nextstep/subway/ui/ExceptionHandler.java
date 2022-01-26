package nextstep.subway.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> stationExceptionHandler() {
		return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
}
