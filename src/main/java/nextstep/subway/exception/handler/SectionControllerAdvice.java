package nextstep.subway.exception.handler;

import nextstep.subway.exception.AlreadyRegisterException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.SameUpStationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SectionControllerAdvice {

	@ExceptionHandler()
	public ResponseEntity<Void> addAlreadyRegisterExceptionHandler(AlreadyRegisterException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler()
	public ResponseEntity<Void> addSameUpStationExceptionHandler(SameUpStationException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler()
	public ResponseEntity<Void> addCacnnotDeleteException(CannotDeleteException e) {
		return ResponseEntity.badRequest().build();
	}
}
