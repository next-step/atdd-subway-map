package nextstep.subway.ui.exception;

public class LineNotFoundException extends RuntimeException {
	private static final String DEFAULT_EXCEPTION_MESSAGE = "not found Line";

	public LineNotFoundException() {
		super(DEFAULT_EXCEPTION_MESSAGE);
	}

	public LineNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
