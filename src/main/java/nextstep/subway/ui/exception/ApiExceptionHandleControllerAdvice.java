package nextstep.subway.ui.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandleControllerAdvice {

	@ExceptionHandler(LineNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleCustomException(LineNotFoundException e) {
		return e.getMessage();
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception e) {
		return e.getMessage();
	}
}
