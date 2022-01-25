package nextstep.subway.exception;

public class DuplicateStoreException extends IllegalArgumentException{
	private static final String DEFAULT_MESSAGE = "중복된 값이 존재합니다.";

	public DuplicateStoreException() {
		super(DEFAULT_MESSAGE);
	}

	public DuplicateStoreException(String message) {
		super(message);
	}
}
