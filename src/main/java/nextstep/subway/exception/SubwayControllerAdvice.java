package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayControllerAdvice {

	@ExceptionHandler(SubwayException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ExceptionResponse subwayExceptionHandle(SubwayException e) {
		return new ExceptionResponse(
						HttpStatus.BAD_REQUEST.value(),
						e.getMessage()
				);
	}

}
