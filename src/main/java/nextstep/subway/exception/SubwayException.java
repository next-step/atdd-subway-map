package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public class SubwayException {

	public static class DuplicatedNameException extends BusinessException {
		private static final String DEFAULT_ERROR_MESSAGE = "Duplicate name";

		public DuplicatedNameException() {
			super(DEFAULT_ERROR_MESSAGE, HttpStatus.BAD_REQUEST.value());
		}

		public DuplicatedNameException(String message, int code) {
			super(message, code);
		}
	}
}
