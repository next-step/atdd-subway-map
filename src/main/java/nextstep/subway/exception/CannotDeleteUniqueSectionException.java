package nextstep.subway.exception;

public class CannotDeleteUniqueSectionException extends RuntimeException {
	public CannotDeleteUniqueSectionException() {
	}

	public CannotDeleteUniqueSectionException(String message) {
		super(message);
	}

	public CannotDeleteUniqueSectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotDeleteUniqueSectionException(Throwable cause) {
		super(cause);
	}
}
