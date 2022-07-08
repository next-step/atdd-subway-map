package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers<T> {

	@ExceptionHandler(SubwayException.class)
	public ResponseEntity<T> handleFileException() {
		return new ResponseEntity<T>(HttpStatus.BAD_REQUEST);
	}

}
