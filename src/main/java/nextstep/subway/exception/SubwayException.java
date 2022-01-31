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

	public static class NotFoundException extends BusinessException {
		private static final String DEFAULT_ERROR_MESSAGE = "Can not found";

		public NotFoundException() {
			super(DEFAULT_ERROR_MESSAGE, HttpStatus.NO_CONTENT.value());
		}

		public NotFoundException(String message, int code) {
			super(message, code);
		}
	}

	public static class CanNotDeleteException extends BusinessException {
		private static final String DEFAULT_ERROR_MESSAGE = "Can not found";

		public CanNotDeleteException() {
			super(DEFAULT_ERROR_MESSAGE, HttpStatus.CONTINUE.value());
		}

		public CanNotDeleteException(String message, int code) {
			super(message, code);
		}
	}
}
