package nextstep.subway.exception;

import nextstep.subway.ui.ExceptionMessage;

public class DuplicateStoreException extends IllegalArgumentException{
	public DuplicateStoreException() {
		super(ExceptionMessage.DUPLICATE_VALUE.getMessage());
	}

	public DuplicateStoreException(String message) {
		super(message);
	}
}
