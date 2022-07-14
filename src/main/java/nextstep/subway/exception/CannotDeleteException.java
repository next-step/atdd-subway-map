package nextstep.subway.exception;

public class CannotDeleteException extends RuntimeException{
	public CannotDeleteException() {
	}

	public CannotDeleteException(String message) {
		super(message);
	}

	public CannotDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotDeleteException(Throwable cause) {
		super(cause);
	}
}
