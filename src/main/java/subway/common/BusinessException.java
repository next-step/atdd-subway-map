package subway.common;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
	private final HttpStatus httpStatus;

	public BusinessException(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
