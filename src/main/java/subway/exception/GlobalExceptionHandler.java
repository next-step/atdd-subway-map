package subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public void entityNotFoundExceptionHandler(EntityNotFoundException exception) {
		log.info("error occurred: {}", exception.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(StationLineCreateException.class)
	public void stationLineCreateExceptionHandler(StationLineCreateException exception) {
		log.info("error occurred: {}", exception.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(StationLineSectionCreateException.class)
	public void stationLineSectionCreateExceptionHandler(StationLineSectionCreateException exception) {
		log.info("error occurred: {}", exception.getMessage());
	}
}
