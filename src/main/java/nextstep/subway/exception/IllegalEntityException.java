package nextstep.subway.exception;

import nextstep.subway.ui.ExceptionMessage;

public class IllegalEntityException extends IllegalArgumentException{
	public IllegalEntityException() {
		super(ExceptionMessage.DEFAULT_MESSAGE.getMessage());
	}

	public IllegalEntityException(String message) {
		super(message);
	}
}
