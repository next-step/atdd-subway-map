package nextstep.subway.exception;

public class SameUpStationException extends RuntimeException {
	public SameUpStationException() {
		super();
	}

	public SameUpStationException(String message) {
		super(message);
	}

	public SameUpStationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SameUpStationException(Throwable cause) {
		super(cause);
	}
}
