package nextstep.subway.applicaion.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorController {

	@ExceptionHandler(value = {NoSuchElementException.class})
	public ResponseEntity<ErrorResponse> noElementAdviser(NoSuchElementException e) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ErrorResponse(e.getMessage(), HttpStatus.NO_CONTENT));
	}
}
