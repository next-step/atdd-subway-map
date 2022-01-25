package nextstep.subway.exception;

public class NotFoundException extends NullPointerException {
	private static final String DEFAULT_MESSAGE = "해당하는 정보를 찾을 수 없습니다.";

	public NotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	public NotFoundException(String message) {
		super(message);
	}
}
