package nextstep.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nextstep.subway.exception.BusinessException;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(BusinessException.class)
	public ResponseEntity<Void> businessExceptionHandler(final BusinessException exception) {
		return ResponseEntity.status(exception.getCode()).build();
	}
}
