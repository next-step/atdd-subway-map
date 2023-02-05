package subway;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubwayExceptionHandler {

	@ExceptionHandler(value = {NoSuchElementException.class, IllegalArgumentException.class})
	public ResponseEntity NoSuchElementException() {
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
}
