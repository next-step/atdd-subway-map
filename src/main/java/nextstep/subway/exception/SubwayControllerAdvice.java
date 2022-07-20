package nextstep.subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubwayControllerAdvice {
	@ExceptionHandler(SubwayException.class)
	public ResponseEntity<String> subwayExceptionHandle() {
		return ResponseEntity.badRequest().build();
	}
}
