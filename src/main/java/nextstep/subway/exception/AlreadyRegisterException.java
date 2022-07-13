package nextstep.subway.exception;

public class AlreadyRegisterException extends RuntimeException {
	public AlreadyRegisterException() {
		super();
	}

	public AlreadyRegisterException(String message) {
		super(message);
	}

	public AlreadyRegisterException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyRegisterException(Throwable cause) {
		super(cause);
	}
}
