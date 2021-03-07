package nextstep.subway.common.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.line.exception.NotFoundLineException;
import nextstep.subway.station.exception.NotFoundStationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {NotFoundLineException.class, NotFoundStationException.class})
	public ResponseEntity<String> notFoundExceptionHandler(BusinessException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().build();
	}
}
